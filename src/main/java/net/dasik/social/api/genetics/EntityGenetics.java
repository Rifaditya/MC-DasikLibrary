/*
 * Dasik Library
 * Verified against: UUIDUtil.java (26.1.2+)
 */
package net.dasik.social.api.genetics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;

public record EntityGenetics(
    Optional<UUID> parent1Uuid,
    Optional<UUID> parent2Uuid,
    boolean inbred,
    boolean traitsRolled,
    Map<String, Float> traits
) {
    public static final EntityGenetics DEFAULT = new EntityGenetics(
        Optional.empty(),
        Optional.empty(),
        false,
        false,
        Map.of()
    );

    public static final Codec<EntityGenetics> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UUIDUtil.CODEC.optionalFieldOf("parent1Uuid").forGetter(EntityGenetics::parent1Uuid),
        UUIDUtil.CODEC.optionalFieldOf("parent2Uuid").forGetter(EntityGenetics::parent2Uuid),
        Codec.BOOL.optionalFieldOf("inbred", false).forGetter(EntityGenetics::inbred),
        Codec.BOOL.optionalFieldOf("traitsRolled", false).forGetter(EntityGenetics::traitsRolled),
        Codec.unboundedMap(Codec.STRING, Codec.FLOAT).optionalFieldOf("traits", Map.of()).forGetter(EntityGenetics::traits)
    ).apply(instance, EntityGenetics::new));
}
