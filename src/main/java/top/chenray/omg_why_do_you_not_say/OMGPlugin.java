package top.chenray.omg_why_do_you_not_say;

import org.bukkit.plugin.java.JavaPlugin;
import top.chenray.omg_why_do_you_not_say.config.ConfigManager;
import top.chenray.omg_why_do_you_not_say.listener.PlayerListener;
import top.chenray.omg_why_do_you_not_say.util.LanguageManager;

import java.util.logging.Logger;

public class OMGPlugin extends JavaPlugin {

    private static OMGPlugin instance;
    private static final Logger LOGGER = Logger.getLogger("OMG_WHY_DO_YOU_NOT_SAY");
    private ConfigManager configManager;
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        instance = this;

        // 保存默认配置
        saveDefaultConfig();

        // 初始化配置管理器
        this.configManager = new ConfigManager(this);

        // 初始化语言管理器
        this.languageManager = new LanguageManager(this);

        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        LOGGER.info("OMG, WHY DO YOU NOT SAY!!!!!!! 插件已加载");
    }

    @Override
    public void onDisable() {
        LOGGER.info("OMG, WHY DO YOU NOT SAY!!!!!!! 插件已卸载");
    }

    public static OMGPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public static Logger getPluginLogger() {
        return LOGGER;
    }
}
