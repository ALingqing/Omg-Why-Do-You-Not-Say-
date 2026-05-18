package top.chenray.omg_why_do_you_not_say.config;

import org.bukkit.configuration.file.FileConfiguration;
import top.chenray.omg_why_do_you_not_say.OMGPlugin;

public class ConfigManager {

    private final OMGPlugin plugin;
    private boolean enabled;
    private int delaySeconds;
    private boolean notifyBreak;
    private boolean notifyPlace;
    private boolean notifyAttack;
    private String language;

    public ConfigManager(OMGPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        this.enabled = config.getBoolean("enabled", true);
        this.delaySeconds = Math.max(1, Math.min(10, config.getInt("delaySeconds", 3)));
        this.notifyBreak = config.getBoolean("notifyBreak", true);
        this.notifyPlace = config.getBoolean("notifyPlace", true);
        this.notifyAttack = config.getBoolean("notifyAttack", true);
        this.language = config.getString("language", "auto");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public boolean isNotifyBreak() {
        return notifyBreak;
    }

    public boolean isNotifyPlace() {
        return notifyPlace;
    }

    public boolean isNotifyAttack() {
        return notifyAttack;
    }

    public String getLanguage() {
        return language;
    }
}
