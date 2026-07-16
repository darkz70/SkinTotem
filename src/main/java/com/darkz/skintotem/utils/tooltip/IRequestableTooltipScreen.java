package com.darkz.skintotem.utils.tooltip;

import org.jetbrains.annotations.Nullable;

public interface IRequestableTooltipScreen {

	void skinTotem$requestTooltip(@Nullable TooltipRequest tooltipRequest);

	@Nullable
	TooltipRequest skinTotem$getCurrentRequest();

}
