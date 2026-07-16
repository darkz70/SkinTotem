package com.darkz.skintotem.mixin.modmenu;

import com.terraformersmc.modmenu.util.mod.fabric.Fabric;
import java.util.*;
import net.fabricmc.loader.api.metadata.Metadata;
import com.darkz.skintotem.SkinTotem;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(Fabric.class)
public class FabricMixin {

	@Shadow(remap = false) @Final protected Metadata metadata;
	@Unique
	private static final Map<String, List<String>> MODEL_AUTHORS = Map.of(
			"Kreo_gen", List.of("gnom", "mini_3d", "parrot", "player_bucket", "pots", "rat", "stairs", "wheelchair")
	);

	//? if >=1.21 {
	@Dynamic
	@Inject(at = @At("RETURN"), method = "getContributors", remap = false)
	private void addMoreContributors(CallbackInfoReturnable<Map<String, Collection<String>>> cir) {
		if (!SkinTotem.MOD_ID.equals(this.metadata.getId())) {
			return;
		}
		Map<String, Collection<String>> map = cir.getReturnValue();
		MODEL_AUTHORS.forEach((nickname, models) -> {
			this.addBuiltinCustomModelAuthor(map, nickname, models);
		});
	}

	@Unique
	private void addBuiltinCustomModelAuthor(Map<String, Collection<String>> map, @SuppressWarnings("all") String nickname, List<String> models) {
		map.put(nickname + " " + Arrays.toString(models.toArray()), List.of("Community Model Author"));
	}
	//?} else {
	/*@Inject(at = @At("RETURN"), method = "getContributors", remap = false)
	private void addMoreContributors(CallbackInfoReturnable<List<String>> cir) {
		if (!SkinTotem.MOD_ID.equals(this.metadata.getId())) {
			return;
		}
		List<String> list = cir.getReturnValue();
		list.add(" ");
		list.add("Community Model Authors");
		MODEL_AUTHORS.forEach((nickname, models) -> {
			list.add(nickname + " " + Arrays.toString(models.toArray()));
		});
	}
	*///?}


}
