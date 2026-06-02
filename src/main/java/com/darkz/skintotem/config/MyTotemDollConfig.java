package com.darkz.skintotem.config;

import com.google.gson.*;
import lombok.*;


import com.darkz.skintotem.utils.*;
import net.minecraft.util.Identifier;
import org.slf4j.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.config.rendering.*;
import com.darkz.skintotem.config.totem.*;
import com.darkz.skintotem.config.other.vector.Vec2i;
import com.darkz.skintotem.doll.model.TotemDollModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

import static com.darkz.skintotem.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class SkinTotemModConfig {

	public static final Codec<SkinTotemModConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("mod_enabled", true, Codec.BOOL, SkinTotemModConfig::isModEnabled),
			option("debug_log_enabled", false, Codec.BOOL, SkinTotemModConfig::isDebugLogEnabled),
			option("rendering_config", RenderingConfig.getNewInstance(), RenderingConfig.CODEC, SkinTotemModConfig::getRenderingConfig),
			option("standard_doll_skin_data", "", Codec.STRING, SkinTotemModConfig::getStandardTotemDollSkinValue),
			option("standard_doll_skin_type", TotemDollSkinType.STEVE, TotemDollSkinType.CODEC, SkinTotemModConfig::getStandardTotemDollSkinType),
			option("standard_doll_model_data", TotemDollModel.TWO_D_MODEL_ID, Identifier.CODEC, SkinTotemModConfig::getStandardTotemDollModelValue),
			option("standard_doll_model_arms_type", TotemDollArmsType.WIDE, TotemDollArmsType.CODEC, SkinTotemModConfig::getStandardTotemDollArmsType),
			option("tag_button_pos", new Vec2i(155, 48), Vec2i.CODEC, SkinTotemModConfig::getTagButtonPos),
			option("use_vanilla_totem_model", false, Codec.BOOL, SkinTotemModConfig::isUseVanillaTotemModel),
			Codec.INT.optionalFieldOf("better_tag_menu_tooltip_size")
					.xmap(o -> o.orElse(60), Optional::of)
					.forGetter(SkinTotemModConfig::getBetterTagMenuTooltipSize),
			option("tag_menu_tooltip_model_scale", 1.0F, Codec.FLOAT, SkinTotemModConfig::getTagMenuTooltipModelScale),
			option("executor_threads_count", 6, Codec.INT, SkinTotemModConfig::getParallelTasksCount),
			option("first_run", true, Codec.BOOL, SkinTotemModConfig::isFirstRun),
			option("support_other_mods_totems", true, Codec.BOOL, SkinTotemModConfig::isSupportOtherModsTotems)
	).apply(instance, SkinTotemModConfig::new));

	private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(SkinTotemMod.MOD_ID + ".json5").toFile();
	private static final Logger LOGGER = LoggerFactory.getLogger(SkinTotemMod.MOD_NAME + "/Config");
	private static SkinTotemModConfig INSTANCE;

	private boolean modEnabled;
	private boolean debugLogEnabled;
	private RenderingConfig renderingConfig;
	private String standardTotemDollSkinValue;
	private TotemDollSkinType standardTotemDollSkinType;
	private Identifier standardTotemDollModelValue;
	private TotemDollArmsType standardTotemDollArmsType;
	private Vec2i tagButtonPos;
	private boolean useVanillaTotemModel;
	private int betterTagMenuTooltipSize;
	private float tagMenuTooltipModelScale;
	private int parallelTasksCount;
	private boolean firstRun;
	private boolean supportOtherModsTotems;

	private SkinTotemModConfig() {
		throw new IllegalArgumentException();
	}

	public static SkinTotemModConfig getInstance() {
		return INSTANCE == null ? reload() : INSTANCE;
	}

	public static SkinTotemModConfig reload() {
		return INSTANCE = SkinTotemModConfig.read();
	}

	public static SkinTotemModConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static SkinTotemModConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, LOGGER);
	}
}
