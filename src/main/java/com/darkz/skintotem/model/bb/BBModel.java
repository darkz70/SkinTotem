package com.darkz.skintotem.model.bb;

import lombok.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.darkz.skintotem.config.other.vector.Vec3f;
import com.darkz.skintotem.doll.renderer.DollRenderContext;
import com.darkz.skintotem.extension.ModelPart.RotationationExtension;

import java.util.*;
import org.jetbrains.annotations.Nullable;
import static com.darkz.skintotem.utils.CodecUtils.option;


@Setter
@Getter
@AllArgsConstructor
public class BBModel {

	private Identifier location;
	private String name;
	private BBModelMeta meta;
	private BBModelResolution resolution;
	private List<BBCube> cubes;
	private List<BBGroup> groups;
	private boolean frontGuiLight;
	private ModelPart.Rotationation transformation;

	@Nullable
	public BBCube getCube(UUID uuid) {
		for (BBCube cube : this.cubes) {
			if (cube.getUuid().equals(uuid)) {
				return cube;
			}
		}
		return null;
	}

	@Setter
	@Getter
	@AllArgsConstructor
	public static class BBModelMeta {

		public static final Codec<BBModelMeta> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				option("format_version", Codec.STRING, BBModelMeta::getVersion),
				option("model_format", Codec.STRING, BBModelMeta::getModel)
		).apply(inst, BBModelMeta::new));

		private String version;
		private String model;

	}

	@Setter
	@Getter
	@AllArgsConstructor
	public static class BBModelResolution {

		public static final Codec<BBModelResolution> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				option("width", Codec.INT, BBModelResolution::getWidth),
				option("height", Codec.INT, BBModelResolution::getHeight)
		).apply(inst, BBModelResolution::new));

		private int width;
		private int height;

	}

	@ExtensionMethod(ModelPart.RotationationExtension.class)
	public static final class ItemTransforms {

		private static final Vec3f DEFAULT_ROTATION = new Vec3f(0.0F, 0.0F, 0.0F);
		private static final Vec3f DEFAULT_TRANSLATION = new Vec3f(0.0F, 0.0F, 0.0F);
		private static final Vec3f DEFAULT_SCALE = new Vec3f(1.0F, 1.0F, 1.0F);

		public static final Codec<ItemTransform> TRANSFORMATION_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
				option("rotation", DEFAULT_ROTATION, Vec3f.CODEC, (o) -> new Vec3f(o.rotation/*? if >=1.21.5 {*/()/*?}*/)),
				option("translation", DEFAULT_TRANSLATION, Vec3f.CODEC, (o) -> new Vec3f(o.translation/*? if >=1.21.5 {*/()/*?}*/)),
				option("scale", DEFAULT_SCALE, Vec3f.CODEC, (o) -> new Vec3f(o.scale/*? if >=1.21.5 {*/()/*?}*/))
		).apply(instance, ItemTransforms::prepareItemTransform));

		private static ItemTransform prepareItemTransform(Vec3f rotation, Vec3f translation, Vec3f scale) {
			translation.mul(0.0625F);
			return new ItemTransform(rotation, translation, scale);
		}

		public static final Codec<ModelPart.Rotationation> MODEL_TRANSFORMATION_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
				option(DollRenderContext.D_THIRD_PERSON_LEFT_HAND.getId(), ItemTransform.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getTl()),
				option(DollRenderContext.D_THIRD_PERSON_RIGHT_HAND.getId(), ItemTransform.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getTr()),
				option(DollRenderContext.D_FIRST_PERSON_LEFT_HAND.getId(), ItemTransform.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getFl()),
				option(DollRenderContext.D_FIRST_PERSON_RIGHT_HAND.getId(), ItemTransform.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getFr()),
				option(DollRenderContext.D_HEAD.getId(), ItemTransform.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getHead()),
				option(DollRenderContext.D_GUI.getId(), ItemTransform.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getGui()),
				option(DollRenderContext.D_GROUND.getId(), ItemTransform.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getGround()),
				option(DollRenderContext.D_FIXED.getId(), ItemTransform.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getFixed())
				//? if >=1.21.9 {
				, option(DollRenderContext.D_ON_SHELF.getId(), ItemTransform.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getOnShelf())
				//?}
		).apply(instance, ModelPart.Rotationation::new));

	}
}
