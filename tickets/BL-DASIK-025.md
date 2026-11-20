# 🎫 [BL-DASIK-025] Fix DynamicGameRuleManager Duplicate Method ClassFormatError via Idempotent Fabric Bridge Injection

- **Ticket ID**: `[BL-DASIK-025]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Scope**: `Anchor 1.20.1` (`Dasik Library v1.20.1` / `FabricBridgeInjector`)
- **Status**: `✅ RESOLVED`
- **Target Repository / Root**: `MC-DasikLibrary` / `Dasik Library v1.20.1`
- **Grilling Status**: `✅ PASSED`

---

## 🎯 1. Problem Statement & Player Impact
When launching Minecraft with Fabric 1.20.1, KnotClassLoader fails during bootstrap with:
```
net.fabricmc.loader.impl.FormattedException: java.lang.ClassFormatError: Duplicate method name "registerCategory" with signature "(Lnet.minecraft.class_2960;Lnet.minecraft.class_2561;)Lnet/fabricmc/fabric/api/gamerule/v1/CustomGameRuleCategory;" in class file net/dasik/social/api/gamerule/DynamicGameRuleManager
```
This fatal crash occurs because `FabricBridgeInjector.java` was invoked non-idempotently. During `remapJar`, Gradle executes `inject_fabric_bridge.py` via `remapJar.doLast`. Any subsequent invocation or rebuild appended duplicate bridge methods (`registerCategory`, `booleanRule`, `integerRule`) into `DynamicGameRuleManager.class`, violating the JVM class format specification.

---

## 🔬 2. Architectural Blueprint & Target Classes
1. **Injector Idempotency Guard**:
   - Class: `E:\Minecraft Project\.agents\scripts\FabricBridgeInjector.java`
   - Target: `injectBridges(byte[] originalBytes)`
   - Mechanism: Track existing method signatures via `visitMethod()`. In `visitEnd()`, verify absence before injecting `registerCategory`, `booleanRule`, and `integerRule`. If already present, log and skip injection.
2. **Clean Rebuild & Bytecode Audit**:
   - Clean `Dasik Library v1.20.1` (`./gradlew clean`).
   - Bump `mod_version=1.4.5+1.20.1` in `gradle.properties`.
   - Run `./gradlew test build --no-daemon --console=plain`.
   - Audit `dasik-library-1.4.5+1.20.1-fabric.jar` with `javap` to verify exactly 1 set of bridge methods.
3. **Deployment**:
   - Deploy `dasik-library-1.4.5+1.20.1-fabric.jar` to Modrinth profile and purge `1.4.4`.
   - Push to `minecraft-mod-release-hub` archive.

---

## 🛡️ 3. Anti-Goals (Creep Shield)
1. **Zero Double-Injection**: The bytecode injector must never insert duplicate method signatures under any circumstances, even across multiple runs.
2. **Zero Inversion of Automated Hooks**: Do not disable `remapJar.doLast` in `fabric/build.gradle`; ensure the pipeline is self-healing and idempotent.
3. **Zero Anchor Drift**: Strictly isolate this release to `1.4.5+1.20.1`.

---

## 🚀 4. Blast Radius Matrix
| Subsystem / Anchor | Risk Level | Mitigation |
| :--- | :---: | :--- |
| `Fabric 1.20.1 ClassLoader` | CRITICAL | JVM verifies class structure on boot; javap pre-audit guarantees 0 duplicates. |
| `Older Consumer Mods (Agrarian Reform)` | HIGH | ABI bridge methods remain 100% present for backwards compatibility. |
| `Dasik Library NeoForge 1.20.1` | LOW | NeoForge JAR does not use Fabric bridges; zero impact. |

---

## 📋 5. Implementation Steps
1. Make `FabricBridgeInjector.java` idempotent by tracking existing methods in `ClassVisitor`.
2. Clean and bump `Dasik Library 1.20.1` to `1.4.5+1.20.1`.
3. Execute `./gradlew test build` and verify bytecode with `javap`.
4. Archive and deploy `1.4.5+1.20.1` to Modrinth profile, purging `1.4.4`.
5. Update `CHANGELOG.md` and `RELEASE_QUEUE.md`.
