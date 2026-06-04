/*
 * Dasik Library
 * Verified against: Codec.java (26.1.2+)
 */
package net.dasik.social.api.genetics;

import java.util.Locale;
import java.util.Map;

public record GeneticsConfig(
    Map<String, TraitConfig> traits,
    Map<String, Map<String, MutationRule>> personalityMutations // personality name -> (traitId -> MutationRule)
) {
    public MutationRule getMutationRule(String personality, String traitId) {
        Map<String, MutationRule> pm = personalityMutations.get(personality.toLowerCase(Locale.ROOT));
        if (pm != null) {
            MutationRule rule = pm.get(traitId);
            if (rule != null) return rule;
        }
        return new MutationRule("constant", 0f, 0f);
    }
}
