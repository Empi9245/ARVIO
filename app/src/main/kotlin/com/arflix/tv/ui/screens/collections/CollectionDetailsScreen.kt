package com.arflix.tv.ui.screens.collections

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arflix.tv.data.model.CatalogConfig
import com.arflix.tv.data.model.MediaItem
import com.arflix.tv.data.model.MediaType
import com.arflix.tv.data.repository.CatalogRepository
import com.arflix.tv.data.repository.MediaRepository
import com.arflix.tv.data.repository.StreamRepository
import com.arflix.tv.ui.components.AppTopBar
import com.arflix.tv.ui.components.MediaCard
import com.arflix.tv.ui.components.SidebarItem
import com.arflix.tv.ui.theme.ArflixTypography
import com.arflix.tv.ui.theme.BackgroundDark
import com.arflix.tv.ui.theme.TextPrimary
import com.arflix.tv.ui.theme.TextSecondary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CollectionDetailsUiState(
    val catalog: CatalogConfig? = null,
    val items: List<MediaItem> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class CollectionDetailsViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val mediaRepository: MediaRepository,
    private val streamRepository: StreamRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CollectionDetailsUiState())
    val uiState: StateFlow<CollectionDetailsUiState> = _uiState.asStateFlow()

    fun load(catalogId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val catalog = catalogRepository.getCatalogs().firstOrNull { it.id == catalogId }
            if (catalog == null) {
                _uiState.value = CollectionDetailsUiState(isLoading = false, error = "Collection not found")
                return@launch
            }
            if (catalog.requiredAddonUrls.isNotEmpty()) {
                runCatching { streamRepository.ensureCustomAddons(catalog.requiredAddonUrls) }
            }
            val page = runCatching {
                mediaRepository.loadCollectionCatalogPage(catalog, offset = 0, limit = 80)
            }.getOrNull()
            _uiState.value = CollectionDetailsUiState(
                catalog = catalog,
                items = page?.items.orEmpty(),
                isLoading = false,
                error = if (page == null) "Failed to load collection" else null
            )
        }
    }
}

@Composable
fun CollectionDetailsScreen(
    catalogId: String,
    currentProfile: com.arflix.tv.data.model.Profile? = null,
    viewModel: CollectionDetailsViewModel = hiltViewModel(),
    onNavigateToDetails: (MediaType, Int) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToWatchlist: () -> Unit,
    onNavigateToTv: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(catalogId) { viewModel.load(catalogId) }
    BackHandler(onBack = onBack)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        AppTopBar(
            selectedItem = SidebarItem.HOME,
            isFocused = false,
            focusedIndex = 0,
            profile = currentProfile,
            profileCount = if (currentProfile != null) 1 else 0,
            clockFormat = "24h"
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 36.dp, end = 36.dp, top = 110.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    androidx.tv.material3.Text(
                        text = uiState.catalog?.title ?: "Collection",
                        style = ArflixTypography.sectionTitle.copy(fontSize = 28.sp),
                        color = TextPrimary
                    )
                    if (!uiState.catalog?.collectionDescription.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.tv.material3.Text(
                            text = uiState.catalog?.collectionDescription.orEmpty(),
                            style = ArflixTypography.body,
                            color = TextSecondary
                        )
                    }
                }
            }

            if (uiState.error != null) {
                item {
                    androidx.tv.material3.Text(text = uiState.error!!, color = Color(0xFFFF8A80))
                }
            }

            if (!uiState.isLoading && uiState.items.isEmpty() && uiState.error == null) {
                item {
                    androidx.tv.material3.Text(
                        text = "This collection is empty. Check that its required addons are installed and enabled.",
                        color = TextSecondary
                    )
                }
            }

            items(uiState.items, key = { "${it.mediaType}-${it.id}" }) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToDetails(item.mediaType, item.id) }
                ) {
                    MediaCard(
                        item = item,
                        width = 320.dp,
                        isLandscape = true,
                        showTitle = true,
                        onClick = { onNavigateToDetails(item.mediaType, item.id) }
                    )
                }
            }
        }
    }
}
