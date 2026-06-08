package com.darkz.skintotem.utils;

public class LightningUtils {

	public static void disable3dLighting() {
		//? if >=1.21.6 {
		net.minecraft.client.Minecraft.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(net.minecraft.client.render.DiffuseLighting.ITEMS_FLAT);
		//?} else {
		/*net.minecraft.client.render.DiffuseLighting.disableGuiDepthLighting();
		 *///?}
	}

	public static void enable3dLighting() {
		//? if >=1.21.6 {
		net.minecraft.client.Minecraft.getInstance().gameRenderer.getDiffuseLighting().setShaderLights(net.minecraft.client.render.DiffuseLighting.ITEMS_3D);
		//?} else {
		/*net.minecraft.client.render.DiffuseLighting.enableGuiDepthLighting();
		*///?}
	}
}
