package com.darkz.skintotem.doll.manager;

import com.mojang.blaze3d.platform.NativeImage;
import com.darkz.skintotem.atlas.manager.SkinTotemAtlasSpriteManager;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.ResourceLocation;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.client.SkinTotemClient;


import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.config.totem.*;
import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.skin.provider.extended.MojangSkinProvider;
import com.darkz.skintotem.utils.texture.*;


import java.io.InputStream;
import java.nio.file.*;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.*;

public class StandardTotemDollManager {

	@Nullable
	private static TotemDollData DEFAULT_DOLL;

	@NotNull
	public static TotemDollData getStandardDoll() {
		if (DEFAULT_DOLL == null) {
			return initializeStandardDollData();
		}
		return DEFAULT_DOLL;
	}

	public static TotemDollData initializeStandardDollData() {
		DEFAULT_DOLL = overrideWithConfigValues(loadStandardDoll());
		return DEFAULT_DOLL;
	}

	public static TotemDollData updateDoll(boolean recreateModel) {
		TotemDollData standardDoll = getStandardDoll();
		overrideWithConfigValues(standardDoll);
		standardDoll.setShouldRecreateStandardModel(recreateModel);
		return standardDoll.refreshAndApplyRenderProperties();
	}

	public static TotemDollData overrideWithConfigValues(TotemDollData data) {
		SkinTotemConfig config = SkinTotemConfig.getInstance();
		data.getStandardSprites().setStandardArmsType(config.getStandardTotemDollArmsType());
		return data;
	}

	@NotNull
	public static TotemDollData loadStandardDoll() {
		SkinTotemConfig config = SkinTotemConfig.getInstance();
		TotemDollSkinType totemDollSkin = config.getStandardTotemDollSkinType();
		String data = config.getStandardTotemDollSkinValue();

		if (totemDollSkin == TotemDollSkinType.STEVE || totemDollSkin == TotemDollSkinType.HOLDING_PLAYER || data == null || data.isEmpty()) {
			return getSteveDoll();
		}

		return switch (totemDollSkin) {
			case PLAYER -> loadPlayerSkin(data);
			case URL_SKIN -> loadUrlSkin(data);
			case FILE_SKIN -> loadFileSkin(data);
			case ELY_BY -> loadElyBySkin(data);
			case TLAUNCHER -> loadTLauncherSkin(data);
			default -> getSteveDoll();
		};
	}

	public static @NotNull TotemDollData getSteveDoll() {
		TotemDollData totemDollData = TotemDollData.create(null);
		totemDollData.getStandardSprites().setState(LoadingState.DOWNLOADED);
		return totemDollData;
	}

	public static TotemDollData loadFileSkin(@NotNull String data) {
		TotemDollData totemDollData = TotemDollData.create(null);
		TotemDollSprites textures = totemDollData.getStandardSprites();
		textures.setState(LoadingState.DOWNLOADING);

		CompletableFuture.runAsync(() -> {
			ResourceLocation id = SkinTotem.getDollTextureId("file/%s".formatted(Math.abs(data.hashCode())));
			String path = data.endsWith("\"") && data.startsWith("\"") ? data.substring(1, data.length()-1) : data;

			try (InputStream inputStream = Files.newInputStream(Path.of(path))) {
				NativeImage nativeImage = NativeImage.read(inputStream);

				SkinTotemAtlasSpriteManager.registerSpecialSkinSprite(id, nativeImage, true, (sprite) -> {
					textures.setSkinSprite(sprite);
					textures.setState(LoadingState.DOWNLOADED);
				});

			} catch (NoSuchFileException e) {
				textures.setState(LoadingState.CRITICAL_ERROR);
			} catch (Exception e) {
				SkinTotemClient.LOGGER.error("Failed to load skin from file at \"{}\":", data, e);
				textures.setState(LoadingState.CRITICAL_ERROR);
			}
		});

		return totemDollData;
	}

	public static TotemDollData loadUrlSkin(@NotNull String data) {
		TotemDollData totemDollData = TotemDollData.create(null);
		TotemDollSprites textures = totemDollData.getStandardSprites();
		textures.setState(LoadingState.DOWNLOADING);

		CompletableFuture.runAsync(() -> {
			ResourceLocation id = SkinTotem.getDollTextureId("url/%s".formatted(Math.abs(data.hashCode())));

			FailedAction onFailed = (throwable) -> {
				textures.setState(LoadingState.CRITICAL_ERROR);
				SkinTotemClient.LOGGER.warn("Failed to download standard doll url skin:", throwable);
			};

			SuccessAction onSuccess = (sprite) -> {
				textures.setSkinSprite(sprite);
				textures.setState(LoadingState.DOWNLOADED);
			};

			PlayerSkinUtils.downloadSkin(data, id, onSuccess, onFailed, false);
		});

		return totemDollData;
	}

	public static TotemDollData loadPlayerSkin(@NotNull String data) {
		if (MojangSkinProvider.getInstance().canProcess(data)) {
			TotemDollData totemDollData = MojangSkinProvider.getInstance().createNewDoll(data);
			MojangSkinProvider.getInstance().loadDoll(data, true, totemDollData);
			return totemDollData;
		}
		return getSteveDoll();
	}

	public static TotemDollData loadElyBySkin(@NotNull String data) {
		String tagged = com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider.PREFIX + data;
		var provider = com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider.getInstance();
		if (provider.canProcess(tagged)) {
			TotemDollData totemDollData = provider.createNewDoll(tagged);
			provider.loadDoll(tagged, true, totemDollData);
			return totemDollData;
		}
		return getSteveDoll();
	}

	public static TotemDollData loadTLauncherSkin(@NotNull String data) {
		String tagged = com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider.PREFIX + data;
		var provider = com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider.getInstance();
		if (provider.canProcess(tagged)) {
			TotemDollData totemDollData = provider.createNewDoll(tagged);
			provider.loadDoll(tagged, true, totemDollData);
			return totemDollData;
		}
		return getSteveDoll();
	}
}
