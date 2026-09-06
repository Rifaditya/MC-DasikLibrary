# 過期屬性清理與體型縮放

| 修復項 | 問題描述 | 修復版本 |
| :--- | :--- | :--- |
| **巨型實體 Bug** | 過期屬性修飾符累加堆疊 | `1.8.11` |
| **縮放基準偏移** | `ADD_VALUE` 屬性修飾符基準偏移數學計算 | `1.8.10` |

---

## 📐 縮放屬性偏移數學

在 Minecraft 26.2 中，實體體型縮放依賴原生的 `minecraft:scale`。當為縮放應用 `ADD_VALUE` 屬性修飾符時，修飾符數值 $M_{\text{scale}}$ 必須從目標縮放 $S$ 中減去 $1.0\text{f}$：

$$M_{\text{scale}} = S - 1.0\text{f}$$

$$\text{Final Scale} = \text{Base Scale } (1.0) + M_{\text{scale}} = 1.0 + (S - 1.0) = S$$

如果未減去 $1.0\text{f}$，將目標縮放設定為 $2.0$ 將會在基準 $1.0$ 上額外加上 $+2.0$，導致非預期的 $3.0\text{x}$（300%）巨型實體渲染。

---

## 🧹 `GeneticsEngine` 中的過期修飾符清理

為防止世界重新加載或繁殖更新時重複的屬性修飾符疊加，`GeneticsEngine` 在應用新修飾符前會清理舊的 `genetics_` 修飾符：

```java
public static void applyGeneticsModifiers(LivingEntity entity) {
    AttributeInstance scaleInstance = entity.getAttribute(Attributes.SCALE);
    if (scaleInstance != null) {
        // Purge legacy/duplicate modifiers by identifier
        scaleInstance.getModifiers().stream()
            .filter(mod -> mod.id().getPath().startsWith("genetics_"))
            .toList()
            .forEach(scaleInstance::removeModifier);
    }
}
```

---

## 🔗 相關頁面
* [[動物遺傳學引擎|zh_tw-Animal-Genetics-Engine]]
* [[遺傳學 API 與譜系|zh_tw-Genetics-API-and-Pedigree]]
