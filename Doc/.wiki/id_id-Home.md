# Portal Utama Wiki - Dasik Library

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

Selamat datang di dokumentasi teknis resmi **Dasik Library**, infrastruktur bersama untuk AI sosial kecerdasan kelompok (Hive Mind), mesin genetika hewan, kalkulasi vektor kawanan Boids, dan GameRule dinamis untuk mod Fabric Minecraft.

> 📌 **Pemberitahuan Repositori Sumber**: Dokumentasi dalam wiki ini mencerminkan **kondisi kode sumber repositori saat ini**, yang mungkin memuat komit terbaru yang belum dirilis atau fitur dalam pengembangan sebelum rilis publik di CurseForge dan Modrinth.

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 Filosofi Arsitektur

Dasik Library dibangun di atas dua prinsip utama:

1. **"Thin Mod, Fat Library" (Mod Ramping, Pustaka Gemuk)**: Mod konsumen modular berfokus murni pada konten entitas dan registrasi. Kalkulasi matematika rumit, penjadwalan tick, pewarisan genetika, kalkulasi vektor kemudi Boids, dan serialisasi NBT dipusatkan di Dasik Library.
2. **"One Brain, Many Minds" (Satu Otak, Banyak Pikiran)**: Eksekusi denyut nadi terpusat melalui `GlobalSocialSystem` menegakkan **Aturan Highlander** (tepat 1 siklus tick global per tick game) untuk mengorkestrasi ribuan entitas aktif dengan beban server minimal.

---

## 📦 Direktori Versi Minecraft

* [[Panduan MC 26.2|id_id-Minecraft-26.2-Guide]] — Instalasi dan konfigurasi untuk Minecraft 26.2+.
* [[Kompatibilitas Versi|id_id-Version-Compatibility]] — Matriks siklus hidup multi-versi (`>=26.1.2-` hingga `26.2+`), keamanan Knot ClassLoader, dan aturan perlindungan versi.

---

## 🎮 Sistem Utama & Matriks Mekanisme

Jelajahi seluruh mekanisme teknis dan panduan konfigurasi:

* [[Sistem Sosial Hive Mind|id_id-Hive-Mind-Social-System]] — Mesin denyut nadi tunggal, Aturan Highlander, `SocialRegistry` ter-shard $O(1)$, dan anggaran tick.
* [[Penjadwal Sosial & Event|id_id-Social-Scheduler-and-Events]] — `EntitySocialScheduler`, eksekusi jalur ganda Mood/Ambient, `PriorityTier`, dan `SocialEventRegistry`.
* [[Mesin Genetika Hewan|id_id-Animal-Genetics-Engine]] — Attachment `EntityGenetics`, pengodean Long DNA, penskalaan ukuran (`0.1x` - `3.0x`), indikator kerdil (Runt), dan matematika pewarisan.
* [[API Genetika & Silsilah|id_id-Genetics-API-and-Pedigree]] — Fasad `DasikAnimalGeneticsAPI`, analisis kekerabatan dan silsilah, prediksi risiko inbreeding, dan pengubah sifat dinamis.
* [[Modifikasi Loot Genetika|id_id-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`, `GeneticsLootRegistry`, dan intersepsi drop via `LivingEntityLootMixin`.
* [[Pengikut Pemimpin & Flocking Boids|id_id-Leader-Follower-and-Flocking]] — `GroupMember`, `FollowLeaderGoal`, strategi Boids udara dan darat, serta pembobotan `GroupParameters`.
* [[Pengelola GameRules Dinamis|id_id-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`, registrasi dinamis, injeksi terjemahan otomatis, tajuk kategori tebal (`§l`), dan pembantu matematika.
* [[Codec GameRule & Serialisasi|id_id-GameRule-Codec-and-Serialization]] — Validasi batas GameRule integer (`Integer.MIN_VALUE`), pencegahan crash `SavedDataStorage.encodeUnchecked`.
* [[GameRule Klien & Pembantu GUI|id_id-Client-GameRule-and-GUI-Helpers]] — Kueri `ClientGameRuleHelper`, `GuiHelper`, pertukaran berkas atomik JSON di `ConfigHelper`.
* [[Enchantment Dinamis & Pelacak Penglihatan|id_id-Dynamic-Enchantments-and-Vision]] — Injeksi runtime via `DynamicEnchantmentManager` dan uji kerucut pandang dengan `PlayerVisionTracker`.
* [[Utilitas Stokastik & Matematika|id_id-Stochastic-and-Math-Utilities]] — Algoritma XORSHIFT pada `FastRandom`, pengambilan sampel permil/desil pada `StochasticUtil`, dan konversi tick ke detik ($20\text{ ticks} = 1\text{s}$).
* [[Pembersihan Atribut Kedaluwarsa & Skala Ukuran|id_id-Stale-Attribute-Purging-and-Scale]] — Aturan pembersihan modifier atribut, matematika offset basis `-1.0f` untuk `ADD_VALUE`, dan keamanan awalan `genetics_`.
* [[ModVersionGuard & Keamanan Saat Startup|id_id-ModVersionGuard-and-Startup-Safety]] — Verifikasi Knot ClassLoader (`Thread.currentThread().getContextClassLoader()`) dan pencegahan crash ketidakcocokan versi.

---

## 💻 Referensi Teknis & Pengembang

* [[Penyiapan Pengembang & Build|id_id-Developer-Setup-and-Building]] — Prasyarat JDK 25, Gradle 9.3+, `./gradlew build --no-daemon`, dan `./gradlew test`.
* [[Arsitektur & Tata Letak Paket|id_id-Architecture-and-Package-Layout]] — Struktur pohon paket ASCII lengkap (`ai`, `api`, `config`, `core`, `mixin`, `util`) dan model konkurensi.
* [[Referensi Mixin & Kait Injeksi|id_id-Mixin-Reference-and-Hooks]] — Tabel rincian injeksi Mixin (`LanguageMixin`, `LivingEntityLootMixin`, `MobGoalAccessor`, `PathfinderMobMixin`, `ProfileTriggerMixin`).
* [[Profil Perilaku & Kondisi Pemicu|id_id-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`, `BehaviorProfile`, `BehaviorCondition`, `ProfileAware`, dan `DefaultProfileBuilder`.
* [[Panduan Integrasi Mod Konsumen|id_id-Consumer-Mods-Integration-Guide]] — Panduan implementasi dan contoh kode untuk mod turunan (*Better Dogs*, *Natural Reproduction*, *Collapsible Game Rule Screen*, *Bat Ecology*, *Ore Amplifier*).

---

## 🔗 Tautan Eksternal

* [[Repositori GitHub|Home]]
* [Halaman Proyek Modrinth](https://modrinth.com/mod/dasik-library)
* [Halaman Proyek CurseForge](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
