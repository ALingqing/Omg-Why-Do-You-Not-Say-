package top.chenray.omg_why_do_you_not_say.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * 调度工具 - 兼容 Spigot / Paper / Folia / Purpur
 */
public class SchedulerUtil {

    private static final boolean isFolia;

    static {
        boolean folia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            // 不是 Folia 服务端
        }
        isFolia = folia;
    }

    /**
     * 延迟执行任务（兼容 Folia 和非 Folia）
     */
    public static void runTaskLater(Plugin plugin, Runnable task, long delayTicks) {
        if (isFolia) {
            runFoliaTaskLater(plugin, task, delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    /**
     * 异步延迟执行任务（兼容 Folia 和非 Folia）
     */
    public static void runTaskLaterAsynchronously(Plugin plugin, Runnable task, long delayTicks) {
        if (isFolia) {
            runFoliaTaskLater(plugin, task, delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delayTicks);
        }
    }

    /**
     * 立即执行任务（兼容 Folia 和非 Folia）
     */
    public static void runTask(Plugin plugin, Runnable task) {
        if (isFolia) {
            runFoliaTaskLater(plugin, task, 1L);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /**
     * Folia 环境下使用 GlobalRegionScheduler 执行延迟任务
     */
    private static void runFoliaTaskLater(Plugin plugin, Runnable task, long delayTicks) {
        try {
            Object scheduler = Bukkit.class.getMethod("getGlobalRegionScheduler").invoke(null);
            Method runDelayed = scheduler.getClass().getMethod("runDelayed", Plugin.class, Runnable.class, long.class);
            runDelayed.invoke(scheduler, plugin, task, delayTicks);
        } catch (Exception e) {
            // 回退到同步调度
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    /**
     * 检查是否运行在 Folia 上
     */
    public static boolean isFolia() {
        return isFolia;
    }
}
