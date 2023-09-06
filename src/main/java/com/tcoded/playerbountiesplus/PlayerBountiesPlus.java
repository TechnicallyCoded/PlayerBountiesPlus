package com.tcoded.playerbountiesplus;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.playerbountiesplus.command.BountyCommand;
import com.tcoded.playerbountiesplus.command.PlayerBountiesPlusAdminCmd;
import com.tcoded.playerbountiesplus.hook.VaultHook;
import com.tcoded.playerbountiesplus.hook.team.AbstractTeamHook;
import com.tcoded.playerbountiesplus.listener.DeathListener;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import com.tcoded.playerbountiesplus.util.LangUtil;
import com.tcoded.updatechecker.SimpleUpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class PlayerBountiesPlus extends JavaPlugin {

    // Utils
    private FoliaLib foliaLib;
    private LangUtil langUtil;

    // Managers
    private BountyDataManager bountyDataManager;

    // Hooks
    private VaultHook vault;
    private AbstractTeamHook teamHook;

    @Override
    public void onEnable() {
        // Config
        saveDefaultConfig();
        FileConfiguration config = this.getConfig();

        // Utils
        this.foliaLib = new FoliaLib(this);
        this.reloadLang();

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
        this.teamHook = AbstractTeamHook.findTeamHook(this);
        if (this.teamHook == null) {
            getLogger().warning("There is no supported team/clan/party plugin on the server! Feel free to request support for the plugin you use on GitHub or Discord!");
        }

        // Commands
        PluginCommand bountyCmd = this.getCommand("bounty");
        if (bountyCmd != null) {
            BountyCommand bountyExec = new BountyCommand(this);
            bountyCmd.setExecutor(bountyExec);
            bountyCmd.setTabCompleter(bountyExec);
        }
        PluginCommand adminCmd = this.getCommand("playerbountiesplusadmin");
        if (adminCmd != null) {
            PlayerBountiesPlusAdminCmd adminExec = new PlayerBountiesPlusAdminCmd(this);
            adminCmd.setExecutor(adminExec);
            adminCmd.setTabCompleter(adminExec);
        }

        // Listener
        this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);

        // Update checker
        checkUpdate();

        // bStats
        Metrics metrics = new Metrics(this, 19617);
        metrics.addCustomChart(new SimplePie("config_lang", () -> langUtil.getLang()));
        metrics.addCustomChart(new SimplePie("datafolder_customlang", () -> Boolean.toString(langUtil.isCustomLang())));
        metrics.addCustomChart(new SimplePie("hook_teamhook", () -> teamHook == null ? "None" : teamHook.getPluginName() + " (" + teamHook.getAuthor() + ")"));
        metrics.addCustomChart(new SimplePie("server_isfolia", () -> Boolean.toString(foliaLib.isFolia())));

        List<Plugin> plugins = ImmutableList.copyOf(this.getServer().getPluginManager().getPlugins());
        findPluginWithQuery(plugins, metrics, "team");
        findPluginWithQuery(plugins, metrics, "teams");
        findPluginWithQuery(plugins, metrics, "clan");
        findPluginWithQuery(plugins, metrics, "clans");
        findPluginWithQuery(plugins, metrics, "party");
        findPluginWithQuery(plugins, metrics, "parties");
        findPluginWithQuery(plugins, metrics, "guild");
        findPluginWithQuery(plugins, metrics, "guilds");
    }

    public void reloadLang() {
        this.langUtil = new LangUtil(this, this.getConfig().getString("lang", "en_us").toLowerCase());
    }

    public void checkUpdate() {
        SimpleUpdateChecker.checkUpdate(this, ChatColor.translateAlternateColorCodes('&', "&f[&bPlayerBountiesPlus&f] "), 108637);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public VaultHook getVaultHook() {
        return this.vault;
    }

    public AbstractTeamHook getTeamHook() {
        return teamHook;
    }

    public BountyDataManager getBountyDataManager() {
        return bountyDataManager;
    }

    public FoliaLib getFoliaLib() {
        return this.foliaLib;
    }

    public LangUtil getLang() {
        return this.langUtil;
    }

    // Utils

    private static void findPluginWithQuery(List<Plugin> plugins, Metrics metrics, String pluginNameQuery) {
        String firstPluginFound = plugins.stream().filter(p -> p.getName().toLowerCase().contains(pluginNameQuery)).findFirst().map(p -> {
            List<String> authors = p.getDescription().getAuthors();
            return p.getName() + " (" + (authors.isEmpty() ? "N/A" : authors.get(0)) + ")";
        }).orElse(null);

        if (firstPluginFound == null) return;

        metrics.addCustomChart(new SimplePie("plugins_query_" + pluginNameQuery, () -> firstPluginFound));
    }
}
