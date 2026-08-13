package com.darkz.skintotem.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import lombok.experimental.ExtensionMethod;
import com.darkz.skintotem.SkinTotem;
import com.darkz.skintotem.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.config.other.vector.Vec2i;
import com.darkz.skintotem.extension.ItemStackExtension;
import com.darkz.skintotem.gui.widget.info.*;
import com.darkz.skintotem.gui.widget.tag.*;
import com.darkz.skintotem.gui.widget.tag.TagMenuWidget.Renamer;
import com.darkz.skintotem.tag.Tag;
import com.darkz.skintotem.utils.mixin.SkinTotemAnvilScreen;

import org.jetbrains.annotations.Nullable;

@Mixin(AnvilScreen.class)
@ExtensionMethod(ItemStackExtension.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> implements SkinTotemAnvilScreen {

	@Shadow
	private EditBox name;
	@Unique
	@Nullable
	private DraggingTagButtonWidget skinTotem$tagButtonWidget = null;
	@Unique
	@Nullable
	private TagMenuWidget skinTotem$tagMenuWidget = null;
	@Unique
	@Nullable
	private SmallInfoWidget skinTotem$infoWidget = null;
	@Unique
	@Nullable
	private TipsWidget skinTotem$tipsWidget = null;
	@Unique
	private boolean skinTotem$currentVisibleState = false;

	public AnvilScreenMixin(AnvilMenu handler, Inventory playerInventory, Component title, ResourceLocation texture) {
		super(handler, playerInventory, title, texture);
	}

	@Shadow
	public abstract void resize(Minecraft client, int width, int height);

	@Shadow
	protected abstract void subInit();

	@Inject(at = @At("HEAD"), method = "subInit")
	private void setupTagMenu(CallbackInfo ci) {
		if (!SkinTotemConfig.getInstance().isModEnabled()) {
			return;
		}

		ItemStack stackOne = this.menu.getSlot(0).getItem();
		ItemStack stackTwo = this.menu.getSlot(2).getItem();
		boolean bl = SkinTotemClient.canProcess(stackOne) && !stackOne.isEmpty();

		//

		this.skinTotem$tagMenuWidget         = new TagMenuWidget(0, 0, new Renamer() {
			@Override
			public String getName() {
				return AnvilScreenMixin.this.name.getValue();
			}

			@Override
			public void setName(String name) {
				AnvilScreenMixin.this.name.setValue(name);
			}
		});
		this.skinTotem$tagMenuWidget.visible = this.skinTotem$currentVisibleState;
		if (this.skinTotem$tagMenuWidget.visible) {
			this.skinTotem$tagMenuWidget.updateButtons(stackTwo.isEmpty() ? stackOne : stackTwo);
		}

		//

		this.skinTotem$infoWidget         = new SmallInfoWidget(0, 0);
		this.skinTotem$infoWidget.visible = this.skinTotem$tagMenuWidget.visible;

		//

		this.skinTotem$tipsWidget         = new TipsWidget(0, 0);
		this.skinTotem$tipsWidget.visible = this.skinTotem$tagMenuWidget.visible;

		//

		Vec2i originalPos = SkinTotemConfig.getNewInstance().getTagButtonPos();
		this.skinTotem$tagButtonWidget         = new DraggingTagButtonWidget(
				Tag.simple('4'),
				this.leftPos,
				this.topPos,
				this.leftPos + originalPos.getX(),
				this.topPos + originalPos.getY(),
				0,
				0,
				(b) -> {
					this.skinTotem$currentVisibleState = b.isPressed();
					this.resize(this.minecraft, this.width, this.height);
		});
		this.skinTotem$tagButtonWidget.visible = bl;
		this.skinTotem$tagButtonWidget.setPressed(this.skinTotem$tagMenuWidget.visible);
		
		//

		if (this.skinTotem$tagMenuWidget.visible) {
			this.imageWidth = 176 + this.skinTotem$tagMenuWidget.getWidth() + 5 + this.skinTotem$infoWidget.getWidth();
		} else {
			this.imageWidth = 176;
		}
		
		//

		this.addRenderableWidget(this.skinTotem$tagMenuWidget);
		this.addRenderableOnly(this.skinTotem$infoWidget);
		this.addRenderableOnly(this.skinTotem$tipsWidget);
		this.addRenderableWidget(this.skinTotem$tagButtonWidget);
		
		//

		this.leftPos = (this.width - this.imageWidth) / 2;
		this.skinTotem$updateWidgets();
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setEditable(Z)V"), method = "subInit")
	private void fixingMojangBugOmg(EditBox instance, boolean editable, Operation<Void> original) {
		original.call(instance, this.menu.getSlot(0).hasItem());
	}

	@Unique
	private void skinTotem$updateWidgets() {
		SkinTotemConfig config = SkinTotemConfig.getInstance();
		if (!config.isModEnabled() || this.skinTotem$tagButtonWidget == null || this.skinTotem$tagMenuWidget == null || this.skinTotem$infoWidget == null || this.skinTotem$tipsWidget == null) {
			return;
		}

		//

		int tagMenuX = this.leftPos + 176 + 1;
		int tagMenuY = this.topPos;
		this.skinTotem$tagMenuWidget.setPosition(tagMenuX + 10, tagMenuY + 33);

		ItemStack stackOne = this.menu.getSlot(0).getItem();
		ItemStack stackTwo = this.menu.getSlot(2).getItem();
		ItemStack result = stackTwo.isEmpty() ? stackOne : stackTwo;
		if (result.is(Items.TOTEM_OF_UNDYING)) {
			this.skinTotem$tagMenuWidget.updateButtons(result);
			this.skinTotem$tagMenuWidget.updateCustomModelTagButtons(result);
		}

		//

		int infoWidgetX = tagMenuX + 50 + 2;
		int infoWidgetY = tagMenuY + 2;
		this.skinTotem$infoWidget.setPosition(infoWidgetX, infoWidgetY);
		this.skinTotem$tipsWidget.setPosition(infoWidgetX, infoWidgetY + this.skinTotem$infoWidget.getHeight() + 4);

		//

		Vec2i pos = config.getTagButtonPos();
		this.skinTotem$tagButtonWidget.setPosition(pos.getX() + this.leftPos, pos.getY() + this.topPos);
		this.skinTotem$tagButtonWidget.setOriginX(this.leftPos);
		this.skinTotem$tagButtonWidget.setOriginY(this.topPos);
	}

	@WrapOperation(
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"),
			method = "renderLabels"
	)
	private void swapBackgroundValue(GuiGraphics instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
		if (!SkinTotemConfig.getInstance().isModEnabled()) {
			original.call(instance, x1, y1, x2, y2, color);
			return;
		}
		original.call(instance, x1 - this.imageWidth + 176, y1, x2 - this.imageWidth + 176, y2, color);
	}

	@Inject(
			at = @At("TAIL"),
			method = "renderBg"
	)
	private void updateWidgetPositions(GuiGraphics context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		if (!SkinTotemConfig.getInstance().isModEnabled()) {
			return;
		}
		this.skinTotem$updateWidgets();
		if (this.skinTotem$tagMenuWidget != null && this.skinTotem$tagMenuWidget.visible) {
			int x = this.leftPos + 176 + 1;
			int y = this.topPos;
			DrawUtils.drawTexture(context, TagMenuWidget.BACKGROUND, x, y, 0, 0, 50, 166, 50, 166);
			DrawUtils.drawCenteredText(context, SkinTotem.text("tag_menu.title"), x + 9, y + 9 + 6 + 3, 32);
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"), method = "renderLabels")
	private int swapBackgroundValue(GuiGraphics instance, Font textRenderer, Component text, int x, int y, int color, Operation<Integer> original) {
		if (!SkinTotemConfig.getInstance().isModEnabled()) {
			return original.call(instance, textRenderer, text, x, y, color);
		}
		return original.call(instance, textRenderer, text, x - this.imageWidth + 176, y, color);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), method = "renderErrorIcon")
	private void swapBackgroundValue(GuiGraphics instance, ResourceLocation identifier, int x, int y, int a, int b, int c, int d, Operation<Void> original) {
		if (!SkinTotemConfig.getInstance().isModEnabled()) {
			original.call(instance, identifier, x, y, a, b, c, d);
		}
		original.call(instance, identifier, x, y, a - this.imageWidth + 176, b, c, d);
	}

	@Inject(at = @At("HEAD"), method = "slotChanged")
	private void checkTotem(AbstractContainerMenu handler, int slotId, ItemStack stack, CallbackInfo ci) {
		if (!SkinTotemConfig.getInstance().isModEnabled() || this.skinTotem$tagButtonWidget == null || this.skinTotem$tagMenuWidget == null) {
			return;
		}
		if (slotId == 0) {
			this.skinTotem$tagButtonWidget.visible = SkinTotemClient.canProcess(stack);
			if (!this.skinTotem$tagButtonWidget.visible && this.skinTotem$tagMenuWidget.visible) {
				this.skinTotem$tagButtonWidget.setPressed(false, true);
			}
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getHoverName()Lnet/minecraft/network/chat/Component;"), method = "slotChanged")
	private Component swapItemName(ItemStack stack, Operation<Component> original) {
		if (!SkinTotemClient.canProcess(stack)) {
			return original.call(stack);
		}
		Component customName = stack.getRealCustomName();
		if (customName == null) {
			return original.call(stack);
		}
		return customName;
	}

	@Override
	public @Nullable TagButtonWidget skinTotem$getTagButtonWidget() {
		return this.skinTotem$tagButtonWidget;
	}

	@Override
	public @Nullable TagMenuWidget skinTotem$getTagMenuWidget() {
		return this.skinTotem$tagMenuWidget;
	}
}
