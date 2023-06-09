package com.tcoded.playerbountiesplus;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.playerbountiesplus.command.BountyCommand;
import com.tcoded.playerbountiesplus.hook.VaultHook;
import com.tcoded.playerbountiesplus.hook.team.AbstractTeamHook;
import com.tcoded.playerbountiesplus.listener.DeathListener;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerBountiesPlus extends JavaPlugin {

    // Utils
    private FoliaLib foliaLib;

    // Managers
    private BountyDataManager bountyDataManager;

    // Hooks
    private VaultHook vault;
    private AbstractTeamHook clanHook;

    @Override
    public void onEnable() {
        // Utils
        this.foliaLib = new FoliaLib(this);

        // Config
        saveDefaultConfig();

        // Managers
        this.bountyDataManager = new BountyDataManager(this);
        this.bountyDataManager.init();

        // Hooks
        this.vault = new VaultHook(this);
        boolean ecoPresent = this.vault.init();
        if (!ecoPresent) {
            getLogger().severe("No economy is present! Aborting startup!");
            return;
        }
        this.clanHook = AbstractTeamHook.findTeamHook(this);
        if (this.clanHook == null) {
            getLogger().warning("There is no supported team/clan/party plugin on the server! Feel free to request support for the plugin you use on GitHub or Discord!");
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

    public VaultHook getVaultHook() {
        return this.vault;
    }

    public AbstractTeamHook getTeamHook() {
        return clanHook;
    }

    public BountyDataManager getBountyDataManager() {
        return bountyDataManager;
    }

    public FoliaLib getFoliaLib() {
        return this.foliaLib;
    }
}
