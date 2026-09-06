# Matriks Kompatibilitas Versi

| Versi Target Minecraft | Versi Pustaka | Mod Version Guard | Spesifikasi Dependensi | Status Dukungan |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **Cabang Utama Aktif** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | Portingan / Stabil |
| **Minecraft 1.21.x** | *Usang* | *N/A* | *Legacy 1.x* | **Akhir Masa Pakai (EOL)** |

> 📌 **Pemberitahuan Repositori Sumber**: Dokumentasi dalam wiki ini mencerminkan **kondisi kode sumber repositori saat ini**, yang mungkin memuat komit terbaru yang belum dirilis atau fitur dalam pengembangan sebelum rilis publik di CurseForge dan Modrinth.

---

## 🛡️ Kebijakan 1 Jar 1 Versi & Kompatibilitas ke Depan

Dasik Library menerapkan kebijakan **1 Jar 1 Versi** yang dipadukan dengan **kompatibilitas ke depan terbuka**:

1. **Batas Terbuka (`"minecraft": ">=26.1.2-"`)**: Pustaka menetapkan batas bawah terbuka pada `fabric.mod.json`, memungkinkan Fabric Loader memuat JAR pada pembaruan minor tanpa memblokir pemain.
2. **Keamanan Knot ClassLoader (`ModVersionGuard`)**: Selama `onInitialize()`, `ModVersionGuard.checkClass` memverifikasi keberadaan kelas penting melalui `Thread.currentThread().getContextClassLoader()`, mencegah crash senyap JVM dengan memberikan pesan diagnostik yang jelas.

```java
public final class ModVersionGuard {
    public static void checkClass(String modName, String requiredClassName) {
        try {
            Class.forName(requiredClassName, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("\n" +
                "=====================================================================\n" +
                " [" + modName + "] Minecraft API Mismatch!\n" +
                " A required Minecraft class or API was not found in your game version.\n" +
                " Try updating your Minecraft version or download a matching build.\n" +
                "=====================================================================");
        }
    }
}
```

---

## 🚫 Larangan Pencampuran Notasi Versi

Sesuai pedoman ketat arsitektur `[DIR-20260614-001]`, **rilis tahunan Minecraft 26.x TIDAK BOLEH dicampuradukkan dengan notasi versi lawas 1.21.x tahun 2024**:
* ❌ `26.2 (1.21.4)` — Sangat Dilarang.
* ✅ `Minecraft 26.2` — Notasi rilis tahunan resmi.

---

## 🔗 Halaman Terkait
* [[Panduan MC 26.2|id_id-Minecraft-26.2-Guide]]
* [[ModVersionGuard & Keamanan Saat Startup|id_id-ModVersionGuard-and-Startup-Safety]]
