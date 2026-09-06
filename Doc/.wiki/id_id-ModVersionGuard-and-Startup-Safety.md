# ModVersionGuard & Keamanan Saat Startup

| Komponen | Kelas |
| :--- | :--- |
| **Penjaga Versi** | `net.dasik.social.core.ModVersionGuard` |
| **ClassLoader yang Digunakan** | `Thread.currentThread().getContextClassLoader()` |
| **Fungsi Pencegahan** | Menangkap ketidakcocokan kelas sebelum dunia game dimuat |

---

## 🛡️ Mekanisme Kerja `ModVersionGuard`

Di bawah lingkungan Knot Fabric Loader, pemanggilan `Class.forName(name)` tanpa ClassLoader eksplisit dapat gagal mendeteksi ketidakcocokan API. `ModVersionGuard` secara eksplisit menggunakan ClassLoader dari thread konteks saat ini untuk memverifikasi dependensi penting sejak tahap inisialisasi awal.

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

## 🔗 Halaman Terkait
* [[Kompatibilitas Versi|id_id-Version-Compatibility]]
* [[Panduan MC 26.2|id_id-Minecraft-26.2-Guide]]
