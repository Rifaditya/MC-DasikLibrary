# GameRules del Cliente y Ayudantes de GUI

| Clase de Ayuda | Entorno | Propósito |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | Cliente / Servidor Integrado | Consulta GameRules del servidor integrado en el hilo del cliente |
| `ConfigHelper` | Común (Cliente/Servidor) | Carga, guardado y sustitución atómica de configuraciones JSON |
| `GuiHelper` | Cliente | Ayudantes de pantallas para ModMenu / Cloth Config |

---

## 🖥️ Seguridad de Carga de Clases en Servidores Dedicados

Para garantizar una compatibilidad del 100% en servidores dedicados sin provocar caídas del sistema, todos los componentes de interfaz y helpers de cliente emplean comprobaciones seguras (`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`).

```ascii
                      DynamicGameRuleManager.getInt(level, ruleKey)
                                        │
                    ┌───────────────────┴───────────────────┐
                    ▼                                       ▼
          [ level instanceof ServerLevel ]       [ Client Environment ]
                    │                                       │
                    ▼                                       ▼
            Direct Level Lookup                   ClientGameRuleHelper
                                            (Queries Integrated Server)
```

---

## 📄 Reemplazo Atómico de Archivos JSON en `ConfigHelper`

`ConfigHelper` garantiza la integridad de los datos de configuración ante apagones repentinos:

1. Escribe los datos en un archivo temporal `config.json.tmp`.
2. Valida la estructura del JSON y el tamaño del archivo.
3. Genera una copia de respaldo automática `config.json.bak`.
4. Ejecuta un intercambio atómico `config.json.tmp` -> `config.json` mediante `Files.move(..., StandardCopyOption.ATOMIC_MOVE)`.

---

## 🔗 Páginas Relacionadas
* [[Gestor de GameRules Dinámicas|es_es-Dynamic-GameRules-Manager]]
* [[Perfiles de Comportamiento y Condiciones|es_es-Behavior-Profiles-and-Conditions]]
