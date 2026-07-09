package com.darkz.skintotem.refresh;

import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.doll.manager.StandardSkinTotemManager;
import java.util.concurrent.*;

public class SkinAutoRefresher {

	private static ScheduledExecutorService scheduler;
	private static final Object LOCK = new Object();

	public static void start() {
		synchronized (LOCK) {
			stop();
			SkinTotemModConfig config = SkinTotemConfig.getInstance();
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
				long startedAt = System.currentTimeMillis();
				try {
					SkinTotemClient.LOGGER.info("[SkinTotem] Auto-refreshing skin...");
					StandardSkinTotemManager.initializeStandardDollData();
					long elapsedMs = System.currentTimeMillis() - startedAt;
					SkinTotemClient.LOGGER.info("[SkinTotem] Auto-refresh SUCCESS ({} ms)", elapsedMs);
				} catch (Exception e) {
					long elapsedMs = System.currentTimeMillis() - startedAt;
					SkinTotemClient.LOGGER.error("[SkinTotem] Auto-refresh FAILED after {} ms:", elapsedMs, e);
				}
			}, intervalMinutes, intervalMinutes, TimeUnit.MINUTES);
			SkinTotemClient.LOGGER.info("[SkinTotem] Auto-refresh started, interval: {} min", intervalMinutes);
		}
	}

	public static void stop() {
		synchronized (LOCK) {
			if (scheduler != null && !scheduler.isShutdown()) {
				scheduler.shutdownNow();
				scheduler = null;
			}
		}
	}

	public static void restart() {
		start();
	}
}
