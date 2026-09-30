# mpvRx — Local Cinematic Home / Stage 2

This stage replaces the first dashboard with a complete local-first home experience.

## Implemented

- Animated local Hero carousel with automatic rotation and manual previous/next controls.
- Real resume state from the existing `RecentlyPlayedRepository` and `PlaybackStateRepository`.
- Real thumbnails through the existing `ThumbnailRepository`.
- Local folder rail with video count, total size, total duration and latest file.
- Library events/recently-played rail backed by the existing Room data.
- Favorites rail backed by the existing Favorites playlist.
- Playlist rail backed by the existing `PlaylistRepository`.
- Search field for the locally loaded library.
- Smart filters: all, 4K, HDR, embedded subtitles, long, short.
- Sorting: newest, name, longest, largest.
- Two library presentation modes: cinematic cards and compact grid.
- Animated quick-action ribbon.
- Local storage/library statistics dialog.
- Quick playback-control deck linking to the existing settings screen.
- Animated storage usage meter.
- All video/folder cards remain wired to the existing `MediaUtils.playFile()` and `VideoListScreen` navigation.
- The existing bottom navigation remains the app-level tab bar; Home is the cinematic local-library tab.

## Important architecture choice

No online movie catalogue, remote posters, TMDB data, or fake media entries were added to Home. The visual language is inspired by cinematic media centers, while the content comes from files and Room state already present in mpvRx.

## Verification

A full Gradle Kotlin compilation was attempted. The execution environment could not download the project's Gradle 9.7.1 distribution because outbound network access/DNS was unavailable (`UnknownHostException: services.gradle.org`). The project was therefore not falsely marked as build-verified.
