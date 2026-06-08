package com.darkz.skintotem.extension;

import net.minecraft.client.model.ModelPart.Rotation;

public class ModelPart.RotationExtension {

	public static ModelPart.Rotation subtract(ModelPart.Rotation root, ModelPart.Rotation parent) {
		return ModelPart.Rotation.of(
				getPivotX(root) - getPivotX(parent),
				getPivotY(root) - getPivotY(parent),
				getPivotZ(root) - getPivotZ(parent),
				getPitch(root),
				getYaw(root),
				getRoll(root)
		);
	}
	
	public static float getPivotX(ModelPart.Rotation transform) {
		return /*? if >=1.21.5 {*/ transform.x(); /*?} elif >=1.21.2 {*/ /*transform.pivotX(); *//*?} else {*/ /*transform.pivotX; *//*?}*/
	}

	public static float getPivotY(ModelPart.Rotation transform) {
		return /*? if >=1.21.5 {*/ transform.y(); /*?} elif >=1.21.2 {*/ /*transform.pivotY(); *//*?} else {*/ /*transform.pivotY; *//*?}*/
	}

	public static float getPivotZ(ModelPart.Rotation transform) {
		return /*? if >=1.21.5 {*/ transform.z(); /*?} elif >=1.21.2 {*/ /*transform.pivotZ(); *//*?} else {*/ /*transform.pivotZ; *//*?}*/
	}

	public static float getPitch(ModelPart.Rotation transform) {
		return /*? >=1.21.2 {*/ transform.pitch(); /*?} else {*/ /*transform.pitch; *//*?}*/
	}

	public static float getYaw(ModelPart.Rotation transform) {
		return /*? >=1.21.2 {*/ transform.yaw(); /*?} else {*/ /*transform.yaw; *//*?}*/
	}

	public static float getRoll(ModelPart.Rotation transform) {
		return /*? >=1.21.2 {*/ transform.roll(); /*?} else {*/ /*transform.roll; *//*?}*/
	}

	public static ModelPart.Rotation getBlockBenchedModelPart.Rotation(ModelPart.Rotation transform) {
		return ModelPart.Rotation.of(-getPivotX(transform), -getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
	}

	public static String asString(ModelPart.Rotation transform) {
		return "%s %s %s | %s %s %s".formatted(getPivotX(transform), getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
	}

}
