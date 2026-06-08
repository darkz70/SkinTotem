package com.darkz.skintotem.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.*;

public class ProfilerFillerUtils {

	public static ProfilerFiller getProfilerFiller() {
		/*? >=1.21.2 {*/
		return ProfilerFillers.get();
		/*?} else {*/ /*return Minecraft.getInstance().getProfilerFiller(); *//*?}*/
	}

}
