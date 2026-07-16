package com.darkz.skintotem.extension;

import net.minecraft.client.render.model.json.*;

public class ModModelTransformationExtension {
	
	public static Transformation getTl(ModModelTransformation transform) {
		return /*? >=1.21.4 {*/ transform.thirdPersonLeftHand(); /*?} else {*/ /*transform.thirdPersonLeftHand; *//*?}*/
	}

	public static Transformation getTr(ModModelTransformation transform) {
		return /*? >=1.21.4 {*/ transform.thirdPersonRightHand(); /*?} else {*/ /*transform.thirdPersonRightHand; *//*?}*/
	}

	public static Transformation getFl(ModModelTransformation transform) {
		return /*? >=1.21.4 {*/ transform.firstPersonLeftHand(); /*?} else {*/ /*transform.firstPersonLeftHand; *//*?}*/
	}

	public static Transformation getFr(ModModelTransformation transform) {
		return /*? >=1.21.4 {*/ transform.firstPersonRightHand(); /*?} else {*/ /*transform.firstPersonRightHand; *//*?}*/
	}

	public static Transformation getHead(ModModelTransformation transform) {
		return /*? >=1.21.4 {*/ transform.head(); /*?} else {*/ /*transform.head; *//*?}*/
	}

	public static Transformation getGui(ModModelTransformation transform) {
		return /*? >=1.21.4 {*/ transform.gui(); /*?} else {*/ /*transform.gui; *//*?}*/
	}

	public static Transformation getGround(ModModelTransformation transform) {
		return /*? >=1.21.4 {*/ transform.ground(); /*?} else {*/ /*transform.ground; *//*?}*/
	}

	public static Transformation getFixed(ModModelTransformation transform) {
		return /*? >=1.21.4 {*/ transform.fixed(); /*?} else {*/ /*transform.fixed; *//*?}*/
	}

	//? if >=1.21.9 {
	public static Transformation getOnShelf(ModModelTransformation transform) {
		return transform.fixedFromBottom();
	}
	//?}

//	public static ModModelTransformation getBlockBenchedModModelTransformation(ModModelTransformation transform) {
//		return ModModelTransformation.of(-getPivotX(transform), -getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
//	}
//
//	public static String asString(ModModelTransformation transform) {
//		return "%s %s %s | %s %s %s".formatted(getPivotX(transform), getPivotY(transform), getPivotZ(transform), getPitch(transform), getYaw(transform), getRoll(transform));
//	}

}
