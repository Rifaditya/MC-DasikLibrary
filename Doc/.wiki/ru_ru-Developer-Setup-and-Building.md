# Настройка среды разработчика и сборка

| Инструмент | Рекомендуемая версия |
| :--- | :--- |
| **JDK** | Java 25 (`E:/JDK25`) |
| **Gradle** | 9.3+ |
| **Fabric Loom** | `1.15.2` |
| **Minecraft Mappings** | Mojang Sovereign |

---

## 🛠️ Требования к среде и сборка

### 1. Клонирование репозитория
```bash
git clone https://github.com/Rifaditya/DasikLibrary-Rebuilt.git
cd DasikLibrary-Rebuilt
```

### 2. Настройка пути к JDK
В файле `gradle.properties`:
```properties
org.gradle.java.home=E:/JDK25
```

### 3. Сборка и публикация в локальный Maven
Сборка релизного JAR:
```bash
./gradlew build --no-daemon
```

Публикация в локальный кэш Maven (`~/.m2/repository`):
```bash
./gradlew publishToMavenLocal
```

---

## 🧪 Безголовое автоматизированное тестирование

Запуск тестовых наборов GameTest и JUnit:
```bash
./gradlew test
```

---

## 🔗 Связанные страницы
* [[Архитектура и структура пакетов|ru_ru-Architecture-and-Package-Layout]]
* [[Руководство по интеграции модов-потребителей|ru_ru-Consumer-Mods-Integration-Guide]]
