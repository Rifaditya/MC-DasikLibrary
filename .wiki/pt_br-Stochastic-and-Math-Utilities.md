# Utilitários Estocásticos e Matemáticos

| Classe Utilitária | Finalidade |
| :--- | :--- |
| `net.dasik.social.util.FastRandom` | Singleton `RandomSource` com alocação zero baseado em `ThreadLocalRandom` |
| `net.dasik.social.util.StochasticUtil` | Sorteios probabilísticos em por milhar, decis e porcentagens |
| `net.dasik.social.util.TimeUtil` | Contagem de dias no jogo (`gameTime / 24000L`) e cálculo de ciclos |

---

## 🎲 `FastRandom` (`RandomSource`)

O `FastRandom` fornece um `RandomSource` singleton seguro para threads (`FastRandom.INSTANCE`) alimentado por `ThreadLocalRandom`:

```java
FastRandom random = FastRandom.INSTANCE;
float val = random.nextFloat(); // [0.0f, 1.0f)
```

---

## ⏱️ Matemática de Ciclos no `TimeUtil`

$$1\text{ Minecraft Day} = 24,000\text{ ticks}$$

```java
long currentDay = TimeUtil.getGameDay(level.getGameTime());
long ticksToTarget = TimeUtil.getCycleDistance(currentTime, targetTime, 24000L);
```

---

## 🔗 Páginas Relacionadas
* [[Motor de Genética Animal|pt_br-Animal-Genetics-Engine]]
* [[Gerenciador Dinâmico de GameRules|pt_br-Dynamic-GameRules-Manager]]
