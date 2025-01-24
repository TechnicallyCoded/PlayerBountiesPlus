package com.tcoded.playerbountiesplus;

import com.google.common.collect.ImmutableList;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.lightlibs.updatechecker.SimpleUpdateChecker;
import com.tcoded.playerbountiesplus.command.BountyCommand;
import com.tcoded.playerbountiesplus.command.PlayerBountiesPlusAdminCmd;
import com.tcoded.playerbountiesplus.hook.currency.EconomyHook;
import com.tcoded.playerbountiesplus.hook.currency.DummyEcoHook;
import com.tcoded.playerbountiesplus.hook.currency.VaultHook;
import com.tcoded.playerbountiesplus.hook.placeholder.PlaceholderHook;
import com.tcoded.playerbountiesplus.hook.team.AbstractTeamHook;
import com.tcoded.playerbountiesplus.hook.team.TeamHook;
import com.tcoded.playerbountiesplus.listener.DeathListener;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import com.tcoded.playerbountiesplus.util.LangUtil;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class PlayerBountiesPlus extends JavaPlugin {

    // Instance
    private static PlayerBountiesPlus instance;

    public static PlayerBountiesPlus getInstance() {
        return instance;
    }

    // Utils
    private FoliaLib foliaLib;
    private LangUtil langUtil;

    // Managers
    private BountyDataManager bountyDataManager;

    // Hooks
    private EconomyHook ecoHook;
    private TeamHook teamHook;
    private PlaceholderHook placeholderHook;

    public PlayerBountiesPlus() {
        instance = this;
    }

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

        // Economy Hooks
        registerDefaultEcoHooks();
        if (!applyEcoHookRegistration()) return;

        // Team Hooks
        this.teamHook = TeamHook.findTeamHook(this);
        if (this.teamHook == null) {
            getLogger().warning("There is no supported team/clan/party plugin on the server! Feel free to request support for the plugin you use on GitHub or Discord!");
        }

        // Placeholder Hooks
        this.placeholderHook = PlaceholderHook.findPlaceholderHook(this);
        this.placeholderHook.enable();

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
        findPluginWithQuery(plugins, metrics, "party", "voteparty");
        findPluginWithQuery(plugins, metrics, "parties");
        findPluginWithQuery(plugins, metrics, "guild");
        findPluginWithQuery(plugins, metrics, "guilds");
    }

    public void reloadLang() {
        this.langUtil = new LangUtil(this, this.getConfig().getString("lang", "en_us").toLowerCase());
    }

    public void checkUpdate() {
        SimpleUpdateChecker.checkUpdate(
                this,
                ChatColor.translateAlternateColorCodes('&', "&f[&bPlayerBountiesPlus&f] "),
                108637,
                runnable -> this.getFoliaLib().getScheduler().runAsync(wt -> runnable.run())
        );
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        this.placeholderHook.disable();
    }

    public EconomyHook getEcoHook() {
        return this.ecoHook;
    }

    public TeamHook getTeamHook() {
        return teamHook;
    }

    public PlaceholderHook getPlaceholderHook() {
        return placeholderHook;
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

    private static void findPluginWithQuery(List<Plugin> plugins, Metrics metrics, String pluginNameQuery, String... excludeStrings) {
        String firstPluginFound = plugins.stream()
                .filter(p -> {
                    String lowerName = p.getName().toLowerCase();
                    // If the plugin name doesn't contain the query, skip
                    if (!lowerName.contains(pluginNameQuery)) return false;
                    // If the plugin name contains any of the exclude strings, skip
                    for (String excludeString : excludeStrings) {
                        if (lowerName.contains(excludeString)) return false;
                    }
                    return true;
                })
                .findFirst()
                .map(p -> {
                    List<String> authors = p.getDescription().getAuthors();
                    return p.getName() + " (" + (authors.isEmpty() ? "N/A" : authors.get(0)) + ")";
                }).orElse(null);

        if (firstPluginFound == null) return;

        metrics.addCustomChart(new SimplePie("plugins_query_" + pluginNameQuery, () -> firstPluginFound));
    }

    private void registerDefaultEcoHooks() {
        ServicesManager servicesManager = getServer().getServicesManager();

        // Unregister old eco hooks
        servicesManager.unregisterAll(this);

        // Register vault economy hook
        VaultHook vaultEcoHook = new VaultHook(this);
        boolean ecoPresent = vaultEcoHook.init();

        // Register or fallback
        if (ecoPresent) servicesManager.register(EconomyHook.class, vaultEcoHook, this, ServicePriority.Low);
        else servicesManager.register(EconomyHook.class, new DummyEcoHook(), this, ServicePriority.Lowest);
    }

    public boolean applyEcoHookRegistration() {
        // Get economy hook
        ServicesManager servicesManager = getServer().getServicesManager();
        RegisteredServiceProvider<EconomyHook> ecoHookProvider = servicesManager.getRegistration(EconomyHook.class);
        if (ecoHookProvider == null) {
            getLogger().severe("No economy hook is present! Aborting startup!");
            return false;
        }

        String hookName = ecoHookProvider.getProvider().getClass().getSimpleName();
        String hookPluginName = ecoHookProvider.getPlugin().getName();

        getLogger().info("Using economy hook: " + hookName + " from plugin: " + hookPluginName);
        this.ecoHook = ecoHookProvider.getProvider();

        return true;
    }
}
