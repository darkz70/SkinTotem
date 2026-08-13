package com.darkz.skintotem.entrypoint;

//? if fabric {

import net.fabricmc.api.ModInitializer;
import com.darkz.skintotem.SkinTotem;

public class CommonEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		SkinTotem.onInitialize();
	}
}

//?} elif forge {

/*import com.darkz.skintotem.SkinTotem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(SkinTotem.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint() {
		SkinTotem.onInitialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientEntrypoint::onInitializeClient);
	}

}

*///?}
