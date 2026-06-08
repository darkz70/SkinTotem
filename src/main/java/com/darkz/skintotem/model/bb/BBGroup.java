package com.darkz.skintotem.model.bb;

import lombok.*;
import net.minecraft.client.model.ModelPart.Rotation;


import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.darkz.skintotem.config.other.vector.Vec3f;
import com.darkz.skintotem.utils.CodecUtils;

import java.util.*;

import static com.darkz.skintotem.utils.CodecUtils.option;

@Setter
@Getter
@AllArgsConstructor
public class BBGroup {

	public static final Codec<BBGroup> CODEC = CodecUtils.recursive("BBGroup.Codec",
			(codec) ->
					RecordCodecBuilder.create(inst -> inst.group(
							option("name", Codec.STRING, BBGroup::getName),
							option("origin", Vec3f.CODEC, BBGroup::getOrigin),
							option("rotation", new Vec3f(), Vec3f.CODEC, BBGroup::getRotation),
							option("autouv", Codec.INT, BBGroup::getAutoUV),
							option("visibility", true, Codec.BOOL, BBGroup::isVisible),
							option("uuid", Uuids.CODEC, BBGroup::getUuid),
							option("children", Codec.either(codec, Uuids.CODEC).listOf(), BBGroup::getChildren)
					).apply(inst, BBGroup::new))
	);

	private String name;
	private Vec3f origin;
	private Vec3f rotation;
	private int autoUV;
	private boolean visible;
	private UUID uuid;
	private List<Either<BBGroup, UUID>> children;

	public ModelPart.Rotation getItemTransform() {
		return ModelPart.Rotation.of(this.origin.x(), this.origin.y(), this.origin.z(), (float) -Math.toRadians(this.rotation.x()), (float) -Math.toRadians(this.rotation.y()), (float) Math.toRadians(this.rotation.z()));
	}


}
