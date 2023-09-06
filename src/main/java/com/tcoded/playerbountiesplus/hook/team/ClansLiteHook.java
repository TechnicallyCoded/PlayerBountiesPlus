package com.tcoded.playerbountiesplus.hook.team;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import xyz.gamlin.clans.models.Clan;
import xyz.gamlin.clans.utils.ClansStorageUtil;

public class ClansLiteHook extends AbstractTeamHook {

    public ClansLiteHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        super(plugin, teamPlugin);
    }

    public @Nullable String getClanId(Player player) {
        Clan clan = ClansStorageUtil.findClanByPlayer(player);
        return clan == null ? null : clan.getClanFinalName();
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        String clanIdVictim = this.getClanId(player1);
        return clanIdVictim != null && clanIdVictim.equals(this.getClanId(player2));
    }
}
