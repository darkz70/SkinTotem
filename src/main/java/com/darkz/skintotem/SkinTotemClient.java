package com.darkz.skintotem;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkinTotemClient implements ClientModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("skintotem-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("SkinTotem client initialized");
    }
}
