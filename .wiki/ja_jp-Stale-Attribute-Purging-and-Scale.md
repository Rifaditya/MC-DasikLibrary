# 古い属性のパージとスケール補正

| 課題項目 | 対策方針 |
| :--- | :--- |
| **残留属性モディファイア** | 再計算時に `genetics_` 接頭辞を持つ修飾子を全削除 |
| **サイズ加算オフセット** | `ADD_VALUE` 演算時の `-1.0f` ベース補正 |
| **対象スケール属性** | `EntityAttributes.SCALE` |

---

## 🧹 古い属性モディファイアの自動パージ

モブの成長、突然変異、治療などによってステータスが再計算される際、以前に適用されたモディファイアを削除しなければ数値が重複して異常に増加してしまいます：

```java
public static void purgeStaleModifiers(LivingEntity entity, Holder<Attribute> attribute) {
    AttributeInstance instance = entity.getAttribute(attribute);
    if (instance != null) {
        instance.getModifiers().stream()
            .filter(m -> m.id().getPath().startsWith("genetics_"))
            .toList()
            .forEach(instance::removeModifier);
    }
}
```

---

## 📏 `ADD_VALUE` 演算におけるベースオフセット

Minecraft において、属性 `SCALE` の初期値は $1.0$ です。そのため、修飾子で `ADD_VALUE` を指定する場合は基準値 $1.0$ を差し引く必要があります：

$$\text{Modifier Amount} = \text{TargetScale} - 1.0f$$

例えば、スケールを $1.5x$ に設定したい場合、モディファイアの加算量は $+0.5f$ となります。

---

## 🔗 関連ページ
* [[動物遺伝学エンジン|ja_jp-Animal-Genetics-Engine]]
* [[遺伝学 API と血統ツリー|ja_jp-Genetics-API-and-Pedigree]]
