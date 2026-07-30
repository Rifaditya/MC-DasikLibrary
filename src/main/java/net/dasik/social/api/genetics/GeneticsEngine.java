/*
 * Zenith Sovereign Engineering - Dasik Library
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
// Verified against: LivingEntity.java, AttributeModifier.java (26.1.2+)
package net.dasik.social.api.genetics;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.dasik.social.DasikLibraryMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class GeneticsEngine {
    public static final AttachmentType<EntityGenetics> GENETICS = AttachmentRegistry.createPersistent(
            Identifier.fromNamespaceAndPath(DasikLibraryMod.MOD_ID, "genetics"),
            EntityGenetics.CODEC);

    public static EntityGenetics getGenetics(LivingEntity entity) {
        return entity.getAttachedOrCreate(GENETICS, () -> EntityGenetics.DEFAULT);
    }

    public static void setGenetics(LivingEntity entity, EntityGenetics genetics) {
        entity.setAttached(GENETICS, genetics);
    }

    public static void rollInitialStats(LivingEntity entity, String variantKey) {
        GeneticsConfig config = EntityGeneticsRegistry.getConfig(entity.getType());
        if (config == null) return;

        EntityGenetics data = getGenetics(entity);
        if (data.traitsRolled()) {
            return;
        }

        UUID uuid = entity.getUUID();
        long seed = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
        RandomSource rand = RandomSource.create(seed);

        Map<String, Float> rolledTraits = new HashMap<>();
        for (Map.Entry<String, TraitConfig> entry : config.traits().entrySet()) {
            String traitId = entry.getKey();
            MutationRule rule = config.getMutationRule(variantKey, traitId);
            float rolledValue = rule.roll(rand);
            float min = GeneticsLimitRegistry.getMin(entity, traitId, entry.getValue().minLimit());
            float max = GeneticsLimitRegistry.getMax(entity, traitId, entry.getValue().maxLimit());
            rolledValue = Math.max(min, Math.min(max, rolledValue));
            rolledTraits.put(traitId, rolledValue);
        }

        setGenetics(entity, new EntityGenetics(
            data.parent1Uuid(),
            data.parent2Uuid(),
            data.inbred(),
            true,
            rolledTraits
        ));

        applyGeneticsModifiers(entity);
    }

    public static void inheritGenetics(LivingEntity baby, LivingEntity parent1, LivingEntity parent2, String babyVariantKey) {
        GeneticsConfig config = EntityGeneticsRegistry.getConfig(baby.getType());
        if (config == null) return;

        // Ensure parent stats are rolled
        if (!getGenetics(parent1).traitsRolled()) {
            rollInitialStats(parent1, "normal");
        }
        if (!getGenetics(parent2).traitsRolled()) {
            rollInitialStats(parent2, "normal");
        }

        EntityGenetics d1 = getGenetics(parent1);
        EntityGenetics d2 = getGenetics(parent2);

        boolean inbred = checkInbreeding(parent1, parent2, d1, d2);
        RandomSource rand = baby.level().getRandom();

        Map<String, Float> babyTraits = new HashMap<>();
        for (Map.Entry<String, TraitConfig> entry : config.traits().entrySet()) {
            String traitId = entry.getKey();
            TraitConfig trait = entry.getValue();

            float p1Val = d1.traits().getOrDefault(traitId, 0.0f);
            float p2Val = d2.traits().getOrDefault(traitId, 0.0f);

            MutationRule rule = config.getMutationRule(babyVariantKey, traitId);
            float mutation = rule.roll(rand);

            float p1Reconstructed = p1Val;
            float p2Reconstructed = p2Val;

            if (!inbred) {
                if (d1.inbred()) {
                    p1Reconstructed = (p1Val + trait.inbreedingPenaltyFlat()) / trait.inbreedingMultiplier();
                }
                if (d2.inbred()) {
                    p2Reconstructed = (p2Val + trait.inbreedingPenaltyFlat()) / trait.inbreedingMultiplier();
                }
            }

            float avg = (p1Reconstructed + p2Reconstructed + mutation) / 3.0f;
            float finalStat;

            if (inbred) {
                finalStat = avg * trait.inbreedingMultiplier() - trait.inbreedingPenaltyFlat();
            } else {
                finalStat = avg;
            }

            float min = GeneticsLimitRegistry.getMin(baby, traitId, trait.minLimit());
            float max = GeneticsLimitRegistry.getMax(baby, traitId, trait.maxLimit());
            finalStat = Math.max(min, Math.min(max, finalStat));
            babyTraits.put(traitId, finalStat);
        }

        setGenetics(baby, new EntityGenetics(
            Optional.of(parent1.getUUID()),
            Optional.of(parent2.getUUID()),
            inbred,
            true,
            babyTraits
        ));

        applyGeneticsModifiers(baby);
    }

    public static void applyGeneticsModifiers(LivingEntity entity) {
        GeneticsConfig config = EntityGeneticsRegistry.getConfig(entity.getType());
        if (config == null) return;

        EntityGenetics data = getGenetics(entity);
        if (!data.traitsRolled()) return;

        for (Map.Entry<String, TraitConfig> entry : config.traits().entrySet()) {
            String traitId = entry.getKey();
            TraitConfig trait = entry.getValue();
            if (trait.attributeId().isEmpty()) continue;

            var attributeTypeOpt = BuiltInRegistries.ATTRIBUTE.get(Identifier.parse(trait.attributeId()));
            if (attributeTypeOpt.isEmpty()) continue;
            var attributeType = attributeTypeOpt.get();

            var attribute = entity.getAttribute(attributeType);
            if (attribute == null) continue;

            Identifier modifierId = Identifier.fromNamespaceAndPath(DasikLibraryMod.MOD_ID, "genetics_" + traitId);
            attribute.removeModifier(modifierId);

            float val = data.traits().getOrDefault(traitId, 0.0f);
            float min = GeneticsLimitRegistry.getMin(entity, traitId, trait.minLimit());
            float max = GeneticsLimitRegistry.getMax(entity, traitId, trait.maxLimit());
            val = Math.max(min, Math.min(max, val));

            if (val != 0.0f) {
                boolean isMaxHealth = "minecraft:generic.max_health".equals(trait.attributeId());
                float prevMaxHealth = 0f;
                float prevHealth = 0f;
                boolean wasAtFullHealth = false;

                if (isMaxHealth) {
                    prevMaxHealth = entity.getMaxHealth();
                    prevHealth = entity.getHealth();
                    wasAtFullHealth = prevHealth >= prevMaxHealth;
                }

                attribute.addPermanentModifier(new AttributeModifier(modifierId, val, trait.getOperation()));

                if (isMaxHealth) {
                    float newMaxHealth = entity.getMaxHealth();
                    if (val < 0.0f) {
                        if (entity.getHealth() > newMaxHealth) {
                            entity.setHealth(newMaxHealth);
                        }
                    } else {
                        if (wasAtFullHealth && entity.getHealth() < newMaxHealth) {
                            entity.heal(newMaxHealth - prevMaxHealth);
                        }
                    }
                }

                // Apply linked attribute modifiers (Size-Stats API)
                applyLinkedModifiers(entity, traitId, val);
            }
        }
    }

    /**
     * Applies linked attribute modifiers for a given trait.
     *
     * <p>If a trait (e.g., "size") has linked attributes registered via
     * {@link LinkedAttributeRegistry}, this method computes and applies
     * the corresponding modifiers on each linked attribute.</p>
     *
     * @param entity  The living entity.
     * @param traitId The trait ID that triggered this (e.g., "size").
     * @param traitValue The rolled trait value.
     */
    private static void applyLinkedModifiers(LivingEntity entity, String traitId, float traitValue) {
        java.util.List<LinkedAttribute> links = LinkedAttributeRegistry.getLinks(entity.getType(), traitId);
        if (links.isEmpty()) return;

        for (LinkedAttribute link : links) {
            var linkedAttrOpt = BuiltInRegistries.ATTRIBUTE.get(Identifier.parse(link.attributeId()));
            if (linkedAttrOpt.isEmpty()) continue;

            var linkedAttr = entity.getAttribute(linkedAttrOpt.get());
            if (linkedAttr == null) continue;

            Identifier linkedModId = Identifier.fromNamespaceAndPath(
                DasikLibraryMod.MOD_ID, "genetics_" + traitId + "_linked_" + link.attributeId().replace(':', '_'));
            linkedAttr.removeModifier(linkedModId);

            float modifierValue = link.computeModifier(traitValue);
            if (modifierValue != 0.0f) {
                boolean isMaxHealth = "minecraft:max_health".equals(link.attributeId())
                    || "minecraft:generic.max_health".equals(link.attributeId());
                float prevMaxHealth = 0f;
                boolean wasAtFullHealth = false;

                if (isMaxHealth) {
                    prevMaxHealth = entity.getMaxHealth();
                    wasAtFullHealth = entity.getHealth() >= prevMaxHealth;
                }

                linkedAttr.addPermanentModifier(new AttributeModifier(
                    linkedModId, modifierValue, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

                if (isMaxHealth) {
                    float newMaxHealth = entity.getMaxHealth();
                    if (modifierValue < 0.0f) {
                        if (entity.getHealth() > newMaxHealth) {
                            entity.setHealth(newMaxHealth);
                        }
                    } else {
                        if (wasAtFullHealth && entity.getHealth() < newMaxHealth) {
                            entity.heal(newMaxHealth - prevMaxHealth);
                        }
                    }
                }
            }
        }
    }

    public static boolean checkInbreeding(LivingEntity parent1, LivingEntity parent2, EntityGenetics d1, EntityGenetics d2) {
        UUID u1 = parent1.getUUID();
        UUID u2 = parent2.getUUID();

        UUID p1_1 = d1.parent1Uuid().orElse(null);
        UUID p1_2 = d1.parent2Uuid().orElse(null);

        UUID p2_1 = d2.parent1Uuid().orElse(null);
        UUID p2_2 = d2.parent2Uuid().orElse(null);

        // Parent-offspring check
        if (u1.equals(p2_1) || u1.equals(p2_2) || u2.equals(p1_1) || u2.equals(p1_2)) {
            return true;
        }

        // Sibling check
        if (p1_1 != null && (p1_1.equals(p2_1) || p1_1.equals(p2_2))) {
            return true;
        }
        if (p1_2 != null && (p1_2.equals(p2_1) || p1_2.equals(p2_2))) {
            return true;
        }

        return false;
    }
}
