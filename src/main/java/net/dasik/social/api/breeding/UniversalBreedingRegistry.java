package net.dasik.social.api.breeding;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class UniversalBreedingRegistry {
    private static final Map<EntityType<?>, BreedingDefinition> REGISTRY = new HashMap<>();

    public static void register(EntityType<?> entity, Ingredient food, int cooldown) {
        REGISTRY.put(entity, new BreedingDefinition(food, cooldown));
    }

    public static void register(EntityType<?> entity, Item food, int cooldown) {
        register(entity, Ingredient.of(food), cooldown);
    }

    public static boolean isBreedable(EntityType<?> type) {
        return REGISTRY.containsKey(type);
    }

    public static boolean isFood(EntityType<?> type, ItemStack stack) {
        BreedingDefinition def = REGISTRY.get(type);
        return def != null && def.food.test(stack);
    }

    public static int getCooldown(EntityType<?> type) {
        BreedingDefinition def = REGISTRY.get(type);
        return def != null ? def.cooldown : 6000;
    }

    private record BreedingDefinition(Ingredient food, int cooldown) {
    }
}
