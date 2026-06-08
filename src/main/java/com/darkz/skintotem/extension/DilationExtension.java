package com.darkz.skintotem.extension;

import net.minecraft.client.model.CubeDeformation;

import com.darkz.skintotem.mixin.accessor.CubeDeformationAccessor;

public class CubeDeformationExtension {

	public static float getRadiusX(CubeDeformation dilation) {
		return ((CubeDeformationAccessor) dilation).getRadiusX();
	}

	public static float getRadiusY(CubeDeformation dilation) {
		return ((CubeDeformationAccessor) dilation).getRadiusY();
	}

	public static float getRadiusZ(CubeDeformation dilation) {
		return ((CubeDeformationAccessor) dilation).getRadiusZ();
	}
}
