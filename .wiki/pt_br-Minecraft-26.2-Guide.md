# Guia do Minecraft 26.2+

| Parâmetro | Especificação |
| :--- | :--- |
| **Versão Alvo do Minecraft** | `26.2` (e compatível com `26.x`) |
| **Restrição do Fabric Loader** | `>=0.18.4` |
| **Ambiente Java** | JDK 25 |
| **Versão da Biblioteca** | `1.8.15` |
| **ID do Mod** | `dasik-library` |
| **Nome do Mod** | Dasik Library |
| **Licença** | LGPL-3.0 |

> 📌 **Aviso sobre o Código Fonte do Repositório**: A documentação nesta Wiki reflete o **estado atual do código fonte no repositório**, que pode incluir commits recentes não lançados ou recursos em desenvolvimento antes das versões públicas no CurseForge e Modrinth.

---

## 🛠️ Visão Geral e Instalação

A **Dasik Library** é uma dependência de execução essencial para mods das coleções *Vanilla Outsider*, *Instant Gratification* e *Delayed Gratification*. Ela fornece agendamento centralizado de IA social, genética animal, matemática vetorial Boids e registro dinâmico de GameRules.

### 📥 Instalação para Jogadores
1. Instale o **Fabric Loader** (`0.18.4` ou superior) para Minecraft `26.2`.
2. Certifique-se de que a **Fabric API** (`0.152.1+26.2` ou superior) esteja na pasta `.minecraft/mods`.
3. Baixe `dasik-library-1.8.15.jar` e coloque na pasta `.minecraft/mods` junto aos mods dependentes (ex: *Better Dogs*, *Natural Reproduction*).

### 💻 Dependência para Desenvolvedores de Mods

Adicione a **Dasik Library** ao seu `fabric.mod.json`:

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

No `gradle.properties`:

```properties
dasik_library_version=1.8.15
```

No `build.gradle`:

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_library_version}"
}
```

---

## ⚙️ Principais Mudanças Arquiteturais em 26.2+

1. **Mojang Sovereign Mappings**: Utiliza mapeamentos oficiais da Mojang (`level`, `ServerLevel`, `EntityTypes`). Métodos legados do Yarn (`world`, `getWorld`) estão obsoletos.
2. **Identifier API**: Usa `Identifier.fromNamespaceAndPath(namespace, path)` ou `Identifier.parse(string)`. O método legado `Identifier.of()` não é suportado.
3. **Limites de Versão Abertos (`>=26.1.2-`)**: Garante que um único JAR continue compatível com atualizações menores de Minecraft `26.2+`, usando `ModVersionGuard` para verificação segura em tempo de execução.

---

## 🔗 Páginas Relacionadas
* [[Compatibilidade de Versões|pt_br-Version-Compatibility]]
* [[ModVersionGuard e Segurança de Inicialização|pt_br-ModVersionGuard-and-Startup-Safety]]
* [[Configuração de Desenvolvedor e Compilação|pt_br-Developer-Setup-and-Building]]
