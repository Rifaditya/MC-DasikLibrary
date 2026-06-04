/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Language.java (Snapshot 10)
 */
package net.dasik.social.mixin;

import java.io.InputStream;
import java.util.Map;
import java.util.function.BiConsumer;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.locale.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Language.class)
public abstract class LanguageMixin {
    @Inject(method = "loadFromJson", at = @At("RETURN"))
    private static void injectDynamicGameRuleTranslations(InputStream stream, BiConsumer<String, String> output, CallbackInfo ci) {
        Map<String, String> generatedTranslations = DynamicGameRuleManager.getGeneratedTranslations();
        for (Map.Entry<String, String> entry : generatedTranslations.entrySet()) {
            output.accept(entry.getKey(), entry.getValue());
        }
    }
}
