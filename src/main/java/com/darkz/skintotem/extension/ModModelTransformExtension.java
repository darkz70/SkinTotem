package com.darkz.skintotem.extension;

import net.minecraft.client.model.ModelTransform;
import net.minecraft.util.math.Vec3d;

public class ModModelTransformExtension {

	/**
	 * Computes the pivot of {@code root} relative to {@code parent}.
	 * <p>
	 * BlockBench stores every group's "origin" in absolute model space, so the naive
	 * component-wise difference of two pivots only gives the correct relative pivot
	 * when the parent has no rotation. If the parent bone is rotated (e.g. a bent arm,
	 * a bent leg, a tilted head/body on a custom doll), the raw world-space delta must
	 * first be rotated into the parent's own (rotated) local space - otherwise the
	 * child part ends up rendered in the wrong place, appearing detached/misaligned
	 * from the part it should be attached to.
	 * <p>
	 * Minecraft applies a ModelPart's own rotation in the order roll (Z) -> yaw (Y) ->
	 * pitch (X), so to go the other way (world-space delta -> parent-local space) we
	 * undo that in reverse: -roll, then -yaw, then -pitch.
	 */
	public static ModelTransform subtract(ModelTransform root, ModelTransform parent) {
		Vec3d delta = new Vec3d(
				getPivotX(root) - getPivotX(parent),
				getPivotY(root) - getPivotY(parent),
				getPivotZ(root) - getPivotZ(parent)
		);

		float parentRoll  = (float) Math.toRadians(getRoll(parent));
		float parentYaw   = (float) Math.toRadians(getYaw(parent));
		float parentPitch = (float) Math.toRadians(getPitch(parent));

		if (parentRoll != 0.0F) {
			delta = delta.rotateZ(-parentRoll);
		}
		if (parentYaw != 0.0F) {
			delta = delta.rotateY(-parentYaw);
		}
		if (parentPitch != 0.0F) {
			delta = delta.rotateX(-parentPitch);
		}

		return ModelTransform.of(
				(float) delta.x,
				(float) delta.y,
				(float) delta.z,
				getPitch(root),
				getYaw(root),
				getRoll(root)
		);
	}
	
	public static float getPivotX(ModelTransform transform) {
		return /*? if >=1.21.5 {*/ transform.x(); /*?} elif >=1.21.2 {*/ /*transform.pivotX(); *//*?} else {*/ /*transform.pivotX; *//*?}*/
	}

	public static float getPivotY(ModelTransform transform) {
		return /*? if >=1.21.5 {*/ transform.y(); /*?} elif >=1.21.2 {*/ /*transform.pivotY(); *//*?} else {*/ /*transform.pivotY; *//*?}*/
	}

	public static float getPivotZ(ModelTransform transform) {
		return /*? if >=1.21.5 {*/ transform.z(); /*?} elif >=1.21.2 {*/ /*transform.pivotZ(); *//*?} else {*/ /*transform.pivotZ; *//*?}*/
	}

	public static float getPitch(ModelTransform transform) {
		return /*? >=1.21.2 {*/ transform.pitch(); /*?} else {*/ /*transform.pitch; *//*?}*/
	}

	public static float getYaw(ModelTransform transform) {
		return /*? >=1.21.2 {*/ transform.yaw(); /*?} else {*/ /*transform.yaw; *//*?}*/
	}

	public static float getRoll(ModelTransform transform) {
		return /*? >=1.21.2 {*/ transform.roll(); /*?} else {*/ /*transform.roll; *//*?}*/
	}

	public static ModelTransform getBlockBenchedModModelTransform(ModelTransform transform) {
		return ModelTransform.of(-getPivotX(transform), -getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
	}

	public static String asString(ModelTransform transform) {
		return "%s %s %s | %s %s %s".formatted(getPivotX(transform), getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
	}

}
