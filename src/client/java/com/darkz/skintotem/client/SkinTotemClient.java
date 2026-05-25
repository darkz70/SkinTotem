package com.darkz.skintotem.client;

import com.darkz.skintotem.SkinTotemMod;
import net.fabricmc.api.ClientModInitializer;

public class SkinTotemClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SkinTotemMod.LOGGER.info("[SkinTotem] Client initialized");
    }
}
