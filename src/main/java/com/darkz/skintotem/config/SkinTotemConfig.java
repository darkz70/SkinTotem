package com.darkz.skintotem.config;

import com.google.gson.*;
import lombok.*;


import com.darkz.skintotem.utils.*;
import net.minecraft.util.Identifier;
import org.slf4j.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.config.rendering.*;
import com.darkz.skintotem.config.totem.*;
import com.darkz.skintotem.config.other.vector.Vec2i;
import com.darkz.skintotem.doll.model.SkinTotemel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

import static com.darkz.skintotem.utils.CodecUtils.option;

public class SkinTotemConfig {

	public boolean isEnabled() { return modEnabled; }
	public void setEnabled(boolean modEnabled) { this.modEnabled = modEnabled; }
	public boolean isDebugLogEnabled() { return debugLogEnabled; }
	public void setDebugLogEnabled(boolean debugLogEnabled) { this.debugLogEnabled = debugLogEnabled; }
	public RenderingConfig getRenderingConfig() { return renderingConfig; }
	public void setRenderingConfig(RenderingConfig renderingConfig) { this.renderingConfig = renderingConfig; }
	public String getStandardSkinTotemSkinValue() { return standardSkinTotemSkinValue; }
	public void setStandardSkinTotemSkinValue(String standardSkinTotemSkinValue) { this.standardSkinTotemSkinValue = standardSkinTotemSkinValue; }
	public SkinTotemSkinType getStandardSkinTotemSkinType() { return standardSkinTotemSkinType; }
	public void setStandardSkinTotemSkinType(SkinTotemSkinType standardSkinTotemSkinType) { this.standardSkinTotemSkinType = standardSkinTotemSkinType; }
	public Identifier getStandardSkinTotemelValue() { return standardSkinTotemelValue; }
	public void setStandardSkinTotemelValue(Identifier standardSkinTotemelValue) { this.standardSkinTotemelValue = standardSkinTotemelValue; }
	public SkinTotemArmsType getStandardSkinTotemArmsType() { return standardSkinTotemArmsType; }
	public void setStandardSkinTotemArmsType(SkinTotemArmsType standardSkinTotemArmsType) { this.standardSkinTotemArmsType = standardSkinTotemArmsType; }
	public Vec2i getTagButtonPos() { return tagButtonPos; }
	public void setTagButtonPos(Vec2i tagButtonPos) { this.tagButtonPos = tagButtonPos; }
	public boolean isUseVanillaTotemModel() { return useVanillaTotemModel; }
	public void setUseVanillaTotemModel(boolean useVanillaTotemModel) { this.useVanillaTotemModel = useVanillaTotemModel; }
	public int getBetterTagMenuTooltipSize() { return betterTagMenuTooltipSize; }
	public void setBetterTagMenuTooltipSize(int betterTagMenuTooltipSize) { this.betterTagMenuTooltipSize = betterTagMenuTooltipSize; }
	public float getTagMenuTooltipModelScale() { return tagMenuTooltipModelScale; }
	public void setTagMenuTooltipModelScale(float tagMenuTooltipModelScale) { this.tagMenuTooltipModelScale = tagMenuTooltipModelScale; }
	public int getParallelTasksCount() { return parallelTasksCount; }
	public void setParallelTasksCount(int parallelTasksCount) { this.parallelTasksCount = parallelTasksCount; }
	public boolean isFirstRun() { return firstRun; }
	public void setFirstRun(boolean firstRun) { this.firstRun = firstRun; }
	public boolean isSupportOthersTotems() { return supportOthersTotems; }
	public void setSupportOthersTotems(boolean supportOthersTotems) { this.supportOthersTotems = supportOthersTotems; }
	public boolean isAutoRefreshEnabled() { return autoRefreshEnabled; }
	public void setAutoRefreshEnabled(boolean autoRefreshEnabled) { this.autoRefreshEnabled = autoRefreshEnabled; }
	public int getAutoRefreshIntervalMinutes() { return autoRefreshIntervalMinutes; }
	public void setAutoRefreshIntervalMinutes(int autoRefreshIntervalMinutes) { this.autoRefreshIntervalMinutes = autoRefreshIntervalMinutes; }

