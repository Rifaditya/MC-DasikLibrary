/*
 * Core Sovereign Engineering - Dasik Library
 * Verified against: FabricLoader.java (26.1.2)
 */
package net.dasik.social.api.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|GUI");

    /**
     * Dynamically resolves the ModMenu ConfigScreenFactory for a mod if Cloth Config is present,
     * preventing compile/runtime class linkage crashes when Cloth Config is absent.
     *
     * @param modId The ID of the calling mod.
     * @param helperClassName The fully qualified name of the isolated ClothConfigScreenHelper.
     * @param methodName The name of the static factory method (usually "createFactory").
     * @return The ConfigScreenFactory if loaded; otherwise a fallback dummy factory (parent -> null).
     */
    public static ConfigScreenFactory<?> getOptionalFactory(
            String modId,
            String helperClassName,
            String methodName) {
        
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            try {
                Class<?> helperClass = Class.forName(helperClassName);
                java.lang.reflect.Method method = helperClass.getMethod(methodName);
                return (ConfigScreenFactory<?>) method.invoke(null);
            } catch (ClassNotFoundException e) {
                LOGGER.warn("[{}] Cloth Config screen helper class {} not found.", modId, helperClassName);
            } catch (NoSuchMethodException e) {
                LOGGER.error("[{}] Screen helper does not implement method {}().", modId, methodName, e);
            } catch (Exception e) {
                LOGGER.error("[{}] Failed to load optional configuration factory.", modId, e);
            }
        }
        return parent -> null;
    }

    /**
     * Dynamically resolves the ModMenu ConfigScreenFactory for a mod if YACL is present,
     * preventing compile/runtime class linkage crashes when YACL is absent.
     *
     * @param modId The ID of the calling mod.
     * @param helperClassName The fully qualified name of the isolated YaclScreenHelper.
     * @param methodName The name of the static factory method (usually "createScreen").
     * @return The ConfigScreenFactory if loaded; otherwise a fallback dummy factory (parent -> null).
     */
    public static ConfigScreenFactory<?> getOptionalYaclFactory(
            String modId,
            String helperClassName,
            String methodName) {
        
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
            try {
                Class<?> helperClass = Class.forName(helperClassName);
                java.lang.reflect.Method method = helperClass.getMethod(methodName);
                return (ConfigScreenFactory<?>) method.invoke(null);
            } catch (ClassNotFoundException e) {
                LOGGER.warn("[{}] YACL screen helper class {} not found.", modId, helperClassName);
            } catch (NoSuchMethodException e) {
                LOGGER.error("[{}] Screen helper does not implement method {}().", modId, methodName, e);
            } catch (Exception e) {
                LOGGER.error("[{}] Failed to load optional YACL configuration factory.", modId, e);
            }
        }
        return parent -> null;
    }
}
