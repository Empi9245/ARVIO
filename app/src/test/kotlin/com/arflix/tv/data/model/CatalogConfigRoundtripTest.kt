package com.arflix.tv.data.model

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogConfigRoundtripTest {
    @Test
    fun `collectionHeroVideoUrl survives gson round-trip`() {
        val original = CatalogConfig(
            id = "collection_service_netflix",
            title = "Netflix",
            sourceType = CatalogSourceType.PREINSTALLED,
            isPreinstalled = true,
            kind = CatalogKind.COLLECTION,
            collectionGroup = CollectionGroupKind.SERVICE,
            collectionHeroVideoUrl = "https://example.com/netflix.mp4"
        )
        val gson = Gson()
        val json = gson.toJson(original)
        val restored = gson.fromJson(json, CatalogConfig::class.java)
        assertEquals("https://example.com/netflix.mp4", restored.collectionHeroVideoUrl)
    }
}
