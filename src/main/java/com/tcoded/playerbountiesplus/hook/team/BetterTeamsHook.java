package com.tcoded.playerbountiesplus.hook.team;

import com.booksaw.betterTeams.Team;
import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BetterTeamsHook extends AbstractTeamHook {

    public BetterTeamsHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        super(plugin, teamPlugin);
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        return Team.getTeam(player1) == Team.getTeam(player2);
    }

}
