package com.darkz.skintotem.doll.manager;

import com.darkz.skintotem.atlas.manager.SkinTotemAtlasSpriteManager;
import net.minecraft.client.renderer.texture.*;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.client.SkinTotemClient;


import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.config.totem.*;
import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.skin.provider.extended.MojangSkinProvider;
import com.darkz.skintotem.skin.provider.extended.TLauncherSkinProvider;
import com.darkz.skintotem.skin.provider.extended.ElyBySkinProvider;
import com.darkz.skintotem.utils.texture.*;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
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
		SkinTotemSkinType skinTotemSkin = config.getStandardSkinTotemSkinType();
		String data = config.getStandardSkinTotemSkinValue();

		if (skinTotemSkin == SkinTotemSkinType.STEVE || skinTotemSkin == SkinTotemSkinType.HOLDING_PLAYER || data == null || data.isEmpty()) {
			return getSteveDoll();
		}

		return switch (skinTotemSkin) {
			case PLAYER -> loadPlayerSkin(data);
			case URL_SKIN -> loadUrlSkin(data);
			case FILE_SKIN -> loadFileSkin(data);
			case TLAUNCHER -> TLauncherSkinProvider.getInstance().getOrLoadDoll(TLauncherSkinProvider.PREFIX + data);
			case ELY_BY -> ElyBySkinProvider.getInstance().getOrLoadDoll(ElyBySkinProvider.PREFIX + data);
			case STEVE, HOLDING_PLAYER -> getSteveDoll();
		};
	}

	public static @NotNull SkinTotemData getSteveDoll() {
		SkinTotemData skinTotemData = SkinTotemData.create(null);
		skinTotemData.getStandardSprites().setState(LoadingState.DOWNLOADED);
		return skinTotemData;
	}

	public static SkinTotemData loadFileSkin(@NotNull String data) {
		SkinTotemData skinTotemData = SkinTotemData.create(null);
		SkinTotemSprites textures = skinTotemData.getStandardSprites();
		textures.setState(LoadingState.DOWNLOADING);

		CompletableFuture.runAsync(() -> {
			ResourceLocation id = SkinTotem.getDollTextureId("file/%s".formatted(sha1(data)));

			try (InputStream inputStream = Files.newInputStream(Path.of(data))) {
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

		return skinTotemData;
	}

	public static SkinTotemData loadUrlSkin(@NotNull String data) {
		SkinTotemData skinTotemData = SkinTotemData.create(null);
		SkinTotemSprites textures = skinTotemData.getStandardSprites();
		textures.setState(LoadingState.DOWNLOADING);

		CompletableFuture.runAsync(() -> {
			ResourceLocation id = SkinTotem.getDollTextureId("url/%s".formatted(sha1(data)));

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

		return skinTotemData;
	}

	public static SkinTotemData loadPlayerSkin(@NotNull String data) {
		if (MojangSkinProvider.getInstance().canProcess(data)) {
			SkinTotemData skinTotemData = MojangSkinProvider.getInstance().createNewDoll(data);
			MojangSkinProvider.getInstance().loadDoll(data, true, skinTotemData);
			return skinTotemData;
		}
		return getSteveDoll();
	}

	/** Возвращает первые 16 символов SHA-1 хэша строки для использования в ResourceLocation. */
	private static String sha1(@NotNull String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) sb.append(String.format("%02x", b));
			return sb.substring(0, 16);
		} catch (Exception e) {
			// SHA-1 всегда доступен в JVM, но на крайний случай — fallback
			return String.valueOf(Math.abs(input.hashCode()));
		}
	}
}
