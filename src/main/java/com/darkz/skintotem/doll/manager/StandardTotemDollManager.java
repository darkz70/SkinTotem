package com.darkz.skintotem.doll.manager;

import com.darkz.skintotem.atlas.manager.SkinTotemModAtlasSpriteManager;
import net.minecraft.client.texture.*;
import net.minecraft.util.Identifier;
import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.client.SkinTotemModClient;


import com.darkz.skintotem.config.SkinTotemModConfig;
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
		SkinTotemModConfig config = SkinTotemModConfig.getInstance();
		data.getStandardSprites().setStandardArmsType(config.getStandardTotemDollArmsType());
		return data;
	}

	@NotNull
	public static TotemDollData loadStandardDoll() {
		SkinTotemModConfig config = SkinTotemModConfig.getInstance();
		TotemDollSkinType totemDollSkin = config.getStandardTotemDollSkinType();
		String data = config.getStandardTotemDollSkinValue();

		if (totemDollSkin == TotemDollSkinType.STEVE || totemDollSkin == TotemDollSkinType.HOLDING_PLAYER || data == null || data.isEmpty()) {
			return getSteveDoll();
		}

		return switch (totemDollSkin) {
			case PLAYER -> loadPlayerSkin(data);
			case URL_SKIN -> loadUrlSkin(data);
			case FILE_SKIN -> loadFileSkin(data);
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
			Identifier id = SkinTotemMod.getDollTextureId("file/%s".formatted(Math.abs(data.hashCode())));

			try (InputStream inputStream = Files.newInputStream(Path.of(data))) {
				NativeImage nativeImage = NativeImage.read(inputStream);

				SkinTotemModAtlasSpriteManager.registerSpecialSkinSprite(id, nativeImage, true, (sprite) -> {
					textures.setSkinSprite(sprite);
					textures.setState(LoadingState.DOWNLOADED);
				});

			} catch (NoSuchFileException e) {
				textures.setState(LoadingState.CRITICAL_ERROR);
			} catch (Exception e) {
				SkinTotemModClient.LOGGER.error("Failed to load skin from file at \"{}\":", data, e);
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
			Identifier id = SkinTotemMod.getDollTextureId("url/%s".formatted(Math.abs(data.hashCode())));

			FailedAction onFailed = (throwable) -> {
				textures.setState(LoadingState.CRITICAL_ERROR);
				SkinTotemModClient.LOGGER.warn("Failed to download standard doll url skin:", throwable);
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
}
