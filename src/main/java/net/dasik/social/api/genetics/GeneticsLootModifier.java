/*
 * Dasik Library
 * Verified against: LivingEntity.java (26.2+)
 */
package net.dasik.social.api.genetics;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface GeneticsLootModifier {
    /**
     * Modifies a dropped item stack based on the entity's genetics.
     *
     * @param entity   The living entity that died.
     * @param genetics The genetics of the entity.
     * @param stack    The item stack that is generated from the loot table.
     * @param random   The random source.
     * @return The modified ItemStack, or ItemStack.EMPTY if it should not drop.
     */
    ItemStack modifyDrop(LivingEntity entity, EntityGenetics genetics, ItemStack stack, RandomSource random);
}
