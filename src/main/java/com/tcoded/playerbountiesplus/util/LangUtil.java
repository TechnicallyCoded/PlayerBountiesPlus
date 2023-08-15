package com.tcoded.playerbountiesplus.util;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class LangUtil {

    private final PlayerBountiesPlus plugin;
    private final File messagesFile;
    private final FileConfiguration messages;

    public enum SupportedLang {
        EN_US
    }

    public LangUtil(PlayerBountiesPlus plugin, String lang) {
        this.plugin = plugin;

        for (SupportedLang tmpLang : SupportedLang.values()) {
            plugin.saveResource("lang/" + tmpLang.name().toLowerCase() + ".yml", false);
        }

        this.messagesFile = new File(plugin.getDataFolder(), "lang/" + lang + ".yml");
        this.messages = YamlConfiguration.loadConfiguration(this.messagesFile);
    }

    public String get(String key) {
        return this.messages.getString(key);
    }

    public String getColored(String key) {
        return ChatColor.translateAlternateColorCodes('&', this.get(key));
    }

}
