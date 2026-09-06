# Configuración de Desarrollador y Compilación

| Herramienta | Versión Recomendada |
| :--- | :--- |
| **JDK** | Java 25 (`E:/JDK25`) |
| **Gradle** | 9.3+ |
| **Fabric Loom** | `1.15.2` |
| **Mapeos de Minecraft** | Mojang Sovereign |

---

## 🛠️ Requisitos de Entorno y Compilación

### 1. Clonar el Repositorio
```bash
git clone https://github.com/Rifaditya/DasikLibrary-Rebuilt.git
cd DasikLibrary-Rebuilt
```

### 2. Configurar la Ruta de JDK
En su archivo `gradle.properties`:
```properties
org.gradle.java.home=E:/JDK25
```

### 3. Compilación y Publicación en Maven Local
Construir el archivo JAR de lanzamiento:
```bash
./gradlew build --no-daemon
```

Publicar en la caché local de Maven (`~/.m2/repository`):
```bash
./gradlew publishToMavenLocal
```

---

## 🧪 Pruebas Automatizadas sin Interfaz

Ejecute las suites de pruebas automatizadas GameTest y JUnit:
```bash
./gradlew test
```

---

## 🔗 Páginas Relacionadas
* [[Arquitectura y Distribución de Paquetes|es_es-Architecture-and-Package-Layout]]
* [[Guía de Integración para Mods Consumidores|es_es-Consumer-Mods-Integration-Guide]]
