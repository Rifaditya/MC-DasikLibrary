# Panduan Minecraft 26.2+

| Parameter | Spesifikasi |
| :--- | :--- |
| **Versi Target Minecraft** | `26.2` (kompatibel ke depan dengan `26.x`) |
| **Persyaratan Fabric Loader** | `>=0.18.4` |
| **Lingkungan Java** | JDK 25 |
| **Versi Pustaka** | `1.8.15` |
| **Mod ID** | `dasik-library` |
| **Nama Mod** | Dasik Library |
| **Lisensi** | LGPL-3.0 |

> 📌 **Pemberitahuan Repositori Sumber**: Dokumentasi dalam wiki ini mencerminkan **kondisi kode sumber repositori saat ini**, yang mungkin memuat komit terbaru yang belum dirilis atau fitur dalam pengembangan sebelum rilis publik di CurseForge dan Modrinth.

---

## 🛠️ Gambaran Umum & Instalasi

**Dasik Library** adalah dependensi runtime wajib untuk mod dalam seri *Vanilla Outsider*, *Instant Gratification*, dan *Delayed Gratification*. Pustaka ini menyediakan penjadwalan tick terpadu untuk AI sosial, genetika hewan, kalkulasi vektor kemudi Boids, dan registrasi GameRule dinamis.

### 📥 Pemasangan untuk Pemain
1. Pasang **Fabric Loader** (`0.18.4` atau lebih baru) untuk Minecraft `26.2`.
2. Pastikan **Fabric API** (`0.152.1+26.2` atau lebih baru) berada di dalam folder `.minecraft/mods`.
3. Unduh berkas `dasik-library-1.8.15.jar` dan tempatkan bersama mod terkait (seperti *Better Dogs*, *Natural Reproduction*) di dalam folder `.minecraft/mods`.

### 💻 Dependensi untuk Pengembang Mod

Tambahkan **Dasik Library** ke dalam `fabric.mod.json`:

```json
{
  "schemaVersion": 1,
  "id": "my_consumer_mod",
  "version": "1.0.0+26.2",
  "name": "My Consumer Mod",
  "depends": {
    "fabricloader": ">=0.18.4",
    "minecraft": ">=26.1.2-",
    "dasik-library": "*"
  }
}
```

Dalam `gradle.properties`:

```properties
dasik_library_version=1.8.15
```

Dalam `build.gradle`:

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_library_version}"
}
```

---

## ⚙️ Perubahan Arsitektur Utama di 26.2+

1. **Mojang Sovereign Mappings**: Menggunakan pemetaan resmi Mojang (`level`, `ServerLevel`, `EntityTypes`). Penamaan lama Yarn (`world`, `getWorld`) telah dihapus seluruhnya.
2. **API Identifier**: Menggunakan `Identifier.fromNamespaceAndPath(namespace, path)` atau `Identifier.parse(string)`. Metode lama `Identifier.of()` tidak lagi didukung.
3. **Batas Versi Terbuka (`>=26.1.2-`)**: Memastikan satu berkas JAR tetap kompatibel dengan pembaruan patch Minecraft `26.2+` berikutnya, sementara `ModVersionGuard` menjamin keamanan saat runtime.

---

## 🔗 Halaman Terkait
* [[Kompatibilitas Versi|id_id-Version-Compatibility]]
* [[ModVersionGuard & Keamanan Saat Startup|id_id-ModVersionGuard-and-Startup-Safety]]
* [[Penyiapan Pengembang & Build|id_id-Developer-Setup-and-Building]]
