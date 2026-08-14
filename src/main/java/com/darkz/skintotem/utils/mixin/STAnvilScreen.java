package com.darkz.skintotem.utils.mixin;

import com.darkz.skintotem.gui.widget.tag.TagButtonWidget;
import com.darkz.skintotem.gui.widget.tag.TagMenuWidget;
import org.jetbrains.annotations.Nullable;

public interface STAnvilScreen {
	@Nullable
	TagButtonWidget st$getTagButtonWidget();

	@Nullable
	TagMenuWidget st$getTagMenuWidget();
}
