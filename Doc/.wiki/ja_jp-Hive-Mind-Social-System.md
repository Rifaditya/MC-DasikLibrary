# 群知能ソーシャルシステム

| システムパラメータ | 設定値 |
| :--- | :--- |
| **メインクラス** | `net.dasik.social.core.GlobalSocialSystem` |
| **レジストリクラス** | `net.dasik.social.core.SocialRegistry` |
| **パルス頻度** | `1 tick`（ハイランダーの原則） |
| **検索計算量** | $O(1)$ シャーディング HashMap |
| **メモリ解放戦略** | `isAlive()` および `isRemoved()` による自動パージ |

---

## ⚡ 概要＆ハイランダーの原則

**群知能ソーシャルシステム（Hive Mind）** は、Dasik Library の最重要コアエンジンです。何百ものソーシャルエンティティが毎 Tick 重い空間探索を個別に行う代わりに、Dasik Library は単一の集中パルスコネクタ（`GlobalSocialSystem`）を採用しています。

### 👑 ハイランダーの原則（「生き残るのは常に一人」）
`GlobalSocialSystem` は、サーバーの 1 Tick（$20\text{ ticks} = 1\text{秒}$）ごとに厳密に**1 回のグローバルパルスサイクル**のみを実行することを強制します。同一番号の Tick 内で複数のスレッドやサブシステムからパルスが要求されても、過剰な呼び出しは完全に無視されます：

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

## 🗂️ `SocialRegistry` のアーキテクチャ

`SocialRegistry` はアクティブなソーシャルエンティティを種族およびワールド UUID 別に整理・管理します：

* **$O(1)$ 高速登録**: エンティティは `SocialRegistry.register(SocialEntity entity)` で登録されます。
* **自動パージ**: アンロードされたチャンクや死亡した Mob の参照は、パルス実行ループ内で `entity.dasik$asEntity().isAlive()` および `isRemoved()` を検証して自動削除されます。
* **種族別シャーディング**: エンティティは `dasik$getSpeciesId()` ごとにグループ化されているため、ワールド全体をスキャンすることなく、近隣の群れ仲間を瞬時に特定できます。

---

## 💻 開発者向けコード例

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

## 🔗 関連ページ
* [[ソーシャルスケジューラ＆イベント|ja_jp-Social-Scheduler-and-Events]]
* [[アーキテクチャとパッケージ構成|ja_jp-Architecture-and-Package-Layout]]
