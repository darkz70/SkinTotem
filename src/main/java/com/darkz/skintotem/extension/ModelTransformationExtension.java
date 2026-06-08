package com.darkz.skintotem.extension;

import net.minecraft.client.renderer.block.model.*;

public class ModelPart.RotationationExtension {
	
	public static ItemTransform getTl(ModelPart.Rotationation transform) {
		return /*? >=1.21.4 {*/ transform.thirdPersonLeftHand(); /*?} else {*/ /*transform.thirdPersonLeftHand; *//*?}*/
	}

	public static ItemTransform getTr(ModelPart.Rotationation transform) {
		return /*? >=1.21.4 {*/ transform.thirdPersonRightHand(); /*?} else {*/ /*transform.thirdPersonRightHand; *//*?}*/
	}

	public static ItemTransform getFl(ModelPart.Rotationation transform) {
		return /*? >=1.21.4 {*/ transform.firstPersonLeftHand(); /*?} else {*/ /*transform.firstPersonLeftHand; *//*?}*/
	}

	public static ItemTransform getFr(ModelPart.Rotationation transform) {
		return /*? >=1.21.4 {*/ transform.firstPersonRightHand(); /*?} else {*/ /*transform.firstPersonRightHand; *//*?}*/
	}

	public static ItemTransform getHead(ModelPart.Rotationation transform) {
		return /*? >=1.21.4 {*/ transform.head(); /*?} else {*/ /*transform.head; *//*?}*/
	}

	public static ItemTransform getGui(ModelPart.Rotationation transform) {
		return /*? >=1.21.4 {*/ transform.gui(); /*?} else {*/ /*transform.gui; *//*?}*/
	}

	public static ItemTransform getGround(ModelPart.Rotationation transform) {
		return /*? >=1.21.4 {*/ transform.ground(); /*?} else {*/ /*transform.ground; *//*?}*/
	}

	public static ItemTransform getFixed(ModelPart.Rotationation transform) {
		return /*? >=1.21.4 {*/ transform.fixed(); /*?} else {*/ /*transform.fixed; *//*?}*/
	}

	//? if >=1.21.9 {
	public static ItemTransform getOnShelf(ModelPart.Rotationation transform) {
		return transform.fixedFromBottom();
	}
	//?}

//	public static ModelPart.Rotationation getBlockBenchedModelPart.Rotationation(ModelPart.Rotationation transform) {
//		return ModelPart.Rotationation.of(-getPivotX(transform), -getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
//	}
//
//	public static String asString(ModelPart.Rotationation transform) {
//		return "%s %s %s | %s %s %s".formatted(getPivotX(transform), getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
//	}

}
