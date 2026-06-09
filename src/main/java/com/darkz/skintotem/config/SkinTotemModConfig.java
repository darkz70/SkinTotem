package com.darkz.skintotem.config;

import com.google.gson.*;
import lombok.*;


import com.darkz.skintotem.utils.*;
import net.minecraft.resources.ResourceLocation;
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

public class SkinTotemModConfig {

	public boolean isModEnabled() { return modEnabled; }
	public void setModEnabled(boolean modEnabled) { this.modEnabled = modEnabled; }
	public boolean isDebugLogEnabled() { return debugLogEnabled; }
	public void setDebugLogEnabled(boolean debugLogEnabled) { this.debugLogEnabled = debugLogEnabled; }
	public RenderingConfig getRenderingConfig() { return renderingConfig; }
	public void setRenderingConfig(RenderingConfig renderingConfig) { this.renderingConfig = renderingConfig; }
	public String getStandardTotemDollSkinValue() { return standardTotemDollSkinValue; }
	public void setStandardTotemDollSkinValue(String standardTotemDollSkinValue) { this.standardTotemDollSkinValue = standardTotemDollSkinValue; }
	public TotemDollSkinType getStandardTotemDollSkinType() { return standardTotemDollSkinType; }
	public void setStandardTotemDollSkinType(TotemDollSkinType standardTotemDollSkinType) { this.standardTotemDollSkinType = standardTotemDollSkinType; }
	public ResourceLocation getStandardTotemDollModelValue() { return standardTotemDollModelValue; }
	public void setStandardTotemDollModelValue(ResourceLocation standardTotemDollModelValue) { this.standardTotemDollModelValue = standardTotemDollModelValue; }
	public TotemDollArmsType getStandardTotemDollArmsType() { return standardTotemDollArmsType; }
	public void setStandardTotemDollArmsType(TotemDollArmsType standardTotemDollArmsType) { this.standardTotemDollArmsType = standardTotemDollArmsType; }
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
	public boolean isSupportOtherModsTotems() { return supportOtherModsTotems; }
	public void setSupportOtherModsTotems(boolean supportOtherModsTotems) { this.supportOtherModsTotems = supportOtherModsTotems; }

	public SkinTotemModConfig(boolean modEnabled, boolean debugLogEnabled, RenderingConfig renderingConfig, String standardTotemDollSkinValue, TotemDollSkinType standardTotemDollSkinType, ResourceLocation standardTotemDollModelValue, TotemDollArmsType standardTotemDollArmsType, Vec2i tagButtonPos, boolean useVanillaTotemModel, int betterTagMenuTooltipSize, float tagMenuTooltipModelScale, int parallelTasksCount, boolean firstRun, boolean supportOtherModsTotems) {
		this.modEnabled = modEnabled;
		this.debugLogEnabled = debugLogEnabled;
		this.renderingConfig = renderingConfig;
		this.standardTotemDollSkinValue = standardTotemDollSkinValue;
		this.standardTotemDollSkinType = standardTotemDollSkinType;
		this.standardTotemDollModelValue = standardTotemDollModelValue;
		this.standardTotemDollArmsType = standardTotemDollArmsType;
		this.tagButtonPos = tagButtonPos;
		this.useVanillaTotemModel = useVanillaTotemModel;
		this.betterTagMenuTooltipSize = betterTagMenuTooltipSize;
		this.tagMenuTooltipModelScale = tagMenuTooltipModelScale;
		this.parallelTasksCount = parallelTasksCount;
		this.firstRun = firstRun;
		this.supportOtherModsTotems = supportOtherModsTotems;
	}

	public static final Codec<SkinTotemModConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("mod_enabled", true, Codec.BOOL, SkinTotemModConfig::isModEnabled),
			option("debug_log_enabled", false, Codec.BOOL, SkinTotemModConfig::isDebugLogEnabled),
			option("rendering_config", RenderingConfig.getNewInstance(), RenderingConfig.CODEC, SkinTotemModConfig::getRenderingConfig),
			option("standard_doll_skin_data", "", Codec.STRING, SkinTotemModConfig::getStandardTotemDollSkinValue),
			option("standard_doll_skin_type", TotemDollSkinType.STEVE, TotemDollSkinType.CODEC, SkinTotemModConfig::getStandardTotemDollSkinType),
			option("standard_doll_model_data", TotemDollModel.TWO_D_MODEL_ID, ResourceLocation.CODEC, SkinTotemModConfig::getStandardTotemDollModelValue),
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
	private ResourceLocation standardTotemDollModelValue;
	private TotemDollArmsType standardTotemDollArmsType;
	private Vec2i tagButtonPos;
	private boolean useVanillaTotemModel;
	private int betterTagMenuTooltipSize;
	private float tagMenuTooltipModelScale;
	private int parallelTasksCount;
	private boolean firstRun;
	private boolean supportOtherModsTotems;

	private SkinTotemModConfig() {
		// throw new IllegalArgumentException();
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
