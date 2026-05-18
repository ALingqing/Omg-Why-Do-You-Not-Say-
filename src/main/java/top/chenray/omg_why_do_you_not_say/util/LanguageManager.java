package top.chenray.omg_why_do_you_not_say.util;

import org.bukkit.configuration.file.YamlConfiguration;
import top.chenray.omg_why_do_you_not_say.OMGPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private final OMGPlugin plugin;
    private final Map<String, YamlConfiguration> languageCache = new HashMap<>();

    public LanguageManager(OMGPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 获取指定语言的翻译文本
     */
    public String getTranslation(String lang, String key) {
        YamlConfiguration config = loadLanguage(lang);
        if (config == null) return null;

        String value = config.getString(key);
        return value != null ? value : null;
    }

    /**
     * 获取指定语言的消息模板
     */
    public String getMessageTemplate(String lang, String actionType, int delay, String target) {
        String key;
        switch (actionType) {
            case "break" -> key = "message.break";
            case "place" -> key = "message.place";
            case "attack" -> key = "message.attack";
            default -> {
                return null;
            }
        }

        String translated = getTranslation(lang, key);
        if (translated != null) {
            return String.format(translated, delay, target);
        }

        // 默认中文回退
        String defaultTemplate;
        switch (actionType) {
            case "break" -> defaultTemplate = "你在 %d 秒前挖了一个 %s ！！！！";
            case "place" -> defaultTemplate = "你在 %d 秒前放置了一个 %s ！！！！";
            case "attack" -> defaultTemplate = "你在 %d 秒前被 %s 攻击了！！！！";
            default -> defaultTemplate = "";
        }
        return String.format(defaultTemplate, delay, target);
    }

    /**
     * 加载语言文件（从插件数据文件夹或 jar 资源）
     */
    private YamlConfiguration loadLanguage(String lang) {
        if (languageCache.containsKey(lang)) {
            return languageCache.get(lang);
        }

        // 首先尝试从插件数据文件夹加载
        File langFolder = new File(plugin.getDataFolder(), "lang");
        File langFile = new File(langFolder, lang + ".yml");

        YamlConfiguration config;
        if (langFile.exists()) {
            config = YamlConfiguration.loadConfiguration(langFile);
        } else {
            // 从 jar 资源加载
            InputStream in = plugin.getResource("lang/" + lang + ".yml");
            if (in == null) {
                // 回退到中文
                in = plugin.getResource("lang/zh_cn.yml");
                if (in == null) {
                    languageCache.put(lang, null);
                    return null;
                }
            }
            config = YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8));
        }

        languageCache.put(lang, config);
        return config;
    }

    /**
     * 将 jar 中的语言文件保存到插件数据文件夹
     */
    public void saveDefaultLanguages() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        // 语言文件列表
        String[] languages = {
            "ang_gb", "ar_ae", "ar_sa", "as_in", "bbc_id", "bho_in", "bn_in",
            "bo_cn", "da_dk", "de_de", "en_ca", "en_gb", "en_pirate", "en_us",
            "eo_ww", "es_es", "fil_ph", "fr_fr", "ga_ie", "got_xx", "gu_in",
            "haw_us", "hi_in", "hmc_cn", "hni_cn", "is_is", "ja_jp", "km_kh",
            "kn_in", "ko_cn", "li_cn", "lzh_cn", "mai_in", "mi_nz", "ml_in",
            "mn_cn", "ms_my", "my_mm", "or_in", "pa_in", "pt_br", "pt_pt",
            "ru_ru", "sat_in", "si_lk", "sv_se", "ta_in", "te_in", "th_th",
            "ug_cn", "za_cn", "zh_cn", "zh_hk", "zh_tw"
        };

        for (String lang : languages) {
            File targetFile = new File(langFolder, lang + ".yml");
            if (!targetFile.exists()) {
                try {
                    InputStream in = plugin.getResource("lang/" + lang + ".yml");
                    if (in != null) {
                        plugin.saveResource("lang/" + lang + ".yml", false);
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("无法保存语言文件: " + lang);
                }
            }
        }
    }
}
