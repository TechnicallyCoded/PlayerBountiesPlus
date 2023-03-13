package com.tcoded.playerbountiesplus.files;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.utils.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class BountiesFileManager {

    private PlayerBountiesPlus plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    Logger logger = PlayerBountiesPlus.getPlugin().getLogger();

    public void BountiesFileManager(PlayerBountiesPlus plugin){
        this.plugin = plugin;
        saveDefaultBountiesConfig();
    }

    public void reloadBountiesConfig(){

        if (this.configFile == null){
            this.configFile = new File(plugin.getDataFolder(), "bounties.yml");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("bounties.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getBountiesConfig(){
        if (this.dataConfig == null){
            this.reloadBountiesConfig();
        }
        return this.dataConfig;
    }

    public void saveBountiesConfig() {
        if (this.dataConfig == null||this.configFile == null){
            return;
        }
        try {
            this.getBountiesConfig().save(this.configFile);
        }catch (IOException e){
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Could not save bounties.yml"));
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Check the below message for the reasons!"));
            e.printStackTrace();
        }
    }

    public void saveDefaultBountiesConfig(){
        if (this.configFile == null){
            this.configFile = new File(plugin.getDataFolder(), "bounties.yml");
        }
        if (!this.configFile.exists()){
            this.plugin.saveResource("bounties.yml", false);
        }
    }
}
