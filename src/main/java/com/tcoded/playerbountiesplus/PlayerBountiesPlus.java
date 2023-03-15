package com.tcoded.playerbountiesplus;

import com.tcoded.playerbountiesplus.command.BountyCommand;
import com.tcoded.playerbountiesplus.hook.VaultHook;
import com.tcoded.playerbountiesplus.hook.clan.AbstractClanHook;
import com.tcoded.playerbountiesplus.listener.DeathListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PlayerBountiesPlus extends JavaPlugin {

    private File bountiesFile;
    private FileConfiguration bountiesConfig;

    private HashMap<UUID, Integer> bounties;
    private VaultHook vault;
    private AbstractClanHook clanHook;

    @Override
    public void onEnable() {
        // Init
        this.bounties = new HashMap<>();

        // Config
        saveDefaultConfig();

        // Bounties file
        String bountiesFileName = "bounties.yml";
        bountiesFile = new File(this.getDataFolder(), bountiesFileName);
        if (!bountiesFile.exists()) this.saveResource(bountiesFileName, false);
        bountiesConfig = YamlConfiguration.loadConfiguration(bountiesFile);

        // Load bounties
        Set<String> keys = bountiesConfig.getKeys(false);
        for (String key: keys) {
            try {
                this.bounties.put(UUID.fromString(key), bountiesConfig.getInt(key));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Hooks
        this.vault = new VaultHook(this);
        boolean ecoPresent = this.vault.init();
        if (!ecoPresent) {
            getLogger().severe("No economy is present! Aborting startup!");
            return;
        }
        this.clanHook = AbstractClanHook.findClanHook(this);
        if (this.clanHook == null) {
            getLogger().severe("There is no supported clans plugin on the server!");
            return;
        }

        // Commands
        PluginCommand bountyCmd = this.getCommand("bounty");
        BountyCommand bountyExec = new BountyCommand(this);
        bountyCmd.setExecutor(bountyExec);
        bountyCmd.setTabCompleter(bountyExec);

        // Listener
        this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public HashMap<UUID, Integer> getBounties() {
        return this.bounties;
    }

    public void saveBounties() {
        // Clear existing
        Set<String> keys = bountiesConfig.getKeys(false);
        for (String key: keys) {
            bountiesConfig.set(key, null);
        }

        // Write changes
        for (Map.Entry<UUID, Integer> entry : this.bounties.entrySet()) {
            this.bountiesConfig.set(entry.getKey().toString(), entry.getValue());
        }

        // Save to file
        try {
            this.bountiesConfig.save(this.bountiesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VaultHook getVaultHook() {
        return this.vault;
    }

    public AbstractClanHook getClanHook() {
        return clanHook;
    }
}
