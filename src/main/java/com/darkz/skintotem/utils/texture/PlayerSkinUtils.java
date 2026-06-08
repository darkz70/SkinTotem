package com.darkz.skintotem.utils.texture;

import java.net.*;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.atlas.RemappedAtlasSprite;
import com.darkz.skintotem.atlas.manager.*;
import com.darkz.skintotem.doll.data.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.ResourceLocation;

import com.darkz.skintotem.client.SkinTotemModClient;

import java.io.*;
import java.nio.file.*;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.*;

import com.darkz.skintotem.atlas.manager.*;
import com.darkz.skintotem.config.totem.TotemDollArmsType;
import com.darkz.skintotem.doll.data.*;
import com.darkz.skintotem.thread.SkinTotemModTaskExecutor;

public class PlayerSkinUtils {

	public static void downloadSkin(@NotNull String textureUrl, @NotNull Identifier textureId, @Nullable SuccessAction onSuccessRegistration, @Nullable FailedAction onFailedRegistration, boolean skin) {
		try {
			NativeImage nativeImage = download(textureUrl);
			NativeImage image = skin ? remapSkinTexture(nativeImage) : remapTextureToStandardSize(nativeImage, true);
			SkinTotemModAtlasSpriteManager.registerSpecialSkinSprite(textureId, image, true, (sprite) -> {
				if (onSuccessRegistration != null) {
					onSuccessRegistration.onSuccess(sprite);
				}
			});
		} catch (Exception e) {
			SkinTotemModClient.LOGGER.error("Failed to download skin texture with id \"{}\": ", textureId, e);
			if (onFailedRegistration != null) {
				onFailedRegistration.onFailed(e);
			}
		}
	}

