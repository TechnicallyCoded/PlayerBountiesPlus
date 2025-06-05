package com.tcoded.playerbountiesplus.hook.team;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import me.loving11ish.clans.api.ClansLiteAPI;
import me.loving11ish.clans.api.models.Clan;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ClansLiteHook extends AbstractTeamHook {

    private final ClansLiteAPI api;

    public ClansLiteHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        super(plugin, teamPlugin);
        api = ClansLiteAPI.getInstance();
    }

    public @Nullable String getClanId(Player player) {
        Clan clan = api.getClanByBukkitPlayer(player);
        return clan == null ? null : clan.getClanFinalName();
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        String clanId1 = this.getClanId(player1);
        String clanId2 = this.getClanId(player2);

        if (clanId1 == null) return false;
        if (clanId2 == null) return false;

        return clanId1.equals(this.getClanId(player2));
    }
}
