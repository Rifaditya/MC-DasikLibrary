# Utilitas Stokastik & Matematika

| Utilitas | Kelas | Peran |
| :--- | :--- | :--- |
| **Pengacak Angka Cepat** | `net.dasik.social.util.FastRandom` | Generator acak non-blocking 64-bit XORSHIFT |
| **Pengambilan Sampel Probabilitas**| `net.dasik.social.util.StochasticUtil` | Desil (`1/10`), Persen (`1/100`), Permil (`1/1000`) |
| **Konstanta Waktu** | $20\text{ ticks} = 1\text{ detik}$ | Faktor konversi tick Minecraft |

---

## 🎲 Algoritma XORSHIFT pada `FastRandom`

`FastRandom` mengungguli `java.util.Random` dengan meniadakan sinkronisasi atomik sembari memberikan sebaran statistik yang merata:

$$x \leftarrow x \oplus (x \ll 13); \quad x \leftarrow x \oplus (x \gg 7); \quad x \leftarrow x \oplus (x \ll 17)$$

---

## 📊 Pengambilan Sampel Presisi via `StochasticUtil`

`StochasticUtil` mencegah pembulatan integer yang tidak akurat dalam penentuan peluang:

```java
// Roll probability out of 1000 (e.g. 5 permille = 0.5% chance)
if (StochasticUtil.rollPermille(random, 5)) {
    // Rare mutation triggered
}

// Roll percentage chance (0-100)
if (StochasticUtil.rollPercent(random, 25)) {
    // 25% chance branch
}
```

---

## 🔗 Halaman Terkait
* [[Mesin Genetika Hewan|id_id-Animal-Genetics-Engine]]
* [[Sistem Sosial Hive Mind|id_id-Hive-Mind-Social-System]]
