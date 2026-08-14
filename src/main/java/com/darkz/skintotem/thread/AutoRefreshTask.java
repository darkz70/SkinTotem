package com.darkz.skintotem.thread;

import java.util.concurrent.*;
import com.darkz.skintotem.client.SkinTotemClient;
import com.darkz.skintotem.client.command.builder.CommandTextBuilder;
import com.darkz.skintotem.config.SkinTotemConfig;
import com.darkz.skintotem.doll.manager.SkinTotemManager;
import com.darkz.skintotem.utils.CommandUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * Periodically re-downloads every currently loaded skin (Mojang, TLauncher, Ely.by, NameMC, URL, ...)
 * so that dolls stay up to date without the player having to run "/skin-totem refresh all" manually.
 * Interval and on/off state are controlled via {@link SkinTotemConfig}.
 */
public class AutoRefreshTask {

	private static ScheduledExecutorService SCHEDULER;
	private static ScheduledFuture<?> CURRENT_TASK;
	private static volatile boolean RUNNING = false;

	public static void start() {
		if (SCHEDULER == null || SCHEDULER.isShutdown()) {
			SCHEDULER = Executors.newSingleThreadScheduledExecutor((r) -> {
				Thread thread = new Thread(r, "SkinTotem-AutoRefresh");
				thread.setDaemon(true);
				return thread;
			});
		}
		reschedule();
	}

	/**
	 * Call this after the "auto_refresh_enabled" or "auto_refresh_interval_minutes"
	 * config values change, so the new settings apply immediately without a client restart.
	 */
	public static void reschedule() {
		if (SCHEDULER == null || SCHEDULER.isShutdown()) {
			return;
		}

		if (CURRENT_TASK != null) {
			CURRENT_TASK.cancel(false);
			CURRENT_TASK = null;
		}

		SkinTotemConfig config = SkinTotemConfig.getInstance();
		if (!config.isAutoRefreshEnabled()) {
			return;
		}

		long intervalMinutes = Math.max(1, config.getAutoRefreshIntervalMinutes());

		CURRENT_TASK = SCHEDULER.scheduleWithFixedDelay(
				AutoRefreshTask::runRefresh,
				intervalMinutes,
				intervalMinutes,
				TimeUnit.MINUTES
		);
	}

	public static void stop() {
		if (CURRENT_TASK != null) {
			CURRENT_TASK.cancel(false);
			CURRENT_TASK = null;
		}
		if (SCHEDULER != null) {
			SCHEDULER.shutdownNow();
			SCHEDULER = null;
		}
	}

	private static void runRefresh() {
		if (RUNNING) {
			return;
		}
		RUNNING = true;

		try {
			Minecraft.getInstance().execute(() -> {
				Component startFeedback = CommandTextBuilder.startBuilder("command.auto_refresh.start").build();
				CommandUtils.sendMessage(startFeedback);
			});

			SkinTotemManager.reloadData((seconds) -> {
				RUNNING = false;
				Minecraft.getInstance().execute(() -> {
					Component endFeedback = CommandTextBuilder.startBuilder("command.auto_refresh.end", seconds).build();
					CommandUtils.sendMessage(endFeedback);
				});
			}).whenComplete((r, e) -> {
				RUNNING = false;
				if (e != null) {
					SkinTotemClient.LOGGER.error("Auto-refresh of skins failed: ", e);
				}
			});
		} catch (Exception e) {
			RUNNING = false;
			SkinTotemClient.LOGGER.error("Auto-refresh of skins failed: ", e);
		}
	}
}
