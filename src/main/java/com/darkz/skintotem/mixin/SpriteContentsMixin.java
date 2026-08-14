package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpriteContents.class)
public class SpriteContentsMixin {

	@Unique
	private static final String mySkinTotem$TEXT = "Wait! This crash was caused by the \"skin-totem\" mod SPECIFICALLY to prevent a crash via drivers. This crash was made to make debugging this unexpected error easier. Someone (maybe \"skin-totem\") just pushed closed sprite to upload and this shouldn't happen! Please report this crash-report to \"skin-totem\" issue tracker: https://github.com/LopyMine/SkinTotem/issues";

	@WrapOperation(
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/platform/NativeImage;upload(IIIIIIIZZ)V"
			),
			method = "upload(IIII[Lcom/mojang/blaze3d/platform/NativeImage;)V"
	)
	private void validateImageBeforeUpload(NativeImage instance, int level, int offsetX, int offsetY, int unpackSkipPixels, int unpackSkipRows, int width, int height, boolean mipmap, boolean close, Operation<Void> original) {
		if (instance.pixels == 0L) {
			throw new IllegalArgumentException(mySkinTotem$TEXT);
		}
		original.call(instance, level, offsetX, offsetY, unpackSkipPixels, unpackSkipRows, width, height, mipmap, close);
	}

}
