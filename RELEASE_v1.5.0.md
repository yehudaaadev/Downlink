# Downlink v1.5.0

## Yang baru

- **Edit tag MP3** — ubah judul dan artis dari riwayat; tag ID3 di file MP3 yang
  sudah tersimpan ikut ditulis ulang.
- **Scan QR** — masukkan link langsung dari kamera, tanpa mengetik.
- Baris tombol di Home bisa digeser horizontal di layar sempit.
- Perbaikan: notifikasi unduhan tidak hilang saat tahap konversi, dan
  penulisan tag saat unduhan tidak lagi dobel.

## Aset rilis

Unggah kedua APK hasil `:app:assembleRelease`:

- `downlink-arm64-v8a-release.apk` — HP modern (mayoritas)
- `downlink-armeabi-v7a-release.apk` — HP 32-bit

Jangan ubah nama file: `AppUpdater` memilih APK berdasarkan nama ABI
(`arm64-v8a` / `armeabi-v7a`). Tandai rilis sebagai **Latest**, bukan draft
atau pre-release, agar endpoint `/releases/latest` mengembalikannya.

## Checklist

- [x] `versionCode` 6, `versionName` 1.5.0
- [x] Unit test lolos
- [x] APK release ditandatangani keystore rilis yang sama dengan 1.4.0
- [ ] Commit + tag `v1.5.0`, push
- [ ] Buat GitHub Release `v1.5.0` + unggah 2 APK
- [ ] Uji update dari v1.4.0 tanpa uninstall (signature harus sama)
- [ ] Cek dialog update membaca rilis ini
