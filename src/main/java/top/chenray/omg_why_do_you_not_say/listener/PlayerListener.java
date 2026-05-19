package top.chenray.omg_why_do_you_not_say.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import top.chenray.omg_why_do_you_not_say.OMGPlugin;
import top.chenray.omg_why_do_you_not_say.config.ConfigManager;
import top.chenray.omg_why_do_you_not_say.util.LanguageManager;
import top.chenray.omg_why_do_you_not_say.util.SchedulerUtil;

public class PlayerListener implements Listener {

    private final OMGPlugin plugin;
    private final ConfigManager configManager;
    private final LanguageManager languageManager;

    public PlayerListener(OMGPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.languageManager = plugin.getLanguageManager();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!configManager.isEnabled() || !configManager.isNotifyBreak()) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();
        String blockName = getFriendlyBlockName(block);
        int delay = configManager.getDelaySeconds();

        scheduleDelayedMessage(player, "break", blockName, delay);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!configManager.isEnabled() || !configManager.isNotifyPlace()) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();
        String blockName = getFriendlyBlockName(block);
        int delay = configManager.getDelaySeconds();

        scheduleDelayedMessage(player, "place", blockName, delay);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!configManager.isEnabled() || !configManager.isNotifyAttack()) return;
        if (!(event.getEntity() instanceof Player player)) return;

        String attackerName;
        if (event.getDamager() instanceof Player) {
            attackerName = ((Player) event.getDamager()).getName();
        } else {
            attackerName = event.getDamager().getName();
            if (attackerName == null || attackerName.isEmpty()) {
                attackerName = event.getDamager().getType().name().toLowerCase().replace('_', ' ');
            }
        }

        int delay = configManager.getDelaySeconds();
        scheduleDelayedMessage(player, "attack", attackerName, delay);
    }

    /**
     * 获取方块注册名（如 grass_block）
     */
    private String getFriendlyBlockName(Block block) {
        return block.getType().name().toLowerCase();
    }

    /**
     * 获取玩家语言代码
     */
    private String getPlayerLanguage(Player player) {
        String configuredLang = configManager.getLanguage();
        if (!"auto".equalsIgnoreCase(configuredLang)) {
            return configuredLang;
        }
        String locale = player.getLocale();
        if (locale == null || locale.isEmpty()) {
            return "zh_cn";
        }
        return locale.replace('-', '_').toLowerCase();
    }

    /**
     * 延迟发送消息到玩家
     */
    private void scheduleDelayedMessage(Player player, String actionType, String targetName, int delaySeconds) {
        SchedulerUtil.runTaskLater(plugin, () -> {
            if (!player.isOnline()) return;

            String lang = getPlayerLanguage(player);

            // 尝试翻译方块/实体名称
            String displayName = targetName;
            if ("break".equals(actionType) || "place".equals(actionType)) {
                String translated = languageManager.getBlockName(lang, targetName);
                if (translated != null) {
                    displayName = translated;
                } else {
                    displayName = targetName.replace('_', ' ');
                }
            }

            String message = languageManager.getMessageTemplate(lang, actionType, delaySeconds, displayName);

            if (message == null) {
                // 最后的回退
                switch (actionType) {
                    case "break" -> message = "你在 " + delaySeconds + " 秒前挖了一个 " + displayName + " ！！！！";
                    case "place" -> message = "你在 " + delaySeconds + " 秒前放置了一个 " + displayName + " ！！！！";
                    case "attack" -> message = "你在 " + delaySeconds + " 秒前被 " + displayName + " 攻击了！！！！";
                }
            }

            // §c = 红色, §l = 粗体
            player.sendTitle("§c§l" + message, "", 10, 70, 20);
        }, delaySeconds * 20L);
    }
}
