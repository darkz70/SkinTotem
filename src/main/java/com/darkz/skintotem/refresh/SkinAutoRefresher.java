package com.darkz.skintotem.refresh;

import com.darkz.skintotem.client.SkinTotemModClient;
import com.darkz.skintotem.config.SkinTotemModConfig;
import com.darkz.skintotem.doll.manager.StandardTotemDollManager;
import java.util.concurrent.*;

public class SkinAutoRefresher {

	private static ScheduledExecutorService scheduler;

	public static void start() {
		stop();
		SkinTotemModConfig config = SkinTotemModConfig.getInstance();
		if (!config.isAutoRefreshEnabled()) {
			return;
		}
		int intervalMinutes = Math.max(1, config.getAutoRefreshIntervalMinutes());
		scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "SkinTotem-AutoRefresh");
			t.setDaemon(true);
			return t;
		});
		scheduler.scheduleAtFixedRate(() -> {
			try {
				SkinTotemModClient.LOGGER.info("[SkinTotem] Auto-refreshing skin...");
				StandardTotemDollManager.initializeStandardDollData();
			} catch (Exception e) {
				SkinTotemModClient.LOGGER.error("[SkinTotem] Auto-refresh failed:", e);
			}
		}, intervalMinutes, intervalMinutes, TimeUnit.MINUTES);
		SkinTotemModClient.LOGGER.info("[SkinTotem] Auto-refresh started, interval: {} min", intervalMinutes);
	}

	public static void stop() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdownNow();
			scheduler = null;
		}
	}

	public static void restart() {
		start();
	}
}
