package spichka.skintotem.skin;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkinCache {

    private static final Map<UUID, Identifier> CACHE = new HashMap<>();

    public static Identifier get(UUID uuid) {
        return CACHE.getOrDefault(
                uuid,
                Identifier.of("minecraft", "textures/entity/steve.png")
        );
    }

    public static void put(UUID uuid, Identifier skin) {
        CACHE.put(uuid, skin);
    }
}
