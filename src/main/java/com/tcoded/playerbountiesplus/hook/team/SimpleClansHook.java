package com.tcoded.playerbountiesplus.hook.team;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class SimpleClansHook extends AbstractTeamHook {

    public SimpleClansHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        super(plugin, teamPlugin);
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        ClanManager clanManager = SimpleClans.getInstance().getClanManager();

        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();

        Clan clan1 = clanManager.getClanByPlayerUniqueId(uuid1);
        Clan clan2 = clanManager.getClanByPlayerUniqueId(uuid2);

        if (clan1 == null) return false;
        if (clan2 == null) return false;

        return clan1 == clan2;
    }

}
