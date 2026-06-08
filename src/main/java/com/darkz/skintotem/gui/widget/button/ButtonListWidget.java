package com.darkz.skintotem.gui.widget.button;

import com.darkz.skintotem.gui.widget.list.AbstractSearchListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.*;

import com.darkz.skintotem.SkinTotemMod;

import java.util.*;
import org.jetbrains.annotations.NotNull;

public class ButtonListWidget extends AbstractSearchListWidget<ButtonListEntryWidget> {

	public ButtonListWidget(int x, int y, int width, int height, int buttonHeight) {
		super(x, y, width, height - 5, buttonHeight /*? if =1.20.1 {*/ /*+ 4 *//*?}*/);
	}

	@Override
	public int addEntry(ButtonListEntryWidget entry) {
		return super.addEntry(entry);
	}

	@Override
	public int getRowWidth() {
		return this.width - (5 * 2);
	}

	public int getRowLeft() {
		return this.getX() + (this.width / 2) - this.getRowWidth() / 2;
	}

	@Override
	protected boolean searched(String string, ButtonListEntryWidget child) {
		return child.getWidget().getMessage().toString().contains(string);
	}

	@Override
	protected @NotNull Comparator<ButtonListEntryWidget> getComparator() {
		return Comparator.comparing(a -> a.getWidget().getMessage().getString());
	}
}
