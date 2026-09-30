# Local cinematic home dashboard

This build replaces the HOME tab in `MainScreen` with `HomeDashboardScreen`.

The dashboard is local-first:
- scans local video folders through the existing `MediaFileRepository`
- uses the existing `ThumbnailRepository` for real video thumbnails
- uses existing recently-played and playlist/favorites databases
- opens existing `VideoListScreen` for folders
- launches videos through existing `MediaUtils.playFile`
- exposes local filters for 4K/HDR/subtitles/long/short media
- shows storage usage from the device filesystem

The existing player, database, thumbnail cache, folder browser and other tabs remain intact.
