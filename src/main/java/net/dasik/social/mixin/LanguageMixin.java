package net.dasik.social.mixin;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.locale.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;
import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(Language.class)
public abstract class LanguageMixin {

    /**
     * Injects into the Language loading phase. We append our generated Language
     * entries
     * into the output consumer map that Minecraft builds when loading en_us.json.
     */
    @Inject(method = "loadFromJson", at = @At("RETURN"))
    private static void injectDynamicGameRuleTranslations(InputStream stream, BiConsumer<String, String> output,
            CallbackInfo ci) {
        Map<String, String> generatedTranslations = DynamicGameRuleManager.getGeneratedTranslations();

        // Push all generated translations into the active language map
        for (Map.Entry<String, String> entry : generatedTranslations.entrySet()) {
            output.accept(entry.getKey(), entry.getValue());
        }
    }
}
