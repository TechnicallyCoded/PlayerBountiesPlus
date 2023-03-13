package com.tcoded.playerbountiesplus;

import com.tcoded.playerbountiesplus.command.BountyCommand;
import com.tcoded.playerbountiesplus.command.BountyCommandTabCompleter;
import com.tcoded.playerbountiesplus.files.BountiesFileManager;
import com.tcoded.playerbountiesplus.hooks.VaultHook;
import com.tcoded.playerbountiesplus.hooks.clan.AbstractClanHook;
import com.tcoded.playerbountiesplus.listener.DeathListener;
import com.tcoded.playerbountiesplus.utils.BountiesStorageUtils;
import com.tcoded.playerbountiesplus.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;;

import java.io.IOException;
import java.util.logging.Logger;

public final class PlayerBountiesPlus extends JavaPlugin {

    private final PluginDescriptionFile pluginInfo = getDescription();
    private final String pluginVersion = pluginInfo.getVersion();
    Logger logger = this.getLogger();

    private static PlayerBountiesPlus plugin;
    public BountiesFileManager bountiesFileManager;

    private VaultHook vault;
    private AbstractClanHook clanHook;

    @Override
    public void onEnable() {
        //Plugin startup logic
        plugin = this;

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.13")||Bukkit.getServer().getVersion().contains("1.14")||
                Bukkit.getServer().getVersion().contains("1.15")||Bukkit.getServer().getVersion().contains("1.16")||
                Bukkit.getServer().getVersion().contains("1.17")||Bukkit.getServer().getVersion().contains("1.18")||
                Bukkit.getServer().getVersion().contains("1.19"))){
            logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4This plugin is only supported on the Minecraft versions listed below:"));
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &41.13.x"));
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &41.14.x"));
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &41.15.x"));
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &41.16.x"));
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &41.17.x"));
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &41.18.x"));
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &41.19.x"));
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Is now disabling!"));
            logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &aA supported Minecraft version has been detected"));
            logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &6Continuing plugin startup"));
            logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
        }

        //Load the plugin configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Load bounties.yml
        this.bountiesFileManager = new BountiesFileManager();
        bountiesFileManager.BountiesFileManager(this);

        //Restore bounties to hashmap
        if (bountiesFileManager.getBountiesConfig().contains("bounties.data")){
            try {
                BountiesStorageUtils bountiesStorageUtils = new BountiesStorageUtils();
                bountiesStorageUtils.restoreBounties();
                bountiesStorageUtils.runStrippedBountyValuePopulate();
            } catch (IOException e) {
                logger.severe(ColorUtils.translateColorCodes("-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Failed to load data from bounties.yml!"));
                logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4See below for errors!"));
                logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Disabling Plugin!"));
                e.printStackTrace();
                logger.severe(ColorUtils.translateColorCodes("-------------------------------------------"));
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }

        //Load plugin hooks
        this.vault = new VaultHook(this);
        if (!this.vault.init()){
            logger.severe(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Failed to hooks into Vault"));
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4This plugin requires Vault"));
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Please install Vault and restart your server!"));
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Disabling Plugin!"));
            Bukkit.getPluginManager().disablePlugin(this);
            logger.severe(ColorUtils.translateColorCodes("-------------------------------------------"));
            return;
        }else {
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &aSuccessfully hooked into Vault"));
            logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &6Continuing startup"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }
        this.clanHook = AbstractClanHook.findClanHook(this);
        if (clanHook == null){
            logger.severe(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Failed to hooks into ClansLite"));
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4This plugin requires ClansLite"));
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Please install ClansLite and restart your server!"));
            logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Disabling Plugin!"));
            logger.severe(ColorUtils.translateColorCodes("-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &aSuccessfully hooked into ClansLite"));
            logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &6Continuing startup"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Register commands
        this.getCommand("bounty").setExecutor(new BountyCommand());

        //Register tab completer
        this.getCommand("bounty").setTabCompleter(new BountyCommandTabCompleter());

        //Register listeners
        this.getServer().getPluginManager().registerEvents(new DeathListener(), this);

        //Plugin startup message
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin by: &b&lLoving11ish & TCoded"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3has been loaded successfully"));
        logger.info(ColorUtils.translateColorCodes("&6ClansLite: &3Plugin Version: &d&l" + pluginVersion));
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
    }

    @Override
    public void onDisable(){
        //Plugin shutdown logic
        BountiesStorageUtils bountiesStorageUtils = new BountiesStorageUtils();

        //Safely stop the background tasks if running
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3Plugin by: &b&lLoving11ish & TCoded"));
        try {
            if (Bukkit.getScheduler().isCurrentlyRunning(bountiesStorageUtils.taskID1)||Bukkit.getScheduler().isQueued(bountiesStorageUtils.taskID1)){
                Bukkit.getScheduler().cancelTask(bountiesStorageUtils.taskID1);
            }
            logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3Background tasks have disabled successfully!"));
        }catch (Exception e){
            logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3Background tasks have disabled successfully!"));
        }

        //Save Bounties HashMap to bounties.yml
        if (!bountiesStorageUtils.getRawBountiesList().isEmpty()){
            try {
                bountiesStorageUtils.saveBounties();
                logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3All bounties saved to bounties.yml successfully!"));
            } catch (IOException e) {
                logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Failed to save bounties to bounties.yml!"));
                logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4See below error for reason!"));
                e.printStackTrace();
            }
        }

        //Final plugin shutdown message
        logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3Plugin Version: &d&l" + pluginVersion));
        logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3Has been shutdown successfully"));
        logger.info(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3Goodbye!"));
        logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
    }

    public static PlayerBountiesPlus getPlugin() {
        return plugin;
    }

    public VaultHook getVaultHook() {
        return this.vault;
    }

    public AbstractClanHook getClanHook() {
        return clanHook;
    }
}