	public SkinTotemConfig(boolean modEnabled, boolean debugLogEnabled, RenderingConfig renderingConfig, String standardSkinTotemSkinValue, SkinTotemSkinType standardSkinTotemSkinType, Identifier standardSkinTotemelValue, SkinTotemArmsType standardSkinTotemArmsType, Vec2i tagButtonPos, boolean useVanillaTotemModel, int betterTagMenuTooltipSize, float tagMenuTooltipModelScale, int parallelTasksCount, boolean firstRun, boolean supportOthersTotems, boolean autoRefreshEnabled, int autoRefreshIntervalMinutes) {
		this.modEnabled = modEnabled;
		this.debugLogEnabled = debugLogEnabled;
		this.renderingConfig = renderingConfig;
		this.standardSkinTotemSkinValue = standardSkinTotemSkinValue;
		this.standardSkinTotemSkinType = standardSkinTotemSkinType;
		this.standardSkinTotemelValue = standardSkinTotemelValue;
		this.standardSkinTotemArmsType = standardSkinTotemArmsType;
		this.tagButtonPos = tagButtonPos;
		this.useVanillaTotemModel = useVanillaTotemModel;
		this.betterTagMenuTooltipSize = betterTagMenuTooltipSize;
		this.tagMenuTooltipModelScale = tagMenuTooltipModelScale;
		this.parallelTasksCount = parallelTasksCount;
		this.firstRun = firstRun;
		this.supportOthersTotems = supportOthersTotems;
		this.autoRefreshEnabled = autoRefreshEnabled;
		this.autoRefreshIntervalMinutes = autoRefreshIntervalMinutes;
	}

	public static final Codec<SkinTotemConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("mod_enabled", true, Codec.BOOL, SkinTotemConfig::isEnabled),
			option("debug_log_enabled", false, Codec.BOOL, SkinTotemConfig::isDebugLogEnabled),
			option("rendering_config", RenderingConfig.getNewInstance(), RenderingConfig.CODEC, SkinTotemConfig::getRenderingConfig),
			option("standard_doll_skin_data", "", Codec.STRING, SkinTotemConfig::getStandardSkinTotemSkinValue),
			option("standard_doll_skin_type", SkinTotemSkinType.STEVE, SkinTotemSkinType.CODEC, SkinTotemConfig::getStandardSkinTotemSkinType),
			option("standard_doll_model_data", SkinTotemel.TWO_D_MODEL_ID, Identifier.CODEC, SkinTotemConfig::getStandardSkinTotemelValue),
			option("standard_doll_model_arms_type", SkinTotemArmsType.WIDE, SkinTotemArmsType.CODEC, SkinTotemConfig::getStandardSkinTotemArmsType),
			option("tag_button_pos", new Vec2i(155, 48), Vec2i.CODEC, SkinTotemConfig::getTagButtonPos),
			option("use_vanilla_totem_model", false, Codec.BOOL, SkinTotemConfig::isUseVanillaTotemModel),
			Codec.INT.optionalFieldOf("better_tag_menu_tooltip_size")
					.xmap(o -> o.orElse(60), Optional::of)
					.forGetter(SkinTotemConfig::getBetterTagMenuTooltipSize),
			option("tag_menu_tooltip_model_scale", 1.0F, Codec.FLOAT, SkinTotemConfig::getTagMenuTooltipModelScale),
			option("executor_threads_count", 6, Codec.INT, SkinTotemConfig::getParallelTasksCount),
			option("first_run", true, Codec.BOOL, SkinTotemConfig::isFirstRun),
			option("support_other_mods_totems", true, Codec.BOOL, SkinTotemConfig::isSupportOthersTotems),
			option("auto_refresh_enabled", false, Codec.BOOL, SkinTotemConfig::isAutoRefreshEnabled),
			option("auto_refresh_interval_minutes", 5, Codec.INT, SkinTotemConfig::getAutoRefreshIntervalMinutes)
	).apply(instance, SkinTotemConfig::new));

	private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(SkinTotem.MOD_ID + ".json5").toFile();
	private static final Logger LOGGER = LoggerFactory.getLogger(SkinTotem.MOD_NAME + "/Config");
	private static SkinTotemConfig INSTANCE;

	private boolean modEnabled;
	private boolean debugLogEnabled;
	private RenderingConfig renderingConfig;
	private String standardSkinTotemSkinValue;
	private SkinTotemSkinType standardSkinTotemSkinType;
	private Identifier standardSkinTotemelValue;
	private SkinTotemArmsType standardSkinTotemArmsType;
	private Vec2i tagButtonPos;
	private boolean useVanillaTotemModel;
	private int betterTagMenuTooltipSize;
	private float tagMenuTooltipModelScale;
	private int parallelTasksCount;
	private boolean firstRun;
	private boolean supportOthersTotems;
	private boolean autoRefreshEnabled;
	private int autoRefreshIntervalMinutes;

	private SkinTotemConfig() {
		// throw new IllegalArgumentException();
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
	}
}
