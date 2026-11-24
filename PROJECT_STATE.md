# PROJECT_STATE: Downlink (Android)

## Ringkasan Proyek
Downlink (sebelumnya YT2MP3) adalah aplikasi Android offline media downloader & converter native (Kotlin + Jetpack Compose) yang memanfaatkan engine yt-dlp (`youtubedl-android`) dan FFmpeg tersemat lokal. Aplikasi berjalan sepenuhnya di perangkat tanpa server eksternal, akun, maupun iklan.

## Status Terkini
- **Versi**: v1.5.0 (`versionCode 6`, `versionName "1.5.0"`)
- **Branding**: Rebranding selesai dari YT2MP3 ke **Downlink**.
- **Repositori Git**: `https://github.com/yehudaaadev/Downlink.git`
- **Branch**: `main`

## Tech Stack & Arsitektur
- **Bahasa & Platform**: Kotlin 1.9.23, Android SDK (compileSdk 34, targetSdk 34, minSdk 30), Java 17.
- **UI Toolkit**: Jetpack Compose + Material 3, dynamic theming, reduced-motion friendly.
- **Penyimpanan & Database**: Android Storage Access Framework (SAF), Room Database (schema migrations up to v4).
- **Core Engine**: `youtubedl-android` (yt-dlp wrapper) + FFmpeg binary native (arm64-v8a / armeabi-v7a).
- **Update Mechanism**: In-app self-updater (`AppUpdater.kt`) membaca rilis tag GitHub terbaru dari `https://api.github.com/repos/yehudaaadev/Downlink/releases/latest`.

## Perubahan v1.5.0
1. Rebrand seluruh teks aplikasi, strings, header, notifikasi, dan file APK output ke nama **Downlink**.
2. Fitur edit tag ID3 MP3 langsung dari riwayat unduhan (`HistoryScreen.kt`, `Id3Writer.kt`).
3. Scan QR code kamera untuk input link media secara instan (`HomeScreen.kt`).
4. Baris tombol Home dibuat scrollable secara horizontal pada layar sempit.
5. Perbaikan sinkronisasi notifikasi unduhan agar tidak hilang saat transisi ke tahap konversi (`QueueRunner.kt`, `DownloadForegroundService.kt`).
6. Granular SponsorBlock Configuration: Penambahan seleksi kategori terpisah (Sponsor, Intro/Outro, Non-music klip, Self-promo, Ajakan interaksi) di Home dan Settings (`VideoModels.kt`, `YtDlpEngine.kt`, `SettingsScreen.kt`, `HomeScreen.kt`).

## Roadmap Pengembangan Bertahap
- [x] Fitur 1: Granular SponsorBlock Configuration
- [ ] Fitur 2: Visual Storage Analyzer (Bar distribusi memori di Settings)
- [ ] Fitur 3: Built-in 5-Band Equalizer & Audio FX di Pemutar
- [ ] Fitur 4: Interactive Stream Inspector (Daftar stream video/audio detail sebelum unduh)

## Langkah Berikutnya
- Lanjutkan ke Fitur 2: Visual Storage Analyzer
- Jalankan build APK rilis: `.\gradlew.bat :app:assembleRelease`
- Unggah file APK rilis (`downlink-arm64-v8a-release.apk` dan `downlink-armeabi-v7a-release.apk`) ke GitHub Releases v1.5.0.
