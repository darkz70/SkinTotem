package com.darkz.skintotem.pack;

import java.util.concurrent.*;
import com.darkz.skintotem.atlas.manager.*;
import net.minecraft.resource.*;
import net.minecraft.util.*;
import net.fabricmc.fabric.api.resource.*;
import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.model.bb.manager.BlockBenchModelManager;
import com.darkz.skintotem.tag.manager.TagsManager;
import net.minecraft.util.profiler.*;

//? if >=1.21.9 {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
//?}

public class SkinTotemModReloadListener implements /*? if >=1.21.9 {*/ ResourceReloader /*?} else {*/ /*IdentifiableResourceReloadListener *//*?}*/ {

	public static void register() {
		//? if >=1.21.9 {
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(getFabricId(), new SkinTotemModReloadListener());
		//?} else {
		/*ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SkinTotemModReloadListener());
		 *///?}
	}

	/*? if <=1.21.8 {*//*@Override*//*?}*/
	public /*? if >=1.21.9 {*/ static /*?}*/ Identifier getFabricId() {
		return SkinTotemMod.id("%s-reload-listener".formatted(SkinTotemMod.MOD_ID));
	}

	//? if >=1.21.9 {
	@Override
	public CompletableFuture<Void> reload(Store store, Executor prepareExecutor, Synchronizer synchronizer, Executor applyExecutor) {
		return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(() -> {
			Profiler profiler = Profilers.get();
			profiler.push("listener");
			this.reloadStuff(synchronizer, store.getResourceManager(), prepareExecutor, applyExecutor);
			profiler.pop();
		}, applyExecutor);
	}
	//?} elif >=1.21.2 {
	/*@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
		return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(() -> {
			Profiler profiler = Profilers.get();
			profiler.push("listener");
			this.reloadStuff(synchronizer, manager, prepareExecutor, applyExecutor);
			profiler.pop();
		}, applyExecutor);
	}
	*///?} else {
	/*@Override
	public CompletableFuture<Void> reload(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(() -> {
			applyProfiler.startTick();
			applyProfiler.push("listener");
			this.reloadStuff(synchronizer, manager, prepareExecutor, applyExecutor);
			applyProfiler.pop();
			applyProfiler.endTick();
		}, applyExecutor);
	}

	*///?}

	private void reloadStuff(Synchronizer synchronizer, ResourceManager resourceManager, Executor prepareExecutor, Executor applyExecutor) {
		this.reloadAtlas(synchronizer, prepareExecutor, applyExecutor);
		BlockBenchModelManager.reload(resourceManager);
		TotemDollModelFinder.reload(resourceManager);
		TagsManager.reloadCustomModelIdsTags();
	}

	private void reloadAtlas(Synchronizer synchronizer, Executor prepareExecutor, Executor applyExecutor) {
		SkinTotemModAtlasSpriteManager.reload();
		SkinTotemModAtlasManager.stitchAndUpdate(SkinTotemModAtlasSpriteManager.getSprites(), synchronizer, prepareExecutor, applyExecutor, null);
	}
}
