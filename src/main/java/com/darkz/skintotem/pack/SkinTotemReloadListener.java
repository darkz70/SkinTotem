package com.darkz.skintotem.pack;

import java.util.concurrent.*;
import com.darkz.skintotem.atlas.manager.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.*;
//? if fabric {
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
//?}
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.loader.SkinTotemLoader;
import com.darkz.skintotem.model.bb.manager.BlockBenchModelManager;
import com.darkz.skintotem.tag.manager.TagsManager;
import net.minecraft.util.profiling.*;
import net.minecraft.util.profiling.ProfilerFiller;


public class SkinTotemReloadListener implements /*? if fabric {*/ IdentifiableResourceReloadListener /*?} else {*/ /*PreparableReloadListener *//*?}*/ {

	public static void register() {
		SkinTotemLoader.registerReloadListener(getId(), new SkinTotemReloadListener());
	}

	public static ResourceLocation getId() {
		return SkinTotem.id("%s-reload-listener".formatted(SkinTotem.MOD_ID));
	}

	//? if fabric {
	@Override
	public ResourceLocation getFabricId() {
		return getId();
	}
	//?}

	@Override
	public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier synchronizer, ResourceManager manager, ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return synchronizer.wait(Unit.INSTANCE).thenRunAsync(() -> {
			applyProfiler.startTick();
			applyProfiler.push("listener");
			this.reloadStuff(synchronizer, manager, prepareExecutor, applyExecutor);
			applyProfiler.pop();
			applyProfiler.endTick();
		}, applyExecutor);
	}


	private void reloadStuff(PreparationBarrier synchronizer, ResourceManager resourceManager, Executor prepareExecutor, Executor applyExecutor) {
		this.reloadAtlas(synchronizer, prepareExecutor, applyExecutor);
		BlockBenchModelManager.reload();
		SkinTotemModelFinder.reload(resourceManager);
		TagsManager.reloadCustomModelIdsTags();
	}

	private void reloadAtlas(PreparationBarrier synchronizer, Executor prepareExecutor, Executor applyExecutor) {
		SkinTotemAtlasSpriteManager.reload();
		SkinTotemAtlasManager.stitchAndUpdate(SkinTotemAtlasSpriteManager.getSprites(), synchronizer, prepareExecutor, applyExecutor, null);
	}
}
