package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.*;
import net.minecraft.client.RunArgs.QuickPlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.resource.ResourceReload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import net.fabricmc.loader.api.FabricLoader;

import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.config.SkinTotemModConfig;
import com.darkz.skintotem.gui.screen.WelcomeScreen;

import java.util.List;
import java.util.function.Function;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

	//? if >=1.21 {
	@Inject(at = @At("HEAD"), method = "createInitScreens")
	private void addMTDHelloScreen(List<Function<Runnable, Screen>> list, /*? if >=1.21.6 {*/ CallbackInfoReturnable<Boolean> /*?} else {*/ /*CallbackInfo *//*?}*/ ci) {
		SkinTotemModConfig config = SkinTotemModConfig.getInstance();
		if (config.isFirstRun()) {
			list.add(WelcomeScreen::new);
			if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
				config.setFirstRun(false);
			}
			config.save();
		}
	}
	//?} else {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;onInitFinished(Lnet/minecraft/client/realms/RealmsClient;Lnet/minecraft/resource/ResourceReload;Lnet/minecraft/client/RunArgs$QuickPlay;)V"), method = "<init>")
	private void addMTDHelloScreen(Minecraft client, RealmsClient realmsClient, ResourceReload resourceReload, QuickPlay quickPlay, Operation<Void> original) {
		Runnable runnable = () -> original.call(client, realmsClient, resourceReload, quickPlay);

		SkinTotemModConfig config = SkinTotemModConfig.getInstance();
		if (config.isFirstRun()) {
			client.setScreen(new WelcomeScreen(runnable));
			if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
				config.setFirstRun(false);
			}
			config.save();
		} else {
			runnable.run();
		}
	}
	*///?}

}
