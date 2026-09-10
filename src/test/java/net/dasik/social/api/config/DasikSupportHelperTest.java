// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.config;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class DasikSupportHelperTest {

    @Test
    @DisplayName("Verify Ko-fi URL constant is well-formed URI")
    public void testKofiUrlFormat() {
        assertEquals("https://ko-fi.com/dasikigaijin", DasikSupportHelper.KOFI_URL);
        assertDoesNotThrow(() -> URI.create(DasikSupportHelper.KOFI_URL));
    }

    @Test
    @DisplayName("Verify label, button, and tooltip return translatable components with standard keys")
    public void testButtonAndTooltipComponents() {
        Component label = DasikSupportHelper.getLabelText();
        assertNotNull(label);
        ComponentContents labelContents = label.getContents();
        assertInstanceOf(TranslatableContents.class, labelContents);
        assertEquals(DasikSupportHelper.KEY_LABEL, ((TranslatableContents) labelContents).getKey());

        Component button = DasikSupportHelper.getButtonText();
        assertNotNull(button);
        ComponentContents buttonContents = button.getContents();
        assertInstanceOf(TranslatableContents.class, buttonContents);
        assertEquals(DasikSupportHelper.KEY_BUTTON, ((TranslatableContents) buttonContents).getKey());

        Component tooltip = DasikSupportHelper.getTooltipText();
        assertNotNull(tooltip);
        ComponentContents tooltipContents = tooltip.getContents();
        assertInstanceOf(TranslatableContents.class, tooltipContents);
        assertEquals(DasikSupportHelper.KEY_TOOLTIP, ((TranslatableContents) tooltipContents).getKey());

        // Discord components
        Component discordLabel = DasikSupportHelper.getDiscordLabelText();
        assertNotNull(discordLabel);
        ComponentContents discordLabelContents = discordLabel.getContents();
        assertInstanceOf(TranslatableContents.class, discordLabelContents);
        assertEquals(DasikSupportHelper.KEY_DISCORD_LABEL, ((TranslatableContents) discordLabelContents).getKey());

        Component discordButton = DasikSupportHelper.getDiscordButtonText();
        assertNotNull(discordButton);
        ComponentContents discordButtonContents = discordButton.getContents();
        assertInstanceOf(TranslatableContents.class, discordButtonContents);
        assertEquals(DasikSupportHelper.KEY_DISCORD_BUTTON, ((TranslatableContents) discordButtonContents).getKey());

        Component discordTooltip = DasikSupportHelper.getDiscordTooltipText();
        assertNotNull(discordTooltip);
        ComponentContents discordTooltipContents = discordTooltip.getContents();
        assertInstanceOf(TranslatableContents.class, discordTooltipContents);
        assertEquals(DasikSupportHelper.KEY_DISCORD_TOOLTIP, ((TranslatableContents) discordTooltipContents).getKey());
    }

    @Test
    @DisplayName("Verify Discord command footer component structure and ClickEvent properties")
    public void testDiscordCommandFooterStructure() {
        Component discordFooter = DasikSupportHelper.getDiscordCommandFooter();
        assertNotNull(discordFooter);

        // Verify root prompt
        ComponentContents promptContents = discordFooter.getContents();
        assertInstanceOf(TranslatableContents.class, promptContents);
        assertEquals(DasikSupportHelper.KEY_DISCORD_CHAT_PROMPT, ((TranslatableContents) promptContents).getKey());

        // Verify sibling link
        assertFalse(discordFooter.getSiblings().isEmpty(), "Discord footer must contain sibling link component");
        Component linkComponent = discordFooter.getSiblings().get(0);
        ComponentContents linkContents = linkComponent.getContents();
        assertInstanceOf(TranslatableContents.class, linkContents);
        assertEquals(DasikSupportHelper.KEY_DISCORD_CHAT_LINK, ((TranslatableContents) linkContents).getKey());

        // Verify ClickEvent
        ClickEvent clickEvent = linkComponent.getStyle().getClickEvent();
        assertNotNull(clickEvent, "Link component must have a ClickEvent attached");
        assertInstanceOf(ClickEvent.OpenUrl.class, clickEvent);
        assertEquals(net.dasik.social.api.SocialLinks.getDiscordUri(), ((ClickEvent.OpenUrl) clickEvent).uri());
    }

    @Test
    @DisplayName("Verify Ko-fi command footer component structure and ClickEvent properties")
    public void testKofiCommandFooterStructure() {
        Component kofiFooter = DasikSupportHelper.getKofiCommandFooter();
        assertNotNull(kofiFooter);

        // Verify root prompt
        ComponentContents promptContents = kofiFooter.getContents();
        assertInstanceOf(TranslatableContents.class, promptContents);
        assertEquals(DasikSupportHelper.KEY_CHAT_PROMPT, ((TranslatableContents) promptContents).getKey());

        // Verify sibling link
        assertFalse(kofiFooter.getSiblings().isEmpty(), "Ko-fi footer must contain sibling link component");
        Component linkComponent = kofiFooter.getSiblings().get(0);
        ComponentContents linkContents = linkComponent.getContents();
        assertInstanceOf(TranslatableContents.class, linkContents);
        assertEquals(DasikSupportHelper.KEY_CHAT_LINK, ((TranslatableContents) linkContents).getKey());

        // Verify ClickEvent
        ClickEvent clickEvent = linkComponent.getStyle().getClickEvent();
        assertNotNull(clickEvent, "Link component must have a ClickEvent attached");
        assertInstanceOf(ClickEvent.OpenUrl.class, clickEvent);
        assertEquals(URI.create(DasikSupportHelper.KOFI_URL), ((ClickEvent.OpenUrl) clickEvent).uri());
    }

    @Test
    @DisplayName("Verify combined command footer component structure and ClickEvent properties")
    public void testCombinedCommandFooterStructure() {
        Component footer = DasikSupportHelper.getCommandFooter();
        assertNotNull(footer);

        // Verify root prompt starts with Discord prompt
        ComponentContents promptContents = footer.getContents();
        assertInstanceOf(TranslatableContents.class, promptContents);
        assertEquals(DasikSupportHelper.KEY_DISCORD_CHAT_PROMPT, ((TranslatableContents) promptContents).getKey());

        // Verify siblings: [0] = Discord link, [1] = separator " • ", [2] = Ko-fi section
        assertTrue(footer.getSiblings().size() >= 3, "Combined command footer must contain Discord link, separator, and Ko-fi section");

        // Sibling 0: Discord link
        Component discordLinkComponent = footer.getSiblings().get(0);
        assertInstanceOf(TranslatableContents.class, discordLinkComponent.getContents());
        assertEquals(DasikSupportHelper.KEY_DISCORD_CHAT_LINK, ((TranslatableContents) discordLinkComponent.getContents()).getKey());
        ClickEvent discordClickEvent = discordLinkComponent.getStyle().getClickEvent();
        assertNotNull(discordClickEvent);
        assertInstanceOf(ClickEvent.OpenUrl.class, discordClickEvent);
        assertEquals(net.dasik.social.api.SocialLinks.getDiscordUri(), ((ClickEvent.OpenUrl) discordClickEvent).uri());

        // Sibling 1: separator
        assertEquals(" • ", footer.getSiblings().get(1).getString());

        // Sibling 2: Ko-fi section
        Component kofiSection = footer.getSiblings().get(2);
        assertInstanceOf(TranslatableContents.class, kofiSection.getContents());
        assertEquals(DasikSupportHelper.KEY_CHAT_PROMPT, ((TranslatableContents) kofiSection.getContents()).getKey());
        assertFalse(kofiSection.getSiblings().isEmpty());
        Component kofiLinkComponent = kofiSection.getSiblings().get(0);
        assertInstanceOf(TranslatableContents.class, kofiLinkComponent.getContents());
        assertEquals(DasikSupportHelper.KEY_CHAT_LINK, ((TranslatableContents) kofiLinkComponent.getContents()).getKey());
        ClickEvent kofiClickEvent = kofiLinkComponent.getStyle().getClickEvent();
        assertNotNull(kofiClickEvent);
        assertInstanceOf(ClickEvent.OpenUrl.class, kofiClickEvent);
        assertEquals(URI.create(DasikSupportHelper.KOFI_URL), ((ClickEvent.OpenUrl) kofiClickEvent).uri());
    }

    @Test
    @DisplayName("Verify appendCommandFooter feeds component and handles null consumer safely")
    public void testAppendCommandFooter() {
        AtomicReference<Component> received = new AtomicReference<>();
        DasikSupportHelper.appendCommandFooter(received::set);
        assertNotNull(received.get());

        // Null safety
        assertDoesNotThrow(() -> DasikSupportHelper.appendCommandFooter(null));
    }

    @Test
    @DisplayName("Verify createYaclButton and createDiscordYaclButton do not throw when called headlessly")
    public void testCreateYaclButtonSafeHeadless() {
        assertDoesNotThrow(DasikSupportHelper::createYaclButton);
        assertDoesNotThrow(DasikSupportHelper::createDiscordYaclButton);
    }
}
