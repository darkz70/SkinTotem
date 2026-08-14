package com.darkz.skintotem.mixin.yacl.category;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ConfigCategory.Builder;
import dev.isxander.yacl3.impl.ConfigCategoryImpl;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import com.darkz.skintotem.utils.mixin.yacl.BetterYACLCategoryBuilder;
import com.darkz.skintotem.yacl.custom.category.better.BetterConfigCategoryImpl;
import com.darkz.skintotem.yacl.custom.category.rendering.RenderingConfigCategoryImpl;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ConfigCategoryImpl.BuilderImpl.class)
public class ConfigCategoryImplBuilderImplMixin implements BetterYACLCategoryBuilder {

	@Unique
	private int mySkinTotem$custom = -1;

	@ModifyReturnValue(at = @At("RETURN"), method = "build", remap = false)
	private ConfigCategory swapCategory(ConfigCategory original) {
		if (this.mySkinTotem$custom == -1) {
			return original;
		} else if (this.mySkinTotem$custom == 0) {
			return new BetterConfigCategoryImpl(original.name(), original.groups(), original.tooltip());
		} else if (this.mySkinTotem$custom == 1) {
			return new RenderingConfigCategoryImpl(original.name(), original.groups(), original.tooltip());
		}
		throw new IllegalArgumentException("Who modified me? mm???? [SkinTotem]");
	}

	@Override
	public Builder mySkinTotem$enableBetter() {
		this.mySkinTotem$custom = 0;
		return ((Builder) this);
	}

	@Override
	public Builder mySkinTotem$enableRendering() {
		this.mySkinTotem$custom = 1;
		return ((Builder) this);
	}
}
