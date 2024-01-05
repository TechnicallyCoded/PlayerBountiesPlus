package com.tcoded.playerbountiesplus.hook.team;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SimpleClansHook extends AbstractTeamHook {

    public SimpleClansHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        super(plugin, teamPlugin);
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        ClanManager clanManager = SimpleClans.getInstance().getClanManager();
        return clanManager.getClanByPlayerUniqueId(player1.getUniqueId()) == clanManager.getClanByPlayerUniqueId(player2.getUniqueId());
    }

}
