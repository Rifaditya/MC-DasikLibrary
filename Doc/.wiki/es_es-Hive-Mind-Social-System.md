# Sistema Social de Mente Colmena

| Parámetro del Sistema | Valor |
| :--- | :--- |
| **Clase Principal** | `net.dasik.social.core.GlobalSocialSystem` |
| **Clase de Registro** | `net.dasik.social.core.SocialRegistry` |
| **Frecuencia de Pulso** | `1 tick` (Regla del Inmortal) |
| **Complejidad de Búsqueda** | $O(1)$ HashMap Fragmentado |
| **Estrategia de Limpieza** | Purga automática mediante `isAlive()` y `isRemoved()` |

---

## ⚡ Descripción General y Regla del Inmortal (Highlander Rule)

El **Sistema Social de Mente Colmena** es el motor central de pulsos de Dasik Library. En lugar de que cientos de entidades sociales ejecuten consultas espaciales pesadas e individuales en cada tick, Dasik Library utiliza un coordinador de pulsos global único (`GlobalSocialSystem`).

### 👑 La Regla del Inmortal ("Solo Puede Haber Uno")
`GlobalSocialSystem` impone que únicamente **un ciclo de pulso global** se ejecute por tick del servidor ($20\text{ ticks} = 1\text{s}$). Si múltiples hilos o subsistemas intentan disparar un pulso dentro del mismo tick, `GlobalSocialSystem` ignora las llamadas duplicadas:

$$\text{Global Pulse Execution} = \begin{cases} \text{Execute Ticks}, & \text{if } \text{currentTick} > \text{lastTick} \\ \text{Skip (No-Op)}, & \text{if } \text{currentTick} \le \text{lastTick} \end{cases}$$

```ascii
[ Server Level Tick ]
         │
         ▼
 ┌──────────────────────┐
 │ GlobalSocialSystem   │  ◄── Highlander Rule Guard (1 tick per server tick)
 └──────────┬───────────┘
            │
            ▼
 ┌──────────────────────┐
 │   SocialRegistry     │  ◄── O(1) Shard Lookup & Dead Entity Purge
 └──────────┬───────────┘
            │
      ┌─────┴────────────────┐
      ▼                      ▼
 ┌──────────────┐      ┌──────────────┐
 │ Entity 1     │      │ Entity N     │
 │ Mood Task    │      │ Mood Task    │
 └──────────────┘      └──────────────┘
```

---

## 🗂️ Arquitectura de `SocialRegistry`

`SocialRegistry` mantiene las entidades sociales activas organizadas por especie y UUIDs del mundo:

* **Registro en $O(1)$**: Las entidades se registran mediante `SocialRegistry.register(SocialEntity entity)`.
* **Purga Automática**: Las referencias obsoletas (chunks descargados, entidades muertas) se purgan automáticamente durante el pulso evaluando `entity.dasik$asEntity().isAlive()` y `isRemoved()`.
* **Fragmentación por Especie**: Las entidades se agrupan por `dasik$getSpeciesId()`, permitiendo consultas de proximidad ultrarrápidas para mecánicas de manada sin iterar sobre todas las entidades del mundo.

---

## 💻 Ejemplo de Código para Desarrolladores

```java
// Registering an entity to the Hive Mind
public class CustomSocialMob extends PathfinderMob implements SocialEntity {
    private final EntitySocialScheduler scheduler = new EntitySocialScheduler();

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            SocialRegistry.register(this);
            GlobalSocialSystem.pulse(serverLevel);
        }
    }

    @Override
    public SocialScheduler dasik$getScheduler() {
        return this.scheduler;
    }
}
```

---

## 🔗 Páginas Relacionadas
* [[Programador Social y Eventos|es_es-Social-Scheduler-and-Events]]
* [[Arquitectura y Distribución de Paquetes|es_es-Architecture-and-Package-Layout]]
