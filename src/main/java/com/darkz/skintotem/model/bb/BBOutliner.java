package com.darkz.skintotem.model.bb;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.*;
import lombok.*;
import com.darkz.skintotem.utils.CodecUtils;

import static com.darkz.skintotem.utils.CodecUtils.option;

@Setter
@Getter
@AllArgsConstructor
public class BBOutliner {

	public static final Codec<BBOutliner> CODEC = CodecUtils.recursive("BBOutliner.Codec",
			(codec) ->
					RecordCodecBuilder.create(inst -> inst.group(
							option("uuid", Uuids.CODEC, BBOutliner::getUuid),
							option("children", Codec.either(codec, Uuids.CODEC).listOf(), BBOutliner::getChildren)
					).apply(inst, BBOutliner::new))
	);

	private UUID uuid;
	private List<Either<BBOutliner, UUID>> children;

}
