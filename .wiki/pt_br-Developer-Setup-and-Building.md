# Configuração de Desenvolvedor e Compilação

| Ferramenta | Versão Recomendada |
| :--- | :--- |
| **JDK** | Java 25 (`E:/JDK25`) |
| **Gradle** | 9.3+ |
| **Fabric Loom** | `1.15.2` |
| **Mapeamentos de Minecraft** | Mojang Sovereign |

---

## 🛠️ Requisitos de Ambiente e Compilação

### 1. Clonar o Repositório
```bash
git clone https://github.com/Rifaditya/DasikLibrary-Rebuilt.git
cd DasikLibrary-Rebuilt
```

### 2. Configurar o Caminho do JDK
No seu `gradle.properties`:
```properties
org.gradle.java.home=E:/JDK25
```

### 3. Compilar e Publicar no Maven Local
Gerar o arquivo JAR final:
```bash
./gradlew build --no-daemon
```

Publicar no repositório Maven local (`~/.m2/repository`):
```bash
./gradlew publishToMavenLocal
```

---

## 🧪 Testes Automatizados Headless

Executar testes automatizados do GameTest e JUnit:
```bash
./gradlew test
```

---

## 🔗 Páginas Relacionadas
* [[Arquitetura e Estrutura de Pacotes|pt_br-Architecture-and-Package-Layout]]
* [[Guia de Integração para Mods Consumidores|pt_br-Consumer-Mods-Integration-Guide]]
