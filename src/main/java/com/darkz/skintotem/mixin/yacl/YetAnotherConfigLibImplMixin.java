package com.darkz.skintotem.mixin.yacl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.impl.YetAnotherConfigLibImpl;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.utils.mixin.yacl.BetterYACLScreenConfig;
import com.darkz.skintotem.yacl.custom.screen.*;

@SuppressWarnings("UnstableApiUsage")
@Mixin(YetAnotherConfigLibImpl.class)
public class YetAnotherConfigLibImplMixin implements BetterYACLScreenConfig {

	@Unique
	private boolean skinTotem$enabled;

	@Dynamic
	@ModifyReturnValue(at = @At("RETURN"), method = "generateScreen", remap = false)
	private Screen swapScreen(Screen original, @Local(argsOnly = true) Screen parent) {
		if (!this.skinTotem$enabled) {
			return original;
		}
		return new SkinTotemYACLScreen(((YetAnotherConfigLib) this), parent);
	}


	@Override
	public YetAnotherConfigLib skinTotem$enable() {
		this.skinTotem$enabled = true;
		return ((YetAnotherConfigLib) this);
	}
}
