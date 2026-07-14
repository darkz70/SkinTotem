package com.darkz.skintotem.config;

import com.google.gson.*;
import lombok.*;


import com.darkz.skintotem.utils.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.darkz.skintotem.loader.SkinTotemLoader;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.client.SkinTotemClient;
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
public class SkinTotemConfig {

	public static final Codec<SkinTotemConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("mod_enabled", true, Codec.BOOL, SkinTotemConfig::isModEnabled),
			option("debug_log_enabled", false, Codec.BOOL, SkinTotemConfig::isDebugLogEnabled),
			option("rendering_config", RenderingConfig.getNewInstance(), RenderingConfig.CODEC, SkinTotemConfig::getRenderingConfig),
			option("standard_doll_skin_data", "", Codec.STRING, SkinTotemConfig::getStandardTotemDollSkinValue),
			option("standard_doll_skin_type", TotemDollSkinType.STEVE, TotemDollSkinType.CODEC, SkinTotemConfig::getStandardTotemDollSkinType),
			option("standard_doll_model_data", TotemDollModel.TWO_D_MODEL_ID, ResourceLocation.CODEC, SkinTotemConfig::getStandardTotemDollModelValue),
			option("standard_doll_model_arms_type", TotemDollArmsType.WIDE, TotemDollArmsType.CODEC, SkinTotemConfig::getStandardTotemDollArmsType),
			option("tag_button_pos", new Vec2i(155, 48), Vec2i.CODEC, SkinTotemConfig::getTagButtonPos),
			option("use_vanilla_totem_model", false, Codec.BOOL, SkinTotemConfig::isUseVanillaTotemModel),
			Codec.INT.optionalFieldOf("better_tag_menu_tooltip_size")
					.xmap(o -> o.orElse(60), Optional::of)
					.forGetter(SkinTotemConfig::getBetterTagMenuTooltipSize),
			option("tag_menu_tooltip_model_scale", 1.0F, Codec.FLOAT, SkinTotemConfig::getTagMenuTooltipModelScale),
			option("executor_threads_count", 6, Codec.INT, SkinTotemConfig::getParallelTasksCount),
			option("first_run", true, Codec.BOOL, SkinTotemConfig::isFirstRun),
			option("support_other_mods_totems", true, Codec.BOOL, SkinTotemConfig::isSupportOtherModsTotems),
			option("auto_refresh_enabled", false, Codec.BOOL, SkinTotemConfig::isAutoRefreshEnabled),
			option("auto_refresh_interval_minutes", 30, Codec.INT, SkinTotemConfig::getAutoRefreshIntervalMinutes)
	).apply(instance, SkinTotemConfig::new));

	private static final File CONFIG_FILE = SkinTotemLoader.getConfigDir().resolve(SkinTotem.MOD_ID + ".json5").toFile();
	private static final Logger LOGGER = LoggerFactory.getLogger(SkinTotem.MOD_NAME + "/Config");
	private static SkinTotemConfig INSTANCE;

	private boolean modEnabled;
	private boolean debugLogEnabled;
	private RenderingConfig renderingConfig;
	private String standardTotemDollSkinValue;
	private TotemDollSkinType standardTotemDollSkinType;
	private ResourceLocation standardTotemDollModelValue;
	private TotemDollArmsType standardTotemDollArmsType;
	private Vec2i tagButtonPos;
	private boolean useVanillaTotemModel;
	private int betterTagMenuTooltipSize;
	private float tagMenuTooltipModelScale;
	private int parallelTasksCount;
	private boolean firstRun;
	private boolean supportOtherModsTotems;
	private boolean autoRefreshEnabled;
	private int autoRefreshIntervalMinutes;

	private SkinTotemConfig() {
		throw new IllegalArgumentException();
	}

	public static SkinTotemConfig getInstance() {
		return INSTANCE == null ? reload() : INSTANCE;
	}

	public static SkinTotemConfig reload() {
		return INSTANCE = SkinTotemConfig.read();
	}

	public static SkinTotemConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static SkinTotemConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, LOGGER);
		com.darkz.skintotem.refresh.SkinAutoRefresher.restart();
	}
}
