package com.arflix.tv.ui.screens.tv.live

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arflix.tv.ui.theme.InterFontFamily

// ARVIO Live TV — design tokens. OKLCH reference kept in spec.md §2.
// Mapped from handoff/tokens.kt. `InterFontFamily` ships; JetBrains Mono
// falls back to system Monospace (Inter's tabular figures are acceptable
// for the numeric/badge slots; can swap for bundled JBMono later).

object LiveColors {
    val Bg           = Color(0xFF16171B) // oklch(0.13 0.008 270)
    val Panel        = Color(0xFF1C1D22) // oklch(0.16 0.01 270)
    val PanelDeep    = Color(0xFF1A1B20) // oklch(0.15 0.01 270)
    val PanelRaised  = Color(0xFF20212A) // oklch(0.18 0.012 270)

    val Divider       = Color(0x992B2D36)
    val DividerStrong = Color(0xE6333542)

    val Fg     = Color(0xFFF5F5F8)
    val FgDim  = Color(0xFFB5B6BE)
    val FgMute = Color(0xFF7D7E86)

    val Accent    = Color(0xFFF5C26B)
    val AccentDim = Color(0xFFB68634)
    val FocusBg   = Color(0x1AF5C26B)

    val LiveRed = Color(0xFFFF3B30)
    val Online  = Color(0xFF4ADE80)

    data class Brand(val bg: Color, val fg: Color)
    val BrandNews    = Brand(Color(0xFF8A2F2F), Color(0xFFFDE7D4))
    val BrandSport   = Brand(Color(0xFF0B6131), Color(0xFFEAFFF1))
    val BrandMovies  = Brand(Color(0xFF1A1A2E), Color(0xFFF5C26B))
    val BrandSeries  = Brand(Color(0xFF3A1552), Color(0xFFE9D2FF))
    val BrandKids    = Brand(Color(0xFFF3B13A), Color(0xFF1A1308))
    val BrandMusic   = Brand(Color(0xFF2A2A6E), Color(0xFFC8D4FF))
    val BrandDocs    = Brand(Color(0xFF1D3F3A), Color(0xFFCFE9E3))
    val BrandGeneral = Brand(Color(0xFF1B2B5A), Color(0xFFE8EFFB))
}

val LiveMono: FontFamily = FontFamily.Monospace

object LiveType {
    val ChannelName  = TextStyle(fontFamily = InterFontFamily, fontSize = 22.sp, fontWeight = FontWeight.W700, letterSpacing = (-0.44).sp)
    val ProgramTitle = TextStyle(fontFamily = InterFontFamily, fontSize = 16.sp, fontWeight = FontWeight.W600, letterSpacing = (-0.16).sp)
    val CellTitle    = TextStyle(fontFamily = InterFontFamily, fontSize = 14.sp, fontWeight = FontWeight.W600, letterSpacing = (-0.14).sp)
    val BodySynopsis = TextStyle(fontFamily = InterFontFamily, fontSize = 13.sp, fontWeight = FontWeight.W400)
    val CatLabel     = TextStyle(fontFamily = InterFontFamily, fontSize = 14.sp, fontWeight = FontWeight.W500)
    val SectionTag   = TextStyle(fontFamily = LiveMono, fontSize = 10.sp, fontWeight = FontWeight.W700, letterSpacing = 1.6.sp)
    val Badge        = TextStyle(fontFamily = LiveMono, fontSize = 10.sp, fontWeight = FontWeight.W700, letterSpacing = 1.2.sp)
    val TimeMono     = TextStyle(fontFamily = LiveMono, fontSize = 12.sp, fontWeight = FontWeight.W500)
    val NumberMono   = TextStyle(fontFamily = LiveMono, fontSize = 11.sp, fontWeight = FontWeight.W600)
}

object LiveDims {
    val SidebarExpanded  = 260.dp
    val SidebarCollapsed = 76.dp

    val MiniPlayerWidth  = 440.dp
    val MiniPlayerHeight = 248.dp

    val EpgChannelColWidth = 340.dp
    val EpgRowHeight       = 84.dp
    val EpgHeaderHeight    = 44.dp
    val EpgPxPerMinute     = 5
    val EpgHalfHourWidth   = 150.dp

    val PanelRadius     = 12.dp
    val CardRadius      = 10.dp
    val CellRadius      = 6.dp
    val VideoRadius     = 12.dp
    val FocusBorder     = 2.dp
    val ActiveIndicator = 3.dp
}

val LocalLiveColors = staticCompositionLocalOf { LiveColors }
val LocalLiveType   = staticCompositionLocalOf { LiveType }
val LocalLiveDims   = staticCompositionLocalOf { LiveDims }
