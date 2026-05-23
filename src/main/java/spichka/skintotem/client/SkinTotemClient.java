package spichka.skintotem.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import spichka.skintotem.SkinTotemMod;

public class SkinTotemClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(
                SkinTotemMod.TOTEM,
                new SkinTotemItemRenderer()
        );
    }
}
