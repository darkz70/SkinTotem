package com.darkz.skintotem.mixin.yacl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.YetAnotherConfigLib.Builder;
import dev.isxander.yacl3.impl.YetAnotherConfigLibImpl;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import com.darkz.skintotem.utils.mixin.yacl.*;

@SuppressWarnings("UnstableApiUsage")
@Mixin(YetAnotherConfigLibImpl.BuilderImpl.class)
public class YetAnotherConfigLibImplBuilderMixin implements BetterYACLScreenBuilder {

	@Unique
	private boolean st$enabled;

	@ModifyReturnValue(at = @At("RETURN"), method = "build", remap = false)
	private YetAnotherConfigLib swapScreen(YetAnotherConfigLib original) {
		if (!this.st$enabled) {
			return original;
		}
		return ((BetterYACLScreenConfig) original).st$enable();
	}

	@Override
	public Builder st$enable() {
		this.st$enabled = true;
		return ((Builder) this);
	}
}
