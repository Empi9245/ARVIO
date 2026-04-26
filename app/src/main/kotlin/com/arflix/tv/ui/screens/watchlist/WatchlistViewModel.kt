package com.arflix.tv.ui.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arflix.tv.data.model.MediaItem
import com.arflix.tv.data.model.MediaType
import com.arflix.tv.data.repository.CloudSyncRepository
import com.arflix.tv.data.repository.MediaRepository
import com.arflix.tv.data.repository.TraktRepository
import com.arflix.tv.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ToastType {
    SUCCESS, ERROR, INFO
}

data class WatchlistUiState(
    val isLoading: Boolean = true,
    val items: List<MediaItem> = emptyList(),
    val error: String? = null,
    // Toast
    val toastMessage: String? = null,
    val toastType: ToastType = ToastType.INFO
)

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository,
    private val cloudSyncRepository: CloudSyncRepository,
    private val traktRepository: TraktRepository,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    private val _logoUrls = MutableStateFlow<Map<String, String>>(emptyMap())
    val logoUrls: StateFlow<Map<String, String>> = _logoUrls.asStateFlow()
    private var syncJob: Job? = null

    init {
        showCachedWatchlist()
        observeWatchlistChanges()
        syncTraktWatchlist()
    }

    private fun observeWatchlistChanges() {
        viewModelScope.launch {
            watchlistRepository.watchlistItems.collect { items ->
                if (items.isNotEmpty() || _uiState.value.items.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        items = items,
                        isLoading = false
                    )
                    fetchLogos(items)
                }
            }
        }
    }

    private fun fetchLogos(items: List<MediaItem>) {
        viewModelScope.launch {
            val currentLogos = _logoUrls.value.toMutableMap()
            for (item in items) {
                val key = "${item.mediaType}_${item.id}"
                if (key in currentLogos) continue
                val url = runCatching { mediaRepository.getLogoUrl(item.mediaType, item.id) }.getOrNull()
                if (url != null) {
                    currentLogos[key] = url
                    _logoUrls.value = currentLogos.toMap()
                }
            }
        }
    }

    private fun showCachedWatchlist() {
        val cachedItems = watchlistRepository.getCachedItems()
        if (cachedItems.isNotEmpty()) {
            _uiState.value = WatchlistUiState(
                isLoading = false,
                items = cachedItems
            )
            fetchLogos(cachedItems)
        } else {
            _uiState.value = WatchlistUiState(isLoading = true)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Sync Trakt first so any items added externally show up in this refresh.
                syncTraktWatchlistSuspend()
                val items = watchlistRepository.refreshWatchlistItems()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    items = items
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    toastMessage = "Failed to refresh",
                    toastType = ToastType.ERROR
                )
            }
        }
    }

    fun removeFromWatchlist(item: MediaItem) {
        viewModelScope.launch {
            try {
                // Optimistic update - remove from local state immediately
                val updatedItems = _uiState.value.items.filter { it.id != item.id || it.mediaType != item.mediaType }
                _uiState.value = _uiState.value.copy(
                    items = updatedItems,
                    toastMessage = "Removed from watchlist",
                    toastType = ToastType.SUCCESS
                )
                // Then sync to backend
                watchlistRepository.removeFromWatchlist(item.mediaType, item.id)
                // Also remove from Trakt if connected
                runCatching { traktRepository.removeFromWatchlist(item.mediaType, item.id) }
                runCatching { cloudSyncRepository.pushToCloud() }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    toastMessage = "Failed to remove from watchlist",
                    toastType = ToastType.ERROR
                )
            }
        }
    }

    /**
     * Pull Trakt watchlist into local storage.
     * When Trakt is connected, Trakt is authoritative for order and membership.
     */
    private fun syncTraktWatchlist() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            runCatching { syncTraktWatchlistSuspend() }
        }
    }

    private suspend fun syncTraktWatchlistSuspend() {
        try {
            val traktItems = traktRepository.getWatchlistOrNull()  // newest-first by listed_at
            if (traktItems != null) {
                watchlistRepository.syncFromTraktOrder(traktItems, keepLocalOnly = false)
            }

            val items = watchlistRepository.refreshWatchlistItems()
            _uiState.value = _uiState.value.copy(items = items, isLoading = false)
            runCatching { cloudSyncRepository.pushToCloud() }
        } catch (_: Exception) {
            // Trakt sync is best-effort, don't show errors
        }
    }

    fun dismissToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }
}


