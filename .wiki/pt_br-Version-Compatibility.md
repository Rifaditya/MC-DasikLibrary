# Matriz de Compatibilidade de Versões

| Versão Alvo do Minecraft | Versão da Biblioteca | Mod Version Guard | Especificação de Dependência | Estado de Suporte |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **Linha Principal Ativa** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | Backport / Estável |
| **Minecraft 1.21.x** | *Obsoleto* | *N/A* | *Legacy 1.x* | **Fim da Vida Útil (EOL)** |

> 📌 **Aviso sobre o Código Fonte do Repositório**: A documentação nesta Wiki reflete o **estado atual do código fonte no repositório**, que pode incluir commits recentes não lançados ou recursos em desenvolvimento antes das versões públicas no CurseForge e Modrinth.

---

## 🛡️ Política 1 Jar 1 Version e Compatibilidade Progressiva

A Dasik Library implementa a **Política 1 Jar 1 Version** combinada com **compatibilidade progressiva aberta**:

1. **Limites Abertos de Versão (`"minecraft": ">=26.1.2-"`)**: A biblioteca define um limite inferior aberto no `fabric.mod.json`, permitindo que o Fabric Loader carregue o JAR em patches secundários sem travar o jogo.
2. **Segurança no Knot ClassLoader (`ModVersionGuard`)**: Durante `onInitialize()`, o método `ModVersionGuard.checkClass` valida classes através de `Thread.currentThread().getContextClassLoader()` para detectar incompatibilidades de API e exibir mensagens claras em vez de falhas silenciosas na JVM.

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

## 🚫 Preservação da Identidade de Versão (Sem Fusão com Versões Antigas)

Sob diretrizes de design rigorosas (`[DIR-20260614-001]`), **as versões anuais do Minecraft 26.x NUNCA devem ser confundidas com as versões legadas 1.21.x de 2024**:
* ❌ `26.2 (1.21.4)` — Estritamente Proibido.
* ✅ `Minecraft 26.2` — Notação soberana anual padrão.

---

## 🔗 Páginas Relacionadas
* [[Guia MC 26.2|pt_br-Minecraft-26.2-Guide]]
* [[ModVersionGuard e Segurança de Inicialização|pt_br-ModVersionGuard-and-Startup-Safety]]
