package com.darkz.skintotem.mixin.yacl;

import com.llamalad7.mixinextras.injector.ifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.impl.YetAnotherConfigLibImpl;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.utils.mixin.yacl.BetterYACLScreenConfig;
import com.darkz.skintotem.yacl.custom.screen.*;

@Pseudo
@Mixin(YetAnotherConfigLibImpl.class)
public class YetAnotherConfigLibImplMixin implements BetterYACLScreenConfig {

	@Unique
	private boolean enabled;

	@Dynamic
	@ifyReturnValue(at = @At("RETURN"), method = "generateScreen")
	private Screen swapScreen(Screen original, @Local(argsOnly = true) Screen parent) {
		if (!this.enabled) {
			return original;
		}
		return new SkinTotemYACLScreen(((YetAnotherConfigLib) this), parent);
	}


	@Override
	public YetAnotherConfigLib skinTotem$enable() {
		this.enabled = true;
		return ((YetAnotherConfigLib) this);
	}
}
