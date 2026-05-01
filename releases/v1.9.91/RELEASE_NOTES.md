# ARVIO 1.9.91

## TV, IPTV, and mobile playback
- Reworked the IPTV category sidebar so provider categories are easier to use while keeping ARVIO's matched groups available under All Channels. Contributor: ARVIO maintainers.
- Restored IPTV category context actions, including hide/reorder behavior for managed channel groups. Contributor: ARVIO maintainers.
- Removed repeated playlist labels from channel rows to make large IPTV lists cleaner and easier to scan. Contributor: ARVIO maintainers.
- Fixed IPTV VOD source handling so all available quality variants can appear instead of only one. Contributor: ARVIO maintainers.
- Added proper mobile and tablet fullscreen behavior for the TV player, including hiding the bottom navigation while fullscreen. Contributor: ARVIO maintainers.

## Watchlist, Trakt, and continue watching
- Fixed Trakt watchlist matching and newest-added ordering regressions so watchlists better match Trakt. Contributor: ARVIO maintainers.
- Fixed watchlist empty-state regressions where items could load briefly and then disappear. Contributor: ARVIO maintainers.
- Improved watchlist loading focus and centered loading behavior. Contributor: ARVIO maintainers.
- Made Continue Watching appear faster on startup by showing cached items first, then refreshing. Contributor: ARVIO maintainers.
- Improved Continue Watching source isolation so profile and source data do not bleed together. Contributor: ARVIO maintainers.
- Fixed anime season and episode matching issues that could send debrid/source searches to the wrong episode. Contributor: ARVIO maintainers.

## Playback and player controls
- Improved stream startup path for faster source selection and playback start. Contributor: ARVIO maintainers.
- Fixed source switching so playback resumes from the previous position instead of restarting unexpectedly. Contributor: Sage Gavin Davids.
- Improved player back-button behavior so controls are hidden before exiting playback. Contributor: Himanth Reddy.
- Added a larger loading clearlogo with subtle heartbeat motion for a more polished loading state. Contributor: Sage Gavin Davids.
- Improved trailer playback internals and added trailer volume control. Contributor: Himanth Reddy.

## Catalogs, discovery, and navigation
- Added catalog list discovery so users can search public Trakt and MDBList lists from settings instead of only pasting URLs. Contributor: ARVIO maintainers.
- Improved the catalog discovery add flow so adding a list is clearer and faster. Contributor: ARVIO maintainers.
- Fixed focus visibility in catalog discovery search results. Contributor: ARVIO maintainers.
- Added catalog row layout controls and limited layout updates to the intended non-collection rows. Contributor: Himanth Reddy.
- Improved home/catalog focus stability, row height calculation, and rail movement to reduce focus jumping. Contributor: Himanth Reddy.
- Fixed catalog navigation and scroll restoration issues. Contributor: silentbil.
- Reduced lag around Top 10 and heavy home rows. Contributor: silentbil.

## Settings, profiles, language, and cloud
- Fixed TV cloud login flow and startup language restore so selected language survives app restart. Contributor: ARVIO maintainers.
- Added wider app language coverage using Android resource translations. Contributor: silentbil.
- Exposed subtitle language filtering in mobile settings and added preferred-language subtitle filtering. Contributors: ARVIO maintainers and silentbil.
- Redesigned subtitle selection into a clearer language/track layout. Contributor: silentbil.
- Fixed DNS provider persistence across app updates and restarts. Contributor: Himanth Reddy.
- Fixed TV focus in the regex filter popup. Contributor: Sage Gavin Davids.
- Improved skip-profile-selection handling and active profile loading state. Contributor: Himanth Reddy.

## Details, home, and metadata
- Fixed details pages to respect home metadata toggles and show network logos consistently. Contributor: Sage Gavin Davids.
- Updated Crunchyroll service assets and hero video metadata. Contributor: Himanth Reddy.
- Improved details and TV episode spacing stability. Contributor: ARVIO maintainers.
- Fixed details row state resets when switching between shows. Contributor: ARVIO maintainers.
- Removed Favorite TV from homescreen catalog generation. Contributor: ARVIO maintainers.

## Contributors
- Sage Gavin Davids: player source switching, loading clearlogo polish, regex filter TV focus, details metadata behavior.
- Himanth Reddy: DNS persistence, player/back behavior, catalog layout controls, focus/rail stability, Crunchyroll assets, trailer/profile improvements.
- silentbil: app language resources, subtitle filtering UI, catalog navigation restoration, Top 10 lag reduction, player focus fixes.
- ARVIO maintainers: IPTV category/sidebar work, VOD quality variants, watchlist/Trakt fixes, continue watching, anime episode matching, catalog discovery, cloud login/language restore, mobile fullscreen TV.

## Download
- AFTVnews / Downloader code: `9383706`
- Direct APK: `https://gitlab.com/arvio1/ARVIO/-/raw/main/releases/v1.9.91/ARVIO%20V1.9.91.apk?inline=false`
