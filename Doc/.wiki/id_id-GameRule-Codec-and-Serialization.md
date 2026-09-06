# Codec GameRule & Serialisasi

| Topik Masalah | Spesifikasi |
| :--- | :--- |
| **Masalah Serialisasi** | Crash pada `SavedDataStorage.encodeUnchecked` saat menyimpan dunia |
| **Diperbaiki pada Rilis**| `1.8.15` |
| **Batas Nilai Integer** | `min = Integer.MIN_VALUE` (atau `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ Perbaikan Codec GameRule Integer

Di Minecraft 26.2, GameRule dinamis diserialisasi ke dalam `level.dat` / `game_rules.dat` melalui `SavedDataStorage.encodeUnchecked`. Pada versi terdahulu, registrasi aturan integer tanpa menentukan batas minimum eksplisit menyebabkan Codec memberlakukan `min = 0`, sehingga memicu `IllegalStateException` saat menyimpan nilai negatif atau nilai default.

### Implementasi pada `DynamicGameRuleManager`

```java
// IntegerBuilder initialization fix in DynamicGameRuleManager
public class IntegerBuilder {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public GameRule<Integer> register() {
        int effectiveMin = Math.min(min, defaultValue);
        int effectiveMax = Math.max(max, defaultValue);
        // Uses Codec.INT.intRange(effectiveMin, effectiveMax) to prevent encodeUnchecked bounds check failure
        GameRule<Integer> rule = new GameRule<>(
            GameRuleType.INT, defaultValue, Codec.INT.intRange(effectiveMin, effectiveMax), 
            FeatureFlagSet.of(), category, visitor
        );
        return Registry.register(BuiltInRegistries.GAME_RULE, Identifier.parse(ruleName), rule);
    }
}
```

---

## 🔒 Integritas Namespace

Selalu gunakan format `modid:rule_name` (`[ERR-20260510-002]`). GameRule tanpa namespace tidak dapat diserialisasi dengan aman dan berisiko tereset saat server dimuat ulang.

---

## 🔗 Halaman Terkait
* [[Pengelola GameRules Dinamis|id_id-Dynamic-GameRules-Manager]]
* [[GameRule Klien & Pembantu GUI|id_id-Client-GameRule-and-GUI-Helpers]]
