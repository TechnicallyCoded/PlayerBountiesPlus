package com.tcoded.playerbountiesplus.hook.team;

import com.cortezromeo.clansplus.api.ClanPlus;
import com.cortezromeo.clansplus.api.storage.IPlayerData;
import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ClansPlusHook extends AbstractTeamHook {

    private final ClanPlus api;

    public ClansPlusHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        super(plugin, teamPlugin);
        this.api = Bukkit.getServicesManager().getRegistration(ClanPlus.class).getProvider();
    }

    private @Nullable String getClanId(Player player) {
        IPlayerData data = api.getPluginDataManager().getPlayerDatabase(player.getName());
        if (data == null) {
            api.getPluginDataManager().loadPlayerDatabase(player.getName());
            data = api.getPluginDataManager().getPlayerDatabase(player.getName());
            if (data == null) return null;
        }
        String clan = data.getClan();
        return clan == null || clan.isEmpty() ? null : clan;
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        String clan1 = getClanId(player1);
        if (clan1 == null) return false;
        String clan2 = getClanId(player2);
        if (clan2 == null) return false;
        return clan1.equalsIgnoreCase(clan2);
    }
}
