# Матрица совместимости версий

| Целевая версия MC | Версия библиотеки | Mod Version Guard | Спецификация зависимостей | Статус поддержки |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **Активная основная ветка** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | Бэкпорт / Стабильный |
| **Minecraft 1.21.x** | *Устарело* | *N/A* | *Legacy 1.x* | **Конец жизненного цикла (EOL)** |

> 📌 **Предупреждение об исходном коде репозитория**: Документация в этой Wiki отражает **текущее состояние исходного кода в репозитории**, которое может содержать недавние невыпущенные коммиты или разрабатываемые функции, опережающие общедоступные релизные сборки на CurseForge и Modrinth.

---

## 🛡️ Политика 1 Jar 1 Version и перспективной совместимости

Dasik Library реализует **Политику 1 Jar 1 Version** в сочетании с **открытой перспективной совместимостью**:

1. **Открытые границы версий (`"minecraft": ">=26.1.2-"`)**: Библиотека объявляет открытую нижнюю границу в `fabric.mod.json`, позволяя загрузчику Fabric Loader принимать JAR на минорных патчах без блокировки пользователей.
2. **Безопасность Knot ClassLoader (`ModVersionGuard`)**: Во время `onInitialize()`, метод `ModVersionGuard.checkClass` проверяет наличие классов с использованием `Thread.currentThread().getContextClassLoader()`, чтобы заранее обнаружить несовместимость API и отобразить понятные баннеры вместо тихого сбоя JVM.

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

## 🚫 Защита идентичности версий (Запрет объединения версий)

В соответствии с правилами проектирования (`[DIR-20260614-001]`), **версии ежегодных обновлений Minecraft 26.x НИКОГДА не должны смешиваться с устаревшими версиями 2024 года 1.21.x**:
* ❌ `26.2 (1.21.4)` — Строго запрещено.
* ✅ `Minecraft 26.2` — Стандартное суверенное обозначение ежегодного релиза.

---

## 🔗 Связанные страницы
* [[Руководство по MC 26.2|ru_ru-Minecraft-26.2-Guide]]
* [[ModVersionGuard и безопасность при запуске|ru_ru-ModVersionGuard-and-Startup-Safety]]
