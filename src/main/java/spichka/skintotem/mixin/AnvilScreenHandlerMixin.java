package spichka.skintotem.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.util.thread.ThreadExecutor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spichka.skintotem.SkinLoader;
import spichka.skintotem.SkinTotem;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin {

    @Shadow @Final private Property levelCost;
    @Shadow private String newItemName;

    @Inject(method = "updateResult", at = @At("HEAD"))
    private void skintotem$onUpdateResult(CallbackInfo ci) {
        AnvilScreenHandler self = (AnvilScreenHandler)(Object)this;
        ItemStack inputStack = self.getSlot(0).getStack();
        ItemStack outputStack = self.getSlot(2).getStack();

        if (inputStack.getItem() == Items.TOTEM_OF_UNDYING && outputStack.getItem() == Items.TOTEM_OF_UNDYING) {
            if (newItemName != null && !newItemName.isEmpty()) {
                NbtCompound nbt = new NbtCompound();
                nbt.putString("username", newItemName);
                outputStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
                SkinTotem.LOGGER.info("Totem renamed to {}. Triggering skin load.", newItemName);

                // Asynchronously load skin on the client thread
                MinecraftClient.getInstance().getRunLoop().send(() -> SkinLoader.loadSkin(newItemName));
            }
        }
    }
}
