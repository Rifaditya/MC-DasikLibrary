package net.dasik.social.api.enchantment;

import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;

public class DynamicEnchantmentManager {

    /**
     * Caps a specific enchantment on an ItemEnchantments.Mutable map to the maximum level allowed by a GameRule.
     * Use this in Mixins (e.g. AnvilMenu, EnchantmentHelper) to enforce dynamic limits.
     *
     * @param level       The current Level (to access the GameRule value)
     * @param mutable     The mutable enchantments map to check and modify
     * @param enchantment The registry key of the enchantment to cap
     * @param maxLevelRule The GameRule containing the maximum level
     */
    public static void capEnchantmentLevel(Level level, ItemEnchantments.Mutable mutable, net.minecraft.resources.Identifier enchantmentId, GameRule<Integer> maxLevelRule) {
        if (level == null || mutable == null || enchantmentId == null || maxLevelRule == null) return;
        
        int maxLevel = DynamicGameRuleManager.getInt(level, maxLevelRule);

        for (var entry : mutable.keySet()) {
            if (entry.is(enchantmentId)) {
                if (mutable.getLevel(entry) > maxLevel) {
                    mutable.set(entry, maxLevel);
                }
            }
        }
    }
}
