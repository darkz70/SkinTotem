package com.darkz.skintotem.mixin;

//? if >=1.21.6 {
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.doll.data.TotemDollData;
import com.darkz.skintotem.doll.renderer.special.*;
import com.darkz.skintotem.extension.ItemStackExtension;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.special.SpecialGuiElementRenderState;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ExtensionMethod(ItemStackExtension.class)
@Mixin(GuiRenderer.class)
public class GuiRendererMixin {

	@Shadow
	@Final
	GuiRenderState state;

	@Shadow
	@Final
	private Immediate vertexConsumers;

	@Inject(at = @At("HEAD"), method = "prepareSpecialElement", cancellable = true)
	private void renderDoll(SpecialGuiElementRenderState elementState, int windowScaleFactor, CallbackInfo ci) {
		if (!(elementState instanceof TotemDollRenderState totemDollRenderState)) {
			return;
		}

		TotemDollData data = totemDollRenderState.data() == null ?
				totemDollRenderState.stack() == null ?
						null
						:
						totemDollRenderState.stack().getTotemDollData()
				:
				totemDollRenderState.data();

		if (data == null) {
			return;
		}

		TotemDollGuiElementRenderer guiRenderer = data.getGuiRenderer(this.vertexConsumers);
		guiRenderer.setActive(true);
		guiRenderer.render(totemDollRenderState, this.state, windowScaleFactor);
		ci.cancel();
	}

	@Inject(at = @At(value = "TAIL"), method = "prepareSpecialElements")
	private void clearUnusedRenderers(CallbackInfo ci) {
		TotemDollGuiElementRenderer.clearUnusedRenderers();
	}

	@Inject(at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"), method = "close")
	private void closeTotemDollRenderers(CallbackInfo ci) {
		TotemDollGuiElementRenderer.closeTotemRenderers();
	}

}

//?}