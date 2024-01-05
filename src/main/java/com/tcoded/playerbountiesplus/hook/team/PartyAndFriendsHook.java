package com.tcoded.playerbountiesplus.hook.team;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PartyAndFriendsHook extends AbstractTeamHook {

    public PartyAndFriendsHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        super(plugin, teamPlugin);
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        return false; // todo: figure out which of the 5 different APIs I'm supposed to use...
    }

}
