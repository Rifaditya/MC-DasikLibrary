# 🎫 [BL-DASIK-010] PathfinderMobMixin Resolution & Defensive GroupMember Cast Guards

- **Ticket ID**: `[BL-DASIK-010]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Target Version**: `All Anchors` (`1.11.3` / `26.2+`, `1.3.1+1.21.11`, `1.3.1+1.21.1`, `1.3.1+1.20.1`)
- **Scope**: `Mob AI & Flocking Architecture` (`net.dasik.social.mixin.PathfinderMobMixin`, `net.dasik.social.ai.goal.FollowLeaderGoal`, `net.dasik.social.core.group.GroupManager`)
- **Status**: `✅ RESOLVED`

---

## 🎯 1. Problem Statement & Player Impact
`PathfinderMobMixin.java` was removed from [`dasik-library.mixins.json`](file:///e:/Minecraft%20Project/MC-DasikLibrary/src/main/resources/dasik-library.mixins.json#L6-L13) in `v1.8.29`, leaving `PathfinderMobMixin.java` as an unreferenced source file in `MC-DasikLibrary` (and already deleted in `1.21.11`, `1.21.1`, `1.20.1`).

Because `PathfinderMobMixin` is inactive, vanilla `PathfinderMob` entities do **not** implement `GroupMember` or `SocialEntity` at runtime. However, [`FollowLeaderGoal.java`](file:///e:/Minecraft%20Project/MC-DasikLibrary/src/main/java/net/dasik/social/ai/goal/FollowLeaderGoal.java#L33-L132) and [`GroupManager.java`](file:///e:/Minecraft%20Project/MC-DasikLibrary/src/main/java/net/dasik/social/core/group/GroupManager.java#L22-L68) perform unchecked `(GroupMember) mob` and `(GroupMember) leader` casts:
```java
GroupMember member = (GroupMember) this.mob;
```
If a downstream mod attaches `FollowLeaderGoal` or calls `GroupManager.registerMember(PathfinderMob)`, this immediately crashes the game with `ClassCastException: ...PathfinderMob cannot be cast to ...GroupMember`.

Furthermore, `PathfinderMobMixin.java` has unresolved flaws:
1. It does not implement `SocialEntity.dasik$getDNA()` or `dasik$getSocialScale()` (`AbstractMethodError`).
2. `dasik$onInit` registers entities on both client and server, crashing `EntitySocialScheduler` with `ClassCastException: ClientLevel cannot be cast to ServerLevel`.
3. Line 115 uses `@Override public void remove(...)` instead of `@Inject(method = "remove", ...)`.

## 🔬 2. Architectural Blueprint & Target Classes
- **Defensive Cast Guards**:
  - In `FollowLeaderGoal.java`: Guard goal activation:
    ```java
    if (!(this.mob instanceof GroupMember member)) return false;
    ```
    Ensure all leader lookups and member updates check `instanceof GroupMember`.
  - In `GroupManager.java`: Guard member registration:
    ```java
    if (!(entity instanceof GroupMember member)) return;
    ```
  - In `AerialFlockingStrategy.java`: Guard flocking leader casts with `instanceof GroupMember`.
- **PathfinderMobMixin Decision**:
  - Purge orphaned `PathfinderMobMixin.java` from `MC-DasikLibrary` to match older version anchors, OR re-enable and complete it with clean server-only gating (`if (!level.isClientSide())`), `@Inject` on `remove`, and full `SocialEntity` interface contracts.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- Parity alignment: Legacy anchors `1.21.11`, `1.21.1`, and `1.20.1` have already purged `PathfinderMobMixin.java`. Align `MC-DasikLibrary` with this clean state or author the universal mixin consistently across all 4 anchors.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Vanilla Mobs without Mixin**: Mobs without `GroupMember` attached must fail goal execution gracefully without crashing the server.

## 🚀 5. Implementation Steps
1. Add `instanceof GroupMember` guards across `FollowLeaderGoal`, `GroupManager`, and `AerialFlockingStrategy`.
2. Clean up or properly re-integrate `PathfinderMobMixin.java`.
3. Author unit tests asserting non-`GroupMember` mobs handle `FollowLeaderGoal` safely.
4. Verify anchor parity across `1.21.11`, `1.21.1`, and `1.20.1`.

## 🏁 6. 5-Pillar Verification Checklist
- [x] **Pillar 1 (Null & Type Safety)**: Non-`GroupMember` mobs passed to `FollowLeaderGoal` return `false` without `ClassCastException`.
- [x] **Pillar 2 (Concurrency & State)**: GroupManager registrations gracefully ignore non-implementing entities.
- [x] **Pillar 3 (Mixin Registry Integrity)**: `dasik-library.mixins.json` matches files on disk 1:1.
- [x] **Pillar 4 (Client/Server Sidedness)**: No `ClientLevel` to `ServerLevel` casting occurs in social entity registration.
- [x] **Pillar 5 (Multi-Era Parity)**: All 4 version anchors share identical defensive guard contracts.
