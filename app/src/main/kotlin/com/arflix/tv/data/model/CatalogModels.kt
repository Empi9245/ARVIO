package com.arflix.tv.data.model

import java.io.Serializable

enum class CatalogSourceType {
    PREINSTALLED,
    TRAKT,
    MDBLIST,
    ADDON
}

enum class CatalogKind {
    STANDARD,
    COLLECTION
}

enum class CollectionGroupKind {
    SERVICE,
    GENRE,
    DIRECTOR,
    FRANCHISE
}

enum class CollectionSourceKind {
    ADDON_CATALOG,
    TMDB_GENRE,
    TMDB_PERSON
}

data class CollectionSourceConfig(
    val kind: CollectionSourceKind,
    val mediaType: String? = null,
    val addonId: String? = null,
    val addonCatalogType: String? = null,
    val addonCatalogId: String? = null,
    val tmdbGenreId: Int? = null,
    val tmdbPersonId: Int? = null,
    val sortBy: String? = null
) : Serializable

data class CatalogConfig(
    val id: String,
    val title: String,
    val sourceType: CatalogSourceType,
    val sourceUrl: String? = null,
    val sourceRef: String? = null,
    val isPreinstalled: Boolean = false,
    val addonId: String? = null,
    val addonCatalogType: String? = null,
    val addonCatalogId: String? = null,
    val addonName: String? = null,
    val kind: CatalogKind = CatalogKind.STANDARD,
    val collectionGroup: CollectionGroupKind? = null,
    val collectionDescription: String? = null,
    val collectionCoverImageUrl: String? = null,
    val collectionFocusGifUrl: String? = null,
    val collectionHeroImageUrl: String? = null,
    val collectionHeroGifUrl: String? = null,
    val collectionSources: List<CollectionSourceConfig> = emptyList(),
    val requiredAddonUrls: List<String> = emptyList()
) : Serializable

data class CatalogValidationResult(
    val isValid: Boolean,
    val normalizedUrl: String? = null,
    val sourceType: CatalogSourceType? = null,
    val error: String? = null
)
