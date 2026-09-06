# 遺伝学 API と血統ツリーシステム

| ファサードクラス | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **サポート対象属性** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **血統探索深度** | 最大 3 世代（親、祖父母） |
| **近親交配リスク判定** | `0%`（無関係）〜 `100%`（同腹の兄弟・または同一個体） |

---

## 🛠️ `DasikAnimalGeneticsAPI` ファサード

`DasikAnimalGeneticsAPI` は、下流 Mod が低レベルの NBT やコンポーネントを直接操作することなく、安全かつ直感的に遺伝情報を操作できるようにする静的ヘルパー群を提供します。

### API メソッド一覧

```java
public final class DasikAnimalGeneticsAPI {
    // Size & Scale
    public static float getScale(LivingEntity entity);
    public static void setScale(LivingEntity entity, float scale);
    public static boolean isRunt(LivingEntity entity);
    public static boolean isGiant(LivingEntity entity);

    // Kinship & Pedigree
    public static boolean isRelated(LivingEntity entity1, LivingEntity entity2);
    public static int predictInbreedingRiskPercent(LivingEntity parent1, LivingEntity parent2);

    // Trait Modifiers
    public static void setTrait(LivingEntity entity, String traitKey, float value);
    public static void modifyTrait(LivingEntity entity, String traitKey, float delta);
    public static void resetGenetics(LivingEntity entity);
}
```

---

## 🌳 家系図と近親交配リスク予測

血統アナライザーは、先祖の UUID を再帰的に比較して重複度を算出します：

$$\text{Inbreeding Risk (\%)} = \begin{cases}
100\%, & \text{if Parent1 UUID} = \text{Parent2 UUID} \\
50\%, & \text{if Full Siblings (same Parent1 AND Parent2)} \\
25\%, & \text{if Half Siblings (sharing 1 Parent)} \\
12.5\%, & \text{if Cousin overlap (sharing Grandparents)} \\
0\%, & \text{if No pedigree overlap detected}
\end{cases}$$

```ascii
[ Parent 1 ]             [ Parent 2 ]
     │                        │
     ├───────────┬────────────┤
     ▼           ▼            ▼
[ Offspring1 ] [ Offspring2 ] 
     │               │
     └─── BREEDING ──┘
            │
            ▼
    Inbreeding Risk = 50% (Full Siblings)
```

---

## 💻 開発者向けコード例

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 関連ページ
* [[動物遺伝学エンジン|ja_jp-Animal-Genetics-Engine]]
* [[遺伝学ドロップ修飾子|ja_jp-Genetics-Loot-Modifiers]]
