# mpvRx Final Cinematic UI

This branch turns the local-first browser into a single visual system inspired by the approved cinematic reference.

## Visual contract

- Dark cinematic background with purple / blue / pink neon accents.
- Flowing upper and lower ribbons behind content.
- Glass-like rounded panels with gradient borders.
- Hero media, local thumbnails and folder cards are real device content; no online catalogue data is required for the home experience.
- The bottom navigation has a moving active orb that follows pager swipes.
- Home, Music, Recents, Playlists and browser screens share the same palette and ambient chrome.
- Folder and episode views retain the same rounded cards, glow, spacing and recent-item pulse instead of reverting to the old flat browser appearance.
- Recently used folders/videos receive a breathing border and soft neon halo.
- Folder lists expose a cinematic library header with real folder/video/size totals.

## Local-first rule

Artwork and metadata shown by the cinematic UI are resolved from local media, MediaStore/file scanning, thumbnail generation, playback state, favorites and playlists already present in mpvRx. The reference artwork is stored under `docs/design/cinematic-reference.png` for design review only and is **not** used as the application's media catalogue or as a static UI screenshot.

## Main implementation points

- `ui/theme/CinematicDesign.kt` — palette, ambient motion and flowing ribbons.
- `ui/theme/CinematicChrome.kt` — reusable cinematic shell/panel chrome.
- `ui/browser/MainScreen.kt` — animated pager and curved/glowing bottom navigation.
- `ui/browser/HomeDashboardScreen.kt` — local cinematic home dashboard.
- `ui/browser/folderlist/FolderListScreen.kt` — cinematic library header and themed folder browsing.
- `ui/browser/videolist/VideoListScreen.kt` — cinematic folder header and themed episode/video browsing.
- `ui/browser/cards/FolderCard.kt` — active/recent folder breathing glow.
- `ui/browser/cards/VideoCard.kt` — active/recent video breathing glow.
- `ui/browser/components/BrowserTopBar.kt` — unified rounded browser top bar.

## Build verification

A full Gradle build was attempted in the execution environment, but the environment could not download Gradle 9.7.1 because external DNS/network access was unavailable. Source-level delimiter checks were completed for all files changed in the final visual pass.
