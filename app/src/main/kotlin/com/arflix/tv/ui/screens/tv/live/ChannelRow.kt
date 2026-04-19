package com.arflix.tv.ui.screens.tv.live

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.arflix.tv.data.model.IptvNowNext

/** Sticky-left channel column row inside the EPG body. Spec §3.4 (channel column). */
@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChannelRow(
    channel: EnrichedChannel,
    nowNext: IptvNowNext?,
    isActive: Boolean,
    isFavorite: Boolean,
    stripe: Boolean = false,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
) {
    var focused by remember { mutableStateOf(false) }
    val bg = when {
        focused -> LiveColors.PanelRaised
        isActive -> LiveColors.FocusBg
        stripe -> LiveColors.RowStripe
        else -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(LiveDims.EpgRowHeight)
            .background(bg)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onFavoriteToggle,
            )
            .focusable()
            .onFocusChanged { focused = it.isFocused }
            .onKeyEvent { ev ->
                if (ev.type == KeyEventType.KeyDown && ev.key == Key.Menu) {
                    onFavoriteToggle(); true
                } else false
            }
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = channel.number.toString(),
            style = LiveType.NumberMono.copy(color = LiveColors.FgMute),
            modifier = Modifier.size(width = 40.dp, height = 20.dp),
        )
        ChannelLogo(channel = channel, size = 44.dp)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = channel.name,
                    style = LiveType.ProgramTitle.copy(
                        color = if (isActive) LiveColors.Fg else LiveColors.Fg,
                        fontSize = androidx.compose.ui.unit.TextUnit.Unspecified,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = LiveColors.Accent,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
            val now = nowNext?.now
            Text(
                text = now?.title ?: "No info",
                style = LiveType.BodySynopsis.copy(color = LiveColors.FgDim),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (now != null) {
                    Text(
                        text = "ends ${formatClock(now.endUtcMillis)}",
                        style = LiveType.TimeMono.copy(color = LiveColors.FgMute),
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(LiveColors.Panel)
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                ) {
                    Text(channel.quality.label, style = LiveType.Badge.copy(color = LiveColors.FgDim))
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(LiveColors.Panel)
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                ) {
                    Text(channel.lang, style = LiveType.Badge.copy(color = LiveColors.FgMute))
                }
            }
        }
    }
}
