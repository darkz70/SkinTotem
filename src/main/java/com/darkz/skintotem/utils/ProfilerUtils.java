package com.darkz.skintotem.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.profiler.*;

public class ProfilerUtils {

	public static Profiler getProfiler() {
		/*? >=1.21.2 {*/
		return Profilers.get();
		/*?} else {*/ /*return Minecraft.getInstance().getProfiler(); *//*?}*/
	}

}
