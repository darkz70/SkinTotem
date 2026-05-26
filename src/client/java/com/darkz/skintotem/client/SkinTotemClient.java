package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.item.Items;

public class SkinTotemClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SkinTotemMod.LOGGER.info("[SkinTotem] Client initialized");

        // Register our custom renderer for Totem of Undying.
        // This replaces the default item model renderer with our own —
        // no mixins required, fully compatible with fabric-renderer-indigo.
        BuiltinItemRendererRegistry.INSTANCE.register(
            Items.TOTEM_OF_UNDYING,
            new TotemItemRenderer()
        );
    }
}
