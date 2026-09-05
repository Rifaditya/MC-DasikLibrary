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
    @DisplayName("Verify button and tooltip return translatable components with standard keys")
    public void testButtonAndTooltipComponents() {
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
    }

    @Test
    @DisplayName("Verify command footer component structure and ClickEvent properties")
    public void testCommandFooterStructure() {
        Component footer = DasikSupportHelper.getCommandFooter();
        assertNotNull(footer);

        // Verify root prompt
        ComponentContents promptContents = footer.getContents();
        assertInstanceOf(TranslatableContents.class, promptContents);
        assertEquals(DasikSupportHelper.KEY_CHAT_PROMPT, ((TranslatableContents) promptContents).getKey());

        // Verify sibling link
        assertFalse(footer.getSiblings().isEmpty(), "Command footer must contain sibling link component");
        Component linkComponent = footer.getSiblings().get(0);
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
    @DisplayName("Verify appendCommandFooter feeds component and handles null consumer safely")
    public void testAppendCommandFooter() {
        AtomicReference<Component> received = new AtomicReference<>();
        DasikSupportHelper.appendCommandFooter(received::set);
        assertNotNull(received.get());

        // Null safety
        assertDoesNotThrow(() -> DasikSupportHelper.appendCommandFooter(null));
    }

    @Test
    @DisplayName("Verify createYaclButton does not throw when called headlessly")
    public void testCreateYaclButtonSafeHeadless() {
        assertDoesNotThrow(DasikSupportHelper::createYaclButton);
    }
}
