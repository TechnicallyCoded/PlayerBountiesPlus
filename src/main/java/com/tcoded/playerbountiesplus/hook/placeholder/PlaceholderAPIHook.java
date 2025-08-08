package com.tcoded.playerbountiesplus.hook.placeholder;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIHook extends PlaceholderExpansion implements PlaceholderHook {

    private final PlayerBountiesPlus pbpPlugin;

    public PlaceholderAPIHook(PlayerBountiesPlus pbpPlugin, Plugin papiPlugin) {
        this.pbpPlugin = pbpPlugin;
        if (!(papiPlugin instanceof PlaceholderAPIPlugin)) {
            throw new IllegalArgumentException("Plugin is not PlaceholderAPI");
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return pbpPlugin.getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return pbpPlugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return pbpPlugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public void enable() {
        this.register();
    }

    @Override
    public void disable() {
        this.unregister();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equals("bounty")) {
            return String.valueOf(pbpPlugin.getBountyDataManager().getBounty(player.getUniqueId()));
        } else if (params.equals("hasbounty")) {
            return String.valueOf(pbpPlugin.getBountyDataManager().hasBounty(player.getUniqueId()));
        }

        return "INVALID-OPTION";
    }

}
