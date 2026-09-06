# 확률 및 수학 유틸리티

| 유틸리티 | 클래스 | 역할 |
| :--- | :--- | :--- |
| **고속 난수 생성기** | `net.dasik.social.util.FastRandom` | 논블로킹 64-bit XORSHIFT 난수기 |
| **확률 샘플링** | `net.dasik.social.util.StochasticUtil` | 십분위수(`1/10`), 백분율(`1/100`), 퍼밀(`1/1000`) |
| **시간 변환 상수** | $20\text{ ticks} = 1\text{초}$ | Minecraft 내부 틱 변환 계수 |

---

## 🎲 `FastRandom`의 XORSHIFT 알고리즘

`FastRandom`은 `java.util.Random`의 원자적 동기화 오버헤드를 배제하여 극도로 빠르고 균일한 통계적 분포를 제공합니다:

$$x \leftarrow x \oplus (x \ll 13); \quad x \leftarrow x \oplus (x \gg 7); \quad x \leftarrow x \oplus (x \ll 17)$$

---

## 📊 `StochasticUtil`을 통한 정밀 샘플링

`StochasticUtil`은 정수 버림 오차를 방지하여 정확한 확률 추첨을 보장합니다:

```java
// Roll probability out of 1000 (e.g. 5 permille = 0.5% chance)
if (StochasticUtil.rollPermille(random, 5)) {
    // Rare mutation triggered
}

// Roll percentage chance (0-100)
if (StochasticUtil.rollPercent(random, 25)) {
    // 25% chance branch
}
```

---

## 🔗 관련 페이지
* [[동물 유전학 엔진|ko_kr-Animal-Genetics-Engine]]
* [[군집 지능 소셜 시스템|ko_kr-Hive-Mind-Social-System]]
