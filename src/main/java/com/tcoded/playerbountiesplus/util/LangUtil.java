package com.tcoded.playerbountiesplus.util;

import com.tcoded.legacycolorcodeparser.LegacyColorCodeParser;
import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class LangUtil {

    private final PlayerBountiesPlus plugin;
    private final File overrideMessagesFile;
    private final FileConfiguration messages;
    private final String lang;
    private final boolean customLang;

    public enum SupportedLang {
        DE_DE,
        EN_GB,
        EN_US,
        EN_UWU,
        ES_ES,
        FR_FR,
        HI_IN,
        RU_RU,
        ZH_CN,
        ;

        public static SupportedLang find(String name) {
            for (SupportedLang lang : SupportedLang.values()) {
                if (lang.name().equals(name)) return EN_US;
            }
            return null;
        }
    }

    public LangUtil(PlayerBountiesPlus plugin, String lang) {
        this.plugin = plugin;
        this.lang = lang;
        Logger logger = this.plugin.getLogger();

//        for (SupportedLang tmpLang : SupportedLang.values()) {
//            plugin.saveResource("lang/" + tmpLang.name().toLowerCase() + ".yml", false);
//        }

        // Resolve the path of the lang files we're going to use
        String englishFilePath = "lang/" + SupportedLang.EN_US.name().toLowerCase() + ".yml";
        String langFilePath = "lang/" + lang + ".yml";

        // Load internal default English file - Worst case scenario fallback
        InputStream internalEnglishDefaultFile = plugin.getResource(englishFilePath);
        if (internalEnglishDefaultFile == null) throw new IllegalStateException("Internal default English file could not be found!");
        FileConfiguration englishDefaults = YamlConfiguration.loadConfiguration(new InputStreamReader(internalEnglishDefaultFile));

        // Check if language is supported
        SupportedLang supportedLang = SupportedLang.find(lang.toUpperCase());
        if (supportedLang == null) {
            logger.severe(String.format("Unsupported language was found: %s!", lang));
            logger.info("You can contribute new languages by following the instructions at the top of the config.yml file :)");
            throw new IllegalStateException("Unsupported language in config.yml");
        }

        // Load user-specified internal language file
        InputStream internalLangFile = plugin.getResource(langFilePath);
        if (internalLangFile == null) throw new IllegalStateException(String.format("Internal language file could not be found! (Lang: %s)", lang));
        FileConfiguration defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(internalLangFile));
        defaults.setDefaults(englishDefaults);

        // Check if the user has created an override language file
        this.overrideMessagesFile = new File(plugin.getDataFolder(), langFilePath);
        if (this.overrideMessagesFile.exists()) {
            logger.warning(String.format("Using custom override language file for language: %s", lang));
            this.messages = YamlConfiguration.loadConfiguration(this.overrideMessagesFile);
            this.messages.setDefaults(defaults);
            this.customLang = true;
        } else {
            this.messages = defaults;
            this.customLang = false;
        }
    }

    public String get(String key) {
        if (this.messages == null) return "STARTUP-ERROR-CONTACT-DEV";
        return this.messages.getString(key);
    }

    public String getColored(String key) {
        String text = this.get(key);
        text = LegacyColorCodeParser.convertHexToLegacy('&', text);
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String getLang() {
        return lang;
    }

    public boolean isCustomLang() {
        return customLang;
    }
}
