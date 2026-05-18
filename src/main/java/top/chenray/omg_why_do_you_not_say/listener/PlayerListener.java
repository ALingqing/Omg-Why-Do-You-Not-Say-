package top.chenray.omg_why_do_you_not_say.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
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
     * 获取友好的方块名称（将下划线替换为空格）
     */
    private String getFriendlyBlockName(Block block) {
        return block.getType().name().toLowerCase().replace('_', ' ');
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
            String message = languageManager.getMessageTemplate(lang, actionType, delaySeconds, targetName);

            if (message == null) {
                // 最后的回退
                switch (actionType) {
                    case "break" -> message = "你在 " + delaySeconds + " 秒前挖了一个 " + targetName + " ！！！！";
                    case "place" -> message = "你在 " + delaySeconds + " 秒前放置了一个 " + targetName + " ！！！！";
                    case "attack" -> message = "你在 " + delaySeconds + " 秒前被 " + targetName + " 攻击了！！！！";
                }
            }

            TextComponent component = new TextComponent(message);
            component.setColor(ChatColor.RED);
            component.setBold(true);

            player.spigot().sendMessage(component);
        }, delaySeconds * 20L);
    }
}