	public static NativeImage download(String uri) throws IOException {
		HttpURLConnection connection = null;
		SkinTotemModClient.LOGGER.debug("Downloading HTTP texture from {}", uri);
		URI currentUri = URI.create(uri);

		NativeImage image;
		try {
			connection = (HttpURLConnection) currentUri.toURL().openConnection(Minecraft.getInstance().getNetworkProxy());
			connection.setDoInput(true);
			connection.setDoOutput(false);
			connection.connect();
			int i = connection.getResponseCode();
			if (i / 100 != 2) {
				String url = String.valueOf(currentUri);
				throw new IOException("Failed to open " + url + ", HTTP error code: " + i);
			}

			image = NativeImage.read(connection.getInputStream().readAllBytes());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}

		}
		return image;
	}

	public static @NotNull NativeImage remapTextureToStandardSize(NativeImage image, boolean closeOriginal) {
		if (image.getWidth() == 64 && image.getHeight() == 64) {
			return image;
		}
		NativeImage nativeImage = new NativeImage(64, 64, true);
		nativeImage.copyFrom(image);
		if (closeOriginal) {
			image.close();
		}
		return nativeImage;
	}

	public static NativeImage remapSkinTexture(NativeImage image) {
		int i = image.getHeight();
		boolean bl = i == 32;
		if (bl) {
			NativeImage nativeImage = new NativeImage(64, 64, true);
			nativeImage.copyFrom(image);
			image.close();
			image = nativeImage;
			nativeImage.fillRect(0, 32, 64, 32, 0);
			nativeImage.copyRect(4, 16, 16, 32, 4, 4, true, false);
			nativeImage.copyRect(8, 16, 16, 32, 4, 4, true, false);
			nativeImage.copyRect(0, 20, 24, 32, 4, 12, true, false);
			nativeImage.copyRect(4, 20, 16, 32, 4, 12, true, false);
			nativeImage.copyRect(8, 20, 8, 32, 4, 12, true, false);
			nativeImage.copyRect(12, 20, 16, 32, 4, 12, true, false);
			nativeImage.copyRect(44, 16, -8, 32, 4, 4, true, false);
			nativeImage.copyRect(48, 16, -8, 32, 4, 4, true, false);
			nativeImage.copyRect(40, 20, 0, 32, 4, 12, true, false);
			nativeImage.copyRect(44, 20, -8, 32, 4, 12, true, false);
			nativeImage.copyRect(48, 20, -16, 32, 4, 12, true, false);
			nativeImage.copyRect(52, 20, -8, 32, 4, 12, true, false);
		}

		stripAlpha(image, 0, 0, 32, 16);
		if (bl) {
			stripColor(image, 32, 0, 64, 32);
		}

		stripAlpha(image, 0, 16, 64, 32);
		stripAlpha(image, 16, 48, 48, 64);
		return image;
	}

	//? if >=1.21.2 {
	@SuppressWarnings("all")
	private static void stripColor(NativeImage image, int x1, int y1, int x2, int y2) {
		for (int i = x1; i < x2; ++i) {
			for (int j = y1; j < y2; ++j) {
				int k = image.getColorArgb(i, j);
				if (FastColor.getAlpha(k) < 128) {
					return;
				}
			}
		}

		for (int i = x1; i < x2; ++i) {
			for (int j = y1; j < y2; ++j) {
				image.setColorArgb(i, j, image.getColorArgb(i, j) & 16777215);
			}
		}
	}

	@SuppressWarnings("all")
	private static void stripAlpha(NativeImage image, int x1, int y1, int x2, int y2) {
		for (int i = x1; i < x2; ++i) {
			for (int j = y1; j < y2; ++j) {
				image.setColorArgb(i, j, FastColor.fullAlpha(image.getColorArgb(i, j)));
			}
		}
	}
	//?} else {
	/*@SuppressWarnings("all")
	private static void stripColor(NativeImage image, int x1, int y1, int x2, int y2) {
		for(int i = x1; i < x2; ++i) {
			for(int j = y1; j < y2; ++j) {
				int k = image.getColor(i, j);
				if ((k >> 24 & 255) < 128) {
					return;
				}
			}
		}

		for(int i = x1; i < x2; ++i) {
			for(int j = y1; j < y2; ++j) {
				image.setColor(i, j, image.getColor(i, j) & 16777215);
			}
		}

	}

	@SuppressWarnings("all")
	private static void stripAlpha(NativeImage image, int x1, int y1, int x2, int y2) {
		for(int i = x1; i < x2; ++i) {
			for(int j = y1; j < y2; ++j) {
				image.setColor(i, j, image.getColor(i, j) | -16777216);
			}
		}

	}
	*///?}

	public static void setupClientTextures(TotemDollData data) {
		//? if >=1.21 {
		Minecraft.getInstance().getSkinProvider().fetchPlayerSkin(Minecraft.getInstance().getGameProfile()).thenAccept((/*? if >=1.21.4 {*/ optional /*?} else {*/ /*skinTextures *//*?}*/) -> {
			//? if >=1.21.4 {
			if (optional.isEmpty()) {
				return;
			}
			//? if >=1.21.9 {
			net.minecraft.entity.player.PlayerSkin skinTextures = optional.get();
			//?} else {
			/*net.minecraft.client.util.PlayerSkin skinTextures = optional.get();
			*///?}

			//?}
			data.setSprites(TotemDollSprites.of(skinTextures));
		});
		//?} else {
		/*Minecraft.getInstance().getSkinProvider().loadSkin(Minecraft.getInstance().getSession().getProfile(), (type, id, texture) -> {
			SkinTotemModTaskExecutor.execute(() -> {
				Minecraft.getInstance().execute(() -> {
					TotemDollSprites textures = data.getStandardSprites();

					switch (type) {
						case SKIN -> {
							SkinTotemModAtlasSpriteManager.registerSpecialSkinSprite(id, false, textures::setSkinSprite);
							if (texture != null) {
								textures.setArmsType(TotemDollArmsType.of(texture.getMetadata("model")));
							}
						}
						case CAPE -> {
							RemappedAtlasSprite capeSprite = RemappedAtlasSprite.ofResource(id);
							SkinTotemModAtlasSpriteManager.registerSpecialRemappedSprite(capeSprite);
							textures.setCapeSprite(capeSprite);
						}
						case ELYTRA -> SkinTotemModAtlasSpriteManager.registerSpecialSkinSprite(id, false, textures::setElytraSprite);
					}

					SkinTotemModAtlasManager.stitchAndUpdate(SkinTotemModAtlasSpriteManager.getSprites(), null);
				});
			});
		}, false);
		*///?}
	}
}
