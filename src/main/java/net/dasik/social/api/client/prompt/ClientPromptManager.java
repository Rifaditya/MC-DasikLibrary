// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.client.prompt;

import net.dasik.social.api.annotation.DasikApiStatus;
import net.dasik.social.api.config.DasikClientConfig;
import net.dasik.social.api.config.DasikSupportHelper;
import net.dasik.social.api.prompt.DasikPromptRegistry;
import net.dasik.social.api.prompt.DasikPromptRegistry.PromptRegistration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Client-side lifecycle coordinator for creator support greetings and milestone reminders.
 * <p>
 * Timing Architecture:
 * <ul>
 *   <li>World-Start Greeting: Delivered at $t = 160$ ticks (~8 seconds post-join) once per session.</li>
 *   <li>Portal Suppression: Dimension changes preserve the current session ID and do NOT re-trigger the greeting.</li>
 *   <li>Milestone Pacing: 60-tick (3-second) post-advancement delay with 6000-tick (5-minute) FIFO cooldown.</li>
 *   <li>User Sovereignty: All prompts are immediately silenced if {@code showSupportPrompts} is false.</li>
 * </ul>
 */
@Environment(EnvType.CLIENT)
@DasikApiStatus.Public
public final class ClientPromptManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|PromptManager");

    public static final int GREETING_DELAY_TICKS = 160;       // 8.0 seconds @ 20 TPS
    public static final int MILESTONE_INITIAL_DELAY = 60;     // 3.0 seconds @ 20 TPS
    public static final int MILESTONE_PACING_COOLDOWN = 6000; // 5.0 minutes @ 20 TPS

    private static String currentSessionId = null;
    private static int joinTimer = 0;
    private static boolean greetingSent = false;
    private static int pacingCooldown = 0;

    private static final Set<String> SHOWN_ADVANCEMENTS = ConcurrentHashMap.newKeySet();
    private static final Queue<QueuedMilestone> PENDING_QUEUE = new ConcurrentLinkedQueue<>();

    private static Field progressField;
    private static boolean progressFieldFailed = false;

    private ClientPromptManager() {}

    /**
     * Internal representation of a queued milestone waiting for display.
     */
    public static final class QueuedMilestone {
        private final PromptRegistration registration;
        private final AtomicInteger delayTicks;

        public QueuedMilestone(PromptRegistration registration, int initialDelay) {
            this.registration = registration;
            this.delayTicks = new AtomicInteger(initialDelay);
        }

        public PromptRegistration getRegistration() {
            return registration;
        }

        public int getDelayTicks() {
            return delayTicks.get();
        }

        public int decrementDelay() {
            return delayTicks.decrementAndGet();
        }
    }

    /**
     * Initializes or maintains the world session upon client connection to a server or level.
     * Preserves existing session ID on dimension transitions to prevent portal greeting spam.
     */
    public static void onWorldJoin(Minecraft client) {
        if (currentSessionId != null) {
            LOGGER.debug("Maintaining active world session '{}' across dimension transition.", currentSessionId);
            return;
        }

        currentSessionId = UUID.randomUUID().toString();
        joinTimer = 0;
        greetingSent = false;
        pacingCooldown = 0;
        PENDING_QUEUE.clear();
        SHOWN_ADVANCEMENTS.clear();
        LOGGER.debug("Initialized new world session '{}' for client.", currentSessionId);
    }

    /**
     * Resets session state upon disconnecting from a server or returning to the main menu.
     */
    public static void onWorldDisconnect() {
        LOGGER.debug("Terminating world session '{}'.", currentSessionId);
        currentSessionId = null;
        joinTimer = 0;
        greetingSent = false;
        pacingCooldown = 0;
        PENDING_QUEUE.clear();
        SHOWN_ADVANCEMENTS.clear();
    }

    /**
     * Called at the end of every client tick to manage greeting timing, pacing cooldowns,
     * advancement polling, and milestone prompt dispatch.
     */
    public static void onClientTick(Minecraft client) {
        if (client == null || client.player == null || client.level == null) {
            return;
        }

        // Global opt-out: player turned off support prompts in config
        if (!DasikClientConfig.get().showSupportPrompts) {
            return;
        }

        // 1. World-Start Greeting Timer
        if (joinTimer < GREETING_DELAY_TICKS) {
            joinTimer++;
            if (joinTimer >= GREETING_DELAY_TICKS && !greetingSent) {
                greetingSent = true;
                if (DasikClientConfig.get().showWorldStartGreeting) {
                    sendWorldStartGreeting(client);
                }
            }
        }

        // 2. Decrement pacing cooldown
        if (pacingCooldown > 0) {
            pacingCooldown--;
        }

        // 3. Periodic advancement polling (once every second / 20 ticks)
        if (client.level.getGameTime() % 20 == 0) {
            pollAdvancements(client);
        }

        // 4. Milestone Queue FIFO Processing
        if (!PENDING_QUEUE.isEmpty()) {
            QueuedMilestone head = PENDING_QUEUE.peek();
            if (head != null) {
                if (head.getDelayTicks() > 0) {
                    head.decrementDelay();
                } else if (pacingCooldown <= 0) {
                    PENDING_QUEUE.poll();
                    if (head.getRegistration().isEnabled() && DasikClientConfig.get().showSupportPrompts) {
                        sendMilestonePrompt(client, head.getRegistration());
                        pacingCooldown = MILESTONE_PACING_COOLDOWN;
                    }
                }
            }
        }
    }

    /**
     * Notifies the prompt manager that a milestone advancement has been earned.
     * Downstream mods may also call this directly.
     */
    public static void onAdvancementCompleted(String advancementId) {
        if (advancementId == null || !DasikClientConfig.get().showSupportPrompts) {
            return;
        }

        Optional<PromptRegistration> opt = DasikPromptRegistry.get(advancementId);
        if (opt.isPresent()) {
            PromptRegistration reg = opt.get();
            if (reg.isEnabled() && SHOWN_ADVANCEMENTS.add(advancementId)) {
                PENDING_QUEUE.offer(new QueuedMilestone(reg, MILESTONE_INITIAL_DELAY));
                LOGGER.debug("Queued milestone support prompt for '{}' (delay: {} ticks).", reg.displayName(), MILESTONE_INITIAL_DELAY);
            }
        }
    }

    /**
     * Builds and sends the world-start greeting message to the player's chat.
     */
    public static void sendWorldStartGreeting(Minecraft client) {
        if (client.player == null) {
            return;
        }

        MutableComponent welcome = Component.translatableWithFallback(
                "dasiklibrary.prompt.greeting.welcome",
                "✨ Thanks for playing with Dasik mods!"
        ).withStyle(ChatFormatting.GOLD);

        Component footer = DasikSupportHelper.getCommandFooter();
        MutableComponent fullMessage = Component.literal("")
                .append(welcome)
                .append(Component.literal(" • "))
                .append(footer);

        client.player.sendSystemMessage(fullMessage);
    }

    /**
     * Builds and sends a milestone congratulatory message to the player's chat.
     */
    public static void sendMilestonePrompt(Minecraft client, PromptRegistration reg) {
        if (client.player == null || reg == null) {
            return;
        }

        MutableComponent milestoneText = Component.translatableWithFallback(
                "dasiklibrary.prompt.milestone",
                "🎉 Milestone reached in %s! Early access builds: ",
                reg.displayName()
        ).withStyle(ChatFormatting.AQUA);

        Component footer = DasikSupportHelper.getCommandFooter();
        MutableComponent fullMessage = Component.literal("")
                .append(milestoneText)
                .append(footer);

        client.player.sendSystemMessage(fullMessage);
    }

    @SuppressWarnings("unchecked")
    private static void pollAdvancements(Minecraft client) {
        if (client.getConnection() == null) {
            return;
        }

        ClientAdvancements advancements = client.getConnection().getAdvancements();
        if (advancements == null) {
            return;
        }

        if (progressField == null && !progressFieldFailed) {
            try {
                progressField = ClientAdvancements.class.getDeclaredField("progress");
                progressField.setAccessible(true);
            } catch (Throwable t) {
                progressFieldFailed = true;
                LOGGER.debug("Reflection access to ClientAdvancements.progress unavailable: {}", t.getMessage());
            }
        }

        if (progressField != null) {
            try {
                Map<AdvancementHolder, AdvancementProgress> map =
                        (Map<AdvancementHolder, AdvancementProgress>) progressField.get(advancements);
                if (map != null) {
                    for (Map.Entry<AdvancementHolder, AdvancementProgress> entry : map.entrySet()) {
                        if (entry.getValue() != null && entry.getValue().isDone()) {
                            String advancementId = entry.getKey().id().toString();
                            if (DasikPromptRegistry.isRegistered(advancementId)) {
                                onAdvancementCompleted(advancementId);
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                LOGGER.debug("Failed reading client advancement progress: {}", t.getMessage());
            }
        }
    }

    // ==========================================
    // TESTING & STATE ACCESSORS
    // ==========================================

    public static String getCurrentSessionId() {
        return currentSessionId;
    }

    public static void setCurrentSessionId(String sessionId) {
        currentSessionId = sessionId;
    }

    public static int getJoinTimer() {
        return joinTimer;
    }

    public static void setJoinTimer(int timer) {
        joinTimer = timer;
    }

    public static boolean isGreetingSent() {
        return greetingSent;
    }

    public static void setGreetingSent(boolean sent) {
        greetingSent = sent;
    }

    public static int getPacingCooldown() {
        return pacingCooldown;
    }

    public static void setPacingCooldown(int cooldown) {
        pacingCooldown = cooldown;
    }

    public static Queue<QueuedMilestone> getPendingQueue() {
        return PENDING_QUEUE;
    }

    public static Set<String> getShownAdvancements() {
        return SHOWN_ADVANCEMENTS;
    }
}
