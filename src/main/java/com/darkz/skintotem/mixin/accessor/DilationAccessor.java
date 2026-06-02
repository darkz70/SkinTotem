package com.darkz.skintotem.mixin.accessor;

import net.minecraft.client.model.Dilation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Dilation.class)
public interface DilationAccessor {

	@Accessor("radiusX")
	float getRadiusX();

	@Accessor("radiusX")
	float getRadiusY();

	@Accessor("radiusX")
	float getRadiusZ();
}
