package com.tcoded.playerbountiesplus.hook;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.UUID;

public class PAPIExpansion extends PlaceholderExpansion {

    private static final String IDENTIFIER = "playerbountiesplus";

    private final PlayerBountiesPlus plugin;

    public PAPIExpansion(PlayerBountiesPlus plugin) {
        this.plugin = plugin;
    }

    public @NotNull String getIdentifier() {
        return IDENTIFIER;
    }

    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equals("bounty")) {
            UUID uuid = player.getUniqueId();
            return String.valueOf(plugin.getBountyDataManager().getBounties().getOrDefault(uuid, 0));
        }

        return null;
    }
}
