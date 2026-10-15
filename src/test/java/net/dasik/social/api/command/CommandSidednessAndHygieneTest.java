// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandSidednessAndHygieneTest {

    @Test
    @DisplayName("Pillar 5 Sidedness: Verify ZERO net.minecraft.client.* imports in net.dasik.social.api.command")
    void testZeroClientImportsInCommandPackage() throws IOException {
        Path commandDir = Path.of("src/main/java/net/dasik/social/api/command");
        assertTrue(Files.exists(commandDir), "Command package directory must exist: " + commandDir);

        try (Stream<Path> files = Files.walk(commandDir)) {
            files.filter(p -> p.toString().endsWith(".java")).forEach(path -> {
                try {
                    List<String> lines = Files.readAllLines(path);
                    for (int i = 0; i < lines.size(); i++) {
                        String line = lines.get(i).trim();
                        if (line.startsWith("import net.minecraft.client.")) {
                            fail("Sidedness Violation: Found client import in " + path.getFileName() + " at line " + (i + 1) + ": " + line);
                        }
                    }
                } catch (IOException e) {
                    fail("Failed to read file: " + path);
                }
            });
        }
    }

    @Test
    @DisplayName("Pillar 5 Hygiene: Verify single-line GPLv3 header on 100% of command files")
    void testLicenseHeaders() throws IOException {
        Path commandDir = Path.of("src/main/java/net/dasik/social/api/command");
        try (Stream<Path> files = Files.walk(commandDir)) {
            files.filter(p -> p.toString().endsWith(".java")).forEach(path -> {
                try {
                    List<String> lines = Files.readAllLines(path);
                    assertFalse(lines.isEmpty(), "File must not be empty: " + path);
                    String firstLine = lines.get(0).trim();
                    assertEquals("// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3", firstLine,
                            "Missing or invalid copyright header in " + path.getFileName());
                } catch (IOException e) {
                    fail("Failed to read file: " + path);
                }
            });
        }
    }

    @Test
    @DisplayName("Verify @DasikApiStatus.Public annotation on all public command API classes")
    void testApiStatusAnnotations() throws IOException {
        Path commandDir = Path.of("src/main/java/net/dasik/social/api/command");
        try (Stream<Path> files = Files.walk(commandDir)) {
            files.filter(p -> p.toString().endsWith(".java")).forEach(path -> {
                try {
                    String content = Files.readString(path);
                    assertTrue(content.contains("@DasikApiStatus.Public"),
                            "Missing @DasikApiStatus.Public in " + path.getFileName());
                } catch (IOException e) {
                    fail("Failed to read file: " + path);
                }
            });
        }
    }
}
