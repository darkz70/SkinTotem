package com.darkz.skintotem.thread;

import java.util.*;
import java.util.concurrent.*;
import com.darkz.skintotem.config.SkinTotemModConfig;

public class SkinTotemModTaskExecutor {

	public static ExecutorService MAIN_EXECUTOR = Executors.newFixedThreadPool(SkinTotemModConfig.getInstance().getParallelTasksCount());

	public static void reload() {
		int threadsCount = SkinTotemModConfig.getInstance().getParallelTasksCount();
		List<Runnable> runnables = MAIN_EXECUTOR.shutdownNow();
		MAIN_EXECUTOR = Executors.newFixedThreadPool(threadsCount);
		for (Runnable runnable : runnables) {
			MAIN_EXECUTOR.submit(runnable);
		}
	}

	public static void stop() {
		MAIN_EXECUTOR.shutdown();
	}

	public static CompletableFuture<Void> execute(Runnable runnable) {
		return CompletableFuture.runAsync(runnable, MAIN_EXECUTOR);
	}
}
