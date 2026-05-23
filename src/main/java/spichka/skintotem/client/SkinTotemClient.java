package spichka.skintotem.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.item.Items;

public class SkinTotemClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(
                Items.TOTEM_OF_UNDYING,
                new SkinTotemItemRenderer()
        );
    }
}
