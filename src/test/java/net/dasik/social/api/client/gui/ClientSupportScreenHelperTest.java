// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.client.gui;

import net.dasik.social.api.SocialLinks;
import net.dasik.social.api.test.DasikContractVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ClientSupportScreenHelperTest {

    @Test
    @DisplayName("Verify createYaclButton and createDiscordYaclButton execute headlessly without unhandled exceptions")
    public void testCreateYaclButtonsHeadless() {
        assertDoesNotThrow(ClientSupportScreenHelper::createYaclButton);
        assertDoesNotThrow(ClientSupportScreenHelper::createDiscordYaclButton);
    }

    @Test
    @DisplayName("Verify openUrl, openDiscord, and openKofi handle headless execution safely without unhandled exceptions")
    public void testOpenUrlHeadlessSafety() {
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, SocialLinks.DISCORD_INVITE_URL));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openDiscord(null));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openKofi(null));

        // Null and blank handling
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, null));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, ""));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, "   "));
    }

    @Test
    @DisplayName("Verify untyped Object overloads handle null and arbitrary objects safely")
    public void testUntypedObjectOverloads() {
        Object dummyNonScreen = new Object();
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(dummyNonScreen, SocialLinks.KOFI_URL));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openDiscord(dummyNonScreen));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openKofi(dummyNonScreen));
    }

    @Test
    @DisplayName("Pillar 4: Verify openUrl handles malformed URIs, illegal characters, and whitespace safely")
    public void testOpenUrlMalformedUriSafety() {
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, "invalid uri ::: // []"));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, "http:// invalid space .com"));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, "   https://ko-fi.com/dasikigaijin   "));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, ":::not:a:valid:url:::"));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, "\n\t"));
    }

    @Test
    @DisplayName("Pillar 5: Verify bytecode descriptor references ConfirmLinkScreen URI overload and purges String overload")
    public void testBytecodeDescriptorVerification() throws Exception {
        byte[] classBytes;
        try (InputStream is = ClientSupportScreenHelper.class.getResourceAsStream("ClientSupportScreenHelper.class")) {
            if (is != null) {
                classBytes = is.readAllBytes();
            } else {
                try (InputStream isLoader = ClientSupportScreenHelper.class.getClassLoader()
                        .getResourceAsStream("net/dasik/social/api/client/gui/ClientSupportScreenHelper.class")) {
                    assertNotNull(isLoader, "ClientSupportScreenHelper.class must be resolvable via ClassLoader");
                    classBytes = isLoader.readAllBytes();
                }
            }
        }

        // 1. Verify constant pool contains the URI descriptor
        Set<String> utf8Constants = DasikContractVerifier.extractUtf8Constants(classBytes);
        assertFalse(utf8Constants.isEmpty(), "Constant pool of ClientSupportScreenHelper must not be empty");
        final String uriDescriptor = "(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V";
        assertTrue(utf8Constants.contains(uriDescriptor),
            "ClientSupportScreenHelper constant pool must reference ConfirmLinkScreen URI descriptor: " + uriDescriptor);

        // 2. ASM bytecode inspection: assert ConfirmLinkScreen.confirmLinkNow invokes URI overload and NOT String overload
        ClassReader cr = new ClassReader(classBytes);
        boolean[] uriMethodFound = new boolean[1];
        boolean[] stringMethodFound = new boolean[1];

        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(Opcodes.ASM9) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String methodName, String methodDesc, boolean isInterface) {
                        if (owner.equals("net/minecraft/client/gui/screens/ConfirmLinkScreen") && methodName.equals("confirmLinkNow")) {
                            if (methodDesc.equals("(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V")) {
                                uriMethodFound[0] = true;
                            } else if (methodDesc.equals("(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V")) {
                                stringMethodFound[0] = true;
                            }
                        }
                    }
                };
            }
        }, 0);

        assertTrue(uriMethodFound[0],
            "ClientSupportScreenHelper bytecode must invoke ConfirmLinkScreen.confirmLinkNow(Screen, URI)");
        assertFalse(stringMethodFound[0],
            "ClientSupportScreenHelper bytecode must strictly NOT invoke ConfirmLinkScreen.confirmLinkNow(Screen, String)");
    }
}
