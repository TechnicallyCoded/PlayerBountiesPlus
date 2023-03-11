package com.tcoded.playerbountiesplus.hook.clan;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import xyz.gamlin.clans.Clans;
import xyz.gamlin.clans.utils.ClansStorageUtil;

public class ClansLiteHook extends AbstractClanHook {

    private PlayerBountiesPlus plugin;
    private Plugin clansLitePlugin;

    private ClansStorageUtil clansData;

    public ClansLiteHook(PlayerBountiesPlus plugin, Plugin clansLitePlugin) {
        this.plugin = plugin;
        this.clansLitePlugin = clansLitePlugin;
    }

    @Override
    public @Nullable String getClanId(Player player) {
        return ClansStorageUtil.findClanByPlayer(player).getClanFinalName();
    }
}
