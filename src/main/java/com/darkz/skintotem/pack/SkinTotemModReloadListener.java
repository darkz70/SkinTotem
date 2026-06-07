package com.darkz.skintotem.pack;

import java.util.concurrent.*;
import com.darkz.skintotem.atlas.manager.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.fabricmc.fabric.api.resource.*;
import com.darkz.skintotem.SkinTotemMod;
import com.darkz.skintotem.model.bb.manager.BlockBenchModelManager;
import com.darkz.skintotem.tag.manager.TagsManager;
import net.minecraft.util.profiler.Profilers;

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
	public /*? if >=1.21.9 {*/ static /*?}*/ ResourceLocation getFabricId() {
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
	//?} elif

