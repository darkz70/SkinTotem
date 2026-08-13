package com.darkz.skintotem.entrypoint;

//? if fabric {

import net.fabricmc.api.ClientModInitializer;
import com.darkz.skintotem.client.SkinTotemClient;

public class ClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SkinTotemClient.onInitializeClient();
	}
}

//?} elif forge {

/*import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.modmenu.ModMenuIntegration;
import net.minecraftforge.fml.ModLoadingContext;

public class ClientEntrypoint {

	public static void onInitializeClient() {
		SkinTotemClient.onInitializeClient();
		new ModMenuIntegration().register(ModLoadingContext.get().getActiveContainer());
	}

}

*///?}
