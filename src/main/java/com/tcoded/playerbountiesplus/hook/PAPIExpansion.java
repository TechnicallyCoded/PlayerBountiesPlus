package com.tcoded.playerbountiesplus.hook;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
            return String.valueOf(plugin.getBountyDataManager().getBounties().getOrDefault(uuid, -1));
        }
        if (params.startsWith("top")) {
            //top_<n>_value \ top_<n>_name
            String[] split = params.split("_");
            if (split.length == 3) {
                int n = Integer.parseInt(split[1]);
                if(n > 0 && n < 11) {

                    Set<Map.Entry<UUID, Integer>> bountiesSet = plugin.getBountyDataManager().getBounties().entrySet();
                    List<Map.Entry<UUID, Integer>> bounties = new ArrayList<>(bountiesSet);
                    bounties.sort((a, b) -> b.getValue() - a.getValue());

                    int bountiesSize = bounties.size();

                    if (bountiesSize < n) {
                        if (Objects.equals(split[2], "value")) {
                            return "0";
                        } else {
                            return plugin.getLang().getColored("command.bounty.top.no-bounties");
                        }
                    }

                    if (Objects.equals(split[2], "value")) {
                        return String.valueOf(bounties.get(n - 1).getValue());
                    } else {
                        return plugin.getServer().getOfflinePlayer(bounties.get(n - 1).getKey()).getName();
                    }

                } else {
                    return "out of bounds";
                }
            }
        }

        return null;
    }
}
