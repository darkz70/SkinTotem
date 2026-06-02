package com.darkz.skintotem.extension;

import net.minecraft.client.model.Dilation;

import com.darkz.skintotem.mixin.accessor.DilationAccessor;

public class DilationExtension {

	public static float getRadiusX(Dilation dilation) {
		return ((DilationAccessor) dilation).getRadiusX();
	}

	public static float getRadiusY(Dilation dilation) {
		return ((DilationAccessor) dilation).getRadiusY();
	}

	public static float getRadiusZ(Dilation dilation) {
		return ((DilationAccessor) dilation).getRadiusZ();
	}
}
