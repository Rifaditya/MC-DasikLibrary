# Движок генетики животных

| Свойство | Значение |
| :--- | :--- |
| **Основной пакет** | `net.dasik.social.api.genetics` |
| **Идентификатор вложения** | `dasik-library:genetics` |
| **Диапазон масштаба** | `0.1x` - `3.0x` (По умолчанию: `0.5x` - `2.0x`) |
| **Порог недомерка (Runt)** | Масштаб $< 0.85x$ (или инбридинг) |
| **Порог гиганта (Giant)** | Масштаб $> 1.15x$ |
| **Codec данных** | `EntityGenetics.CODEC` |

---

## 🧬 Обзор и структура ДНК

**Движок генетики животных** предоставляет независимую от типа сущности систему сохранения генетики на базе Fabric `AttachmentType`. Каждое генетическое существо хранит запись `EntityGenetics`:

1. **`parent1Uuid` (`Optional<UUID>`)**: UUID первого родителя при разведении.
2. **`parent2Uuid` (`Optional<UUID>`)**: UUID второго родителя при разведении.
3. **`inbred` (`boolean`)**: Флаг наличия общего родства родителей.
4. **`traitsRolled` (`boolean`)**: Флаг завершения первоначальной генерации характеристик.
5. **`traits` (`Map<String, Float>`)**: Значения динамических признаков (`scale`, `max_health`, `attack_damage`, `movement_speed`).

```ascii
 ┌────────────────────────────────────────────────────────────────────────┐
 │                           EntityGenetics                               │
 ├───────────────────────────────────┬────────────────────────────────────┤
 │ parent1Uuid: Optional<UUID>       │ inbred: boolean                    │
 │ parent2Uuid: Optional<UUID>       │ traitsRolled: boolean              │
 │ traits: Map<String, Float>        │ Codec: EntityGenetics.CODEC        │
 └───────────────────────────────────┴────────────────────────────────────┘
```

---

## 📐 Математика наследования и мутаций

При скрещивании двух сущностей генетика потомства рассчитывается через `GeneticsEngine.calculateOffspringGenetics`:

### 1. Формула наследования масштаба
Базовый масштаб потомка $S_{\text{offspring}}$ формируется как среднее родителей плюс гауссово треугольное отклонение мутации $\Delta_{\text{mutate}}$:

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

Где $\Delta_{\text{mutate}}$ выбирается из треугольного распределения в границах $\pm \text{mutationRate}$.

### 2. Формула штрафа за инбридинг
Если родители состоят в близком родстве ($F > 0$), применяется штраф здоровья и масштаба $\text{Penalty}_{\text{inbreeding}}$:

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 Пример кода для разработчиков

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 Связанные страницы
* [[API генетики и родословная|ru_ru-Genetics-API-and-Pedigree]]
* [[Очистка устаревших атрибутов и масштабирование|ru_ru-Stale-Attribute-Purging-and-Scale]]
