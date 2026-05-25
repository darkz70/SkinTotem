package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SkinTotemClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SkinTotemMod.LOGGER.info("[SkinTotem] Client initialized");
        // SkinTotemResourceManager handles the rest via mixin
    }
}
