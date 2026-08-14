package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig.QuickPlayData;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import com.darkz.skintotem.loader.SkinTotemLoader;

import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.gui.screen.WelcomeScreen;

import java.util.List;
import java.util.function.Function;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@WrapOperation(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setInitialScreen(Lcom/mojang/realmsclient/client/RealmsClient;Lnet/minecraft/server/packs/resources/ReloadInstance;Lnet/minecraft/client/main/GameConfig$QuickPlayData;)V"),
			//? if fabric {
			method = "<init>"
			//?} else {
			/*method = {"lambda$new$3", "lambda$new$4"}
			*///?}
	)
	private void addMTDHelloScreen(Minecraft client, RealmsClient realmsClient, ReloadInstance resourceReload, QuickPlayData quickPlay, Operation<Void> original) {
		Runnable runnable = () -> original.call(client, realmsClient, resourceReload, quickPlay);

		SkinTotemConfig config = SkinTotemConfig.getInstance();
		if (config.isFirstRun()) {
			client.setScreen(new WelcomeScreen(runnable));
			if (!SkinTotemLoader.isDevelopmentEnvironment()) {
				config.setFirstRun(false);
			}
			config.save();
		} else {
			runnable.run();
		}
	}

}
