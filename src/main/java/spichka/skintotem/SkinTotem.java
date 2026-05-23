package spichka.skintotem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkinTotem implements ModInitializer {
    public static final String MOD_ID = "skintotem";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static boolean firstTick = true;

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (firstTick) {
                    SimpleTextureLoader.loadTextures();
                    firstTick = false;
                }
            });
        }
    }
}
