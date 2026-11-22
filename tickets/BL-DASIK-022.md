# 🎫 [BL-DASIK-022] Unified Creator Support Prompt & Client Config Engine (World-Start Greeting + Milestone Triggers + YACL Toggle)

- **Ticket ID**: `[BL-DASIK-022]`
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Target Version**: `All Active Anchors (MC-DasikLibrary 26.2/26.3, 26.1.2, 1.21.11, 1.21.1, 1.20.1)`
- **Scope**: `ClientSupportScreenHelper.java`, `DasikSupportHelper.java`, `DasikPromptRegistry.java`, `DasikClientConfig.java`, and YACL Config GUI
- **Status**: `📌 DEFERRED`
- **Target Repository/Root**: `MC-DasikLibrary` (`E:\Minecraft Project\MC-DasikLibrary`) & Sister Multi-Era Anchors
- **Unblocks**: [`BL-WS-047`](file:///E:/Minecraft%20Project/tickets/BL-WS-047.md) (Template and studio-wide mod rollout)
- **Governance Mandate**: [Monetization & Creator Support Policy](file:///E:/Minecraft%20Project/.agents/rules/Monetization%20&%20Creator%20Support%20Policy.md) (§6–§7), [Server-Side Primacy Policy](file:///E:/Minecraft%20Project/.agents/rules/Server-Side%20Primacy%20&%20Vanilla%20Client%20Compatibility%20Policy.md)
- **Date Added**: `2026-11-22`

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Implement a unified, player-first creator support prompt engine in `DasikLibrary` combining:
   - A warm, non-intrusive **World-Start Greeting** delivered 8 seconds (160 ticks) after world join.
   - Paced **Milestone Advancement Prompts** delivered 3 seconds after achieving mod milestones (with a 5-minute cooldown).
   - An intuitive **ModMenu / YACL Client Config Toggle** (`Show Support Prompts in Chat`, default `true`) allowing players to completely silence recurring chat reminders at will.
2. **In-Game Experience (Before vs. After)**:
   - *Before*: Players either received rigid world-join greetings with no way to turn them off, or feared unsolicited nagging.
   - *After*: When entering a world, players receive a polite, styled chat welcome after loading finishes. As they reach gameplay milestones, subtle prompts highlight Early Access builds on Ko-fi and Discord. If any player prefers a completely silent chat, one click in ModMenu / YACL turns off all chat reminders permanently.
3. **🚫 Anti-Goals (The Creep Shield)**:
   - ❌ **Zero Dimension Portal Spam**: Moving across dimensions (Overworld ⇄ Nether ⇄ End) must NEVER re-trigger the world-start greeting.
   - ❌ **Zero Dedicated Server Enforcement / No Server GameRules**: Prompt settings and tracking are strictly client-side preferences. Dedicated servers running DasikLibrary must never broadcast to vanilla clients or execute client GUI code.
   - ❌ **Zero Modal Popups / Title Banners**: All prompts remain strictly in chat with standard chat fade; zero screen takeovers, screen overlays, or intrusive sound alerts.
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [ ] Client config persistence (`config/dasik_library_client.json`) with `showSupportPrompts = true` and `showWorldStartGreeting = true`.
   - [ ] ModMenu / YACL category integration with an explicit toggle switch and verified Ko-fi / Discord button actions.
   - [ ] World-start greeting scheduler executing once per world connection at tick $t = 160$ (~8s post-join delay).
   - [ ] `DasikPromptRegistry.register(...)` API allowing downstream mods to register milestone advancement triggers without duplicating code.
   - [ ] Pacing queue enforcing a 5-minute (6000 ticks) delay between subsequent advancement prompts.
   - [ ] Headless unit tests validating config serialization, toggle toggling, pacing delay, and dedicated server classloading safety.

---

## 🔬 2. Calibrated Timing & Pacing Architecture

| Event / Trigger | Delay | Recurrence / Cooldown | Behavior & Content |
| :--- | :--- | :--- | :--- |
| **World-Start Greeting** | **8.0s** (160 ticks post-join) | Once per connection (dimension portal changes suppressed) | Warm solo dev welcome with clickable `[Ko-fi]` and `[Discord]` links. |
| **Milestone Advancement Trigger** | **3.0s** (60 ticks post-advancement) | Once per mod per world; **5-minute cooldown** between any prompts | Congratulatory milestone note highlighting Early Access builds. |
| **Player Toggles OFF in YACL** | Immediate | Permanent until player re-enables | Silences **all** world-start and milestone prompts immediately. |

---

## 🛡️ 3. Architectural Blueprint & Target Classes

1. **Config Engine (`net.dasik.social.api.config.DasikClientConfig`)**:
   - Manages client-side JSON serialization at `config/dasik_library_client.json`.
   - Fields: `boolean showSupportPrompts = true;`, `boolean showWorldStartGreeting = true;`.
2. **YACL Integration (`ClientSupportScreenHelper.java`)**:
   - In `createYaclScreen(...)` / ModMenu config builder:
     - Add a `BooleanOption` bound to `DasikClientConfig.showSupportPrompts`.
     - Retain top-pinned `createYaclButton()` and `createDiscordYaclButton()`.
3. **Event Listeners & Scheduling**:
   - Client connection listener: Tracks `connectedServerAddress` / `singleplayerLevelId`.
   - Tick listener: Increments post-join timer ($t = 160$ ticks) and manages the 5-minute prompt pacing queue.
   - Dimension change listener: Preserves world-session ID to suppress Nether/End portal spam.
4. **Central Registration API (`DasikPromptRegistry`)**:
   - `public static void register(String modId, String displayName, String advancementId)`:
     Downstream mods (Better Dogs, Agrarian Reform, etc.) call this 1-line hook during initialization.

---

## 🚀 4. Multi-Era Parity Specifications

- **MC 26.2 / 26.3 (`MC-DasikLibrary`)**: Sovereign Lead implementation using Fabric lifecycle events and YACL v3.
- **MC 26.1.2, 1.21.11, 1.21.1**: Architectury multi-loader ports (`common`, `fabric`, `neoforge`) sharing client config and prompt registry.
- **MC 1.20.1**: Java 17 multi-loader port with legacy YACL / Cloth Config compatibility.
