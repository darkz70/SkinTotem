package com.darkz.skintotem.utils.tooltip;

import org.jetbrains.annotations.Nullable;

public interface IRequestableTooltipScreen {

	void st$requestTooltip(@Nullable TooltipRequest tooltipRequest);

	@Nullable
	TooltipRequest st$getCurrentRequest();

}
