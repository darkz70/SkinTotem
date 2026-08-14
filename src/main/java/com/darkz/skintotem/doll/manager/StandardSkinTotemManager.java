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

public class StandardSkinTotemManager {

	@Nullable
	private static SkinTotemData DEFAULT_DOLL;

	@NotNull
	public static SkinTotemData getStandardDoll() {
		if (DEFAULT_DOLL == null) {
			return initializeStandardDollData();
		}
		return DEFAULT_DOLL;
	}

	public static SkinTotemData initializeStandardDollData() {
		DEFAULT_DOLL = overrideWithConfigValues(loadStandardDoll());
		return DEFAULT_DOLL;
	}

	public static SkinTotemData updateDoll(boolean recreateModel) {
		SkinTotemData standardDoll = getStandardDoll();
		overrideWithConfigValues(standardDoll);
		standardDoll.setShouldRecreateStandardModel(recreateModel);
		return standardDoll.refreshAndApplyRenderProperties();
	}

	public static SkinTotemData overrideWithConfigValues(SkinTotemData data) {
		SkinTotemConfig config = SkinTotemConfig.getInstance();
		data.getStandardSprites().setStandardArmsType(config.getStandardSkinTotemArmsType());
		return data;
	}

	@NotNull
	public static SkinTotemData loadStandardDoll() {
		SkinTotemConfig config = SkinTotemConfig.getInstance();
		SkinTotemSkinType totemDollSkin = config.getStandardSkinTotemSkinType();
		String data = config.getStandardSkinTotemSkinValue();

		if (totemDollSkin == SkinTotemSkinType.STEVE || totemDollSkin == SkinTotemSkinType.HOLDING_PLAYER || data == null || data.isEmpty()) {
			return getSteveDoll();
		}

		return switch (totemDollSkin) {
			case PLAYER -> loadPlayerSkin(data);
			case URL_SKIN -> loadUrlSkin(data);
			case FILE_SKIN -> loadFileSkin(data);
			case TLAUNCHER -> loadTLauncherSkin(data);
			case ELY_BY -> loadElyBySkin(data);
			default -> getSteveDoll();
		};
	}

	public static @NotNull SkinTotemData getSteveDoll() {
		SkinTotemData totemDollData = SkinTotemData.create(null);
		totemDollData.getStandardSprites().setState(LoadingState.DOWNLOADED);
		return totemDollData;
	}

	public static SkinTotemData loadFileSkin(@NotNull String data) {
		SkinTotemData totemDollData = SkinTotemData.create(null);
		SkinTotemSprites textures = totemDollData.getStandardSprites();
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

	public static SkinTotemData loadUrlSkin(@NotNull String data) {
		SkinTotemData totemDollData = SkinTotemData.create(null);
		SkinTotemSprites textures = totemDollData.getStandardSprites();
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

	public static SkinTotemData loadPlayerSkin(@NotNull String data) {
		if (MojangSkinProvider.getInstance().canProcess(data)) {
			SkinTotemData totemDollData = MojangSkinProvider.getInstance().createNewDoll(data);
			MojangSkinProvider.getInstance().loadDoll(data, true, totemDollData);
			return totemDollData;
		}
		return getSteveDoll();
	}

	public static SkinTotemData loadTLauncherSkin(@NotNull String data) {
		String value = data.startsWith(com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider.PREFIX)
				? data
				: com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider.PREFIX + data;

		com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider provider =
				com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider.getInstance();

		if (provider.canProcess(value)) {
			SkinTotemData totemDollData = provider.createNewDoll(value);
			provider.loadDoll(value, true, totemDollData);
			return totemDollData;
		}
		return getSteveDoll();
	}

	public static SkinTotemData loadElyBySkin(@NotNull String data) {
		String value = data.startsWith(com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider.PREFIX)
				? data
				: com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider.PREFIX + data;

		com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider provider =
				com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider.getInstance();

		if (provider.canProcess(value)) {
			SkinTotemData totemDollData = provider.createNewDoll(value);
			provider.loadDoll(value, true, totemDollData);
			return totemDollData;
		}
		return getSteveDoll();
	}
}
