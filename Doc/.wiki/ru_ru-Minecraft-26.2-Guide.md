# Руководство по Minecraft 26.2+

| Параметр | Спецификация |
| :--- | :--- |
| **Целевая версия Minecraft** | `26.2` (и обратно совместимые `26.x`) |
| **Ограничение Fabric Loader** | `>=0.18.4` |
| **Среда Java** | JDK 25 |
| **Версия библиотеки** | `1.8.15` |
| **Идентификатор мода (Mod ID)**| `dasik-library` |
| **Название мода** | Dasik Library |
| **Лицензия** | LGPL-3.0 |

> 📌 **Предупреждение об исходном коде репозитория**: Документация в этой Wiki отражает **текущее состояние исходного кода в репозитории**, которое может содержать недавние невыпущенные коммиты или разрабатываемые функции, опережающие общедоступные релизные сборки на CurseForge и Modrinth.

---

## 🛠️ Обзор и установка

**Dasik Library** является обязательной зависимостью времени выполнения для модов серий *Vanilla Outsider*, *Instant Gratification* и *Delayed Gratification*. Библиотека обеспечивает централизованное планирование тиков социального ИИ, генетику животных, векторную математику Boids и динамическую регистрацию GameRules.

### 📥 Установка для игроков
1. Установите **Fabric Loader** (`0.18.4` или новее) для Minecraft `26.2`.
2. Убедитесь, что **Fabric API** (`0.152.1+26.2` или новее) установлен в папку `.minecraft/mods`.
3. Загрузите файл `dasik-library-1.8.15.jar` и поместите его в папку `.minecraft/mods` вместе с модами-потребителями (например, *Better Dogs*, *Natural Reproduction*).

### 💻 Зависимость для разработчиков модов

Добавьте **Dasik Library** в ваш `fabric.mod.json`:

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

В `gradle.properties`:

```properties
dasik_library_version=1.8.15
```

В `build.gradle`:

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_library_version}"
}
```

---

## ⚙️ Основные изменения архитектуры 26.2+

1. **Mojang Sovereign Mappings**: Используются маппинги Mojang (`level`, `ServerLevel`, `EntityTypes`). Устаревшие методы Yarn (`world`, `getWorld`) более не поддерживаются.
2. **Identifier API**: Используется `Identifier.fromNamespaceAndPath(namespace, path)` или `Identifier.parse(string)` для ресурсов. Устаревший метод `Identifier.of()` не поддерживается.
3. **Открытые границы версий (`>=26.1.2-`)**: Единый JAR-файл остается совместимым со следующими патчами `26.2+`, используя `ModVersionGuard` для проверки безопасности во время выполнения.

---

## 🔗 Связанные страницы
* [[Совместимость версий|ru_ru-Version-Compatibility]]
* [[ModVersionGuard и безопасность при запуске|ru_ru-ModVersionGuard-and-Startup-Safety]]
* [[Настройка среды разработчика и сборка|ru_ru-Developer-Setup-and-Building]]
