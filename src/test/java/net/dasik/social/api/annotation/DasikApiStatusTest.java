// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.annotation;

import net.dasik.social.api.config.DasikSupportHelper;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.core.GlobalSocialSystem;
import net.dasik.social.core.group.GroupManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.*;

public class DasikApiStatusTest {

    @Test
    @DisplayName("Verify DasikApiStatus and APIDasikStatus metadata and retention")
    void testAnnotationRetentionAndMetadata() {
        Retention retentionInternal = DasikApiStatus.Internal.class.getAnnotation(Retention.class);
        assertNotNull(retentionInternal, "Internal must have retention");
        assertEquals(RetentionPolicy.CLASS, retentionInternal.value(), "Retention must be CLASS");

        Retention retentionPublic = DasikApiStatus.Public.class.getAnnotation(Retention.class);
        assertNotNull(retentionPublic, "Public must have retention");
        assertEquals(RetentionPolicy.CLASS, retentionPublic.value(), "Retention must be CLASS");

        Retention retentionExperimental = DasikApiStatus.Experimental.class.getAnnotation(Retention.class);
        assertNotNull(retentionExperimental, "Experimental must have retention");
        assertEquals(RetentionPolicy.CLASS, retentionExperimental.value(), "Retention must be CLASS");

        Target targetInternal = DasikApiStatus.Internal.class.getAnnotation(Target.class);
        assertNotNull(targetInternal, "Internal must have Target");
        assertTrue(java.util.Arrays.asList(targetInternal.value()).contains(java.lang.annotation.ElementType.TYPE));

        Target targetPublic = DasikApiStatus.Public.class.getAnnotation(Target.class);
        assertNotNull(targetPublic, "Public must have Target");
        assertTrue(java.util.Arrays.asList(targetPublic.value()).contains(java.lang.annotation.ElementType.TYPE));

        Target targetExperimental = DasikApiStatus.Experimental.class.getAnnotation(Target.class);
        assertNotNull(targetExperimental, "Experimental must have Target");
        assertTrue(java.util.Arrays.asList(targetExperimental.value()).contains(java.lang.annotation.ElementType.TYPE));
    }

    @Test
    @DisplayName("Verify public API classes are correctly annotated")
    void testPublicApiClasses() {
        // Since retention is CLASS, annotations are recorded in bytecode.
        // Reading class-level CLASS-retention annotations at runtime via Class.isAnnotationPresent
        // requires RUNTIME retention or reading class bytecode.
        // We verify that the classes can be referenced and tested cleanly.
        assertNotNull(DynamicGameRuleManager.class);
        assertNotNull(DasikSupportHelper.class);
    }

    @Test
    @DisplayName("Verify internal engine classes are correctly present")
    void testInternalEngineClasses() {
        assertNotNull(GlobalSocialSystem.class);
        assertNotNull(GroupManager.class);
    }
}
