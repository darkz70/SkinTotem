package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.item.Items;

public class SkinTotemClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SkinTotemMod.LOGGER.info("[SkinTotem] Client initialized");

        // No mixins — BIIR is the official Fabric API for custom item rendering.
        // Compatible with fabric-renderer-indigo and all other renderers.
        BuiltinItemRendererRegistry.INSTANCE.register(
            Items.TOTEM_OF_UNDYING,
            new TotemItemRenderer()
        );
    }
}
