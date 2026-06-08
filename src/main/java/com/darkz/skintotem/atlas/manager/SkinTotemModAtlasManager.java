package com.darkz.skintotem.atlas.manager;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.atlas.*;
import com.darkz.skintotem.atlas.stitch.*;
import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.thread.SkinTotemModTaskExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.server.packs.resources.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.*;

public class SkinTotemModAtlasManager {

	private static final StitchHooksManager STITCH_HOOKS_MANAGER = new StitchHooksManager();
	private static final AtomicInteger LATEST_ATLAS_VERSION = new AtomicInteger();
	public static final Identifier ATLAS_ID = SkinTotemMod.id("main_atlas.png");
	//? if >=1.21.11 {
	public static final RenderType ATLAS_RENDER_LAYER = RenderTypes.entityTranslucent(ATLAS_ID);
	//?} else {
	/*public static final RenderType ATLAS_RENDER_LAYER = RenderType.getEntityTranslucent(ATLAS_ID);
	*///?}
	@Nullable
	private static LockableAtlasTexture ATLAS_TEXTURE;

	@NotNull
	public static TextureAtlas createNotRegisteredInstance() {
		return new TextureAtlas(ATLAS_ID);
	}

	public static RenderType getRenderType() {
		return ATLAS_RENDER_LAYER;
	}

	public static LockableAtlasTexture getNullableAtlasTexture() {
		return ATLAS_TEXTURE;
	}

	public static void setAtlas(@NotNull TextureAtlas texture) {
		if (ATLAS_TEXTURE != null && ATLAS_TEXTURE.isLocked()) {
			LockableAtlasTexture atlasTexture = new LockableAtlasTexture(texture);
			ATLAS_TEXTURE.setUnlockHook(() -> set(atlasTexture));
			return;
		}
		set(new LockableAtlasTexture(texture));
	}

	@NotNull
	private static LockableAtlasTexture set(@NotNull LockableAtlasTexture texture) {
		TextureAtlas atlas = texture.getAtlas();
		ATLAS_TEXTURE = texture;
		Minecraft.getInstance().getTextureManager().registerTexture(atlas.getId(), atlas);
		return ATLAS_TEXTURE;
	}

	public static void stitchAndUpdate(Set<AtlasSprite> sprites, @Nullable OnAtlasStitched onAtlasStitched) {
		stitchAndUpdate(sprites, SkinTotemModTaskExecutor.MAIN_EXECUTOR, onAtlasStitched);
	}

	public static void stitchAndUpdate(Set<AtlasSprite> sprites, Executor executor, @Nullable OnAtlasStitched onAtlasStitched) {
		stitchAndUpdate(sprites, null, executor, Minecraft.getInstance(), onAtlasStitched);
	}

	public static void stitchAndUpdate(Set<AtlasSprite> sprites, @Nullable PreparableReloadListenerer.Synchronizer synchronizer, Executor prepareExecutor, Executor applyExecutor, @Nullable OnAtlasStitched onAtlasStitched) {
		int currentId = LATEST_ATLAS_VERSION.incrementAndGet();
		STITCH_HOOKS_MANAGER.addHook(onAtlasStitched);

		TextureAtlas atlasTexture = SkinTotemModAtlasManager.createNotRegisteredInstance();

		List<SpriteContents> contents = sprites.stream().map(AtlasSprite::getContents).filter(Objects::nonNull).toList();
		//? if >=1.21.9 {
		CompletableFuture<SpriteLoader.Preparations> future = CompletableFuture.supplyAsync(
				() -> SpriteLoader.fromAtlas(atlasTexture).stitch(contents, 0, prepareExecutor)
		);
		//?} else {
		/*CompletableFuture<SpriteLoader.Preparations> future = SpriteLoader.fromAtlas(atlasTexture)
				.stitch(contents, 0, prepareExecutor)
				.whenComplete();
		*///?}

		if (synchronizer != null) {
			future = future.thenCompose(synchronizer::whenPrepared);
		}

		AtlasStitchingContext stitchingContext = new AtlasStitchingContext(currentId, atlasTexture, sprites);
		future.thenAcceptAsync(stitchingContext::upload, applyExecutor);
	}

	public static void close() {
		if (ATLAS_TEXTURE == null) {
			return;
		}
		ATLAS_TEXTURE.getAtlas().close();
	}

	private record AtlasStitchingContext(int version, TextureAtlas atlas, Set<AtlasSprite> atlasSprites) {

		public void upload(SpriteLoader.Preparations result) {
			int latestAtlasVersion = LATEST_ATLAS_VERSION.get();
			if (this.version != latestAtlasVersion) {
				SkinTotemModClient.LOGGER.warn("Skipped atlas stitching, waiting \"{}\"", latestAtlasVersion);
				return;
			}
			//? if >=1.21.11 {
			this.atlas.create(result);
			//?} else {
			/*this.atlas.upload(result);
			*///?}
			this.atlasSprites.forEach(AtlasSprite::markUploaded);
			SkinTotemModAtlasManager.setAtlas(this.atlas);
			STITCH_HOOKS_MANAGER.runAllHooks();
 		}

	}

}
