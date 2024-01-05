package com.tcoded.playerbountiesplus.hook.team;

import com.massivecraft.factions.FPlayers;
import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SaberFactionsHook extends AbstractTeamHook {

    public SaberFactionsHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        super(plugin, teamPlugin);
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        FPlayers fPlayers = FPlayers.getInstance();
        String factionId1 = fPlayers.getByPlayer(player1).getFactionId();
        String factionId2 = fPlayers.getByPlayer(player2).getFactionId();
        return factionId1.equals(factionId2);
    }

}
