package com.tcoded.playerbountiesplus.hook.team;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TownyHook extends AbstractTeamHook {

    public TownyHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        super(plugin, teamPlugin);
    }

    public Town getTown(Player player) {
        return TownyAPI.getInstance().getTown(player);
    }

    public Nation getNation(Player player) {
        return TownyAPI.getInstance().getNation(player);
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        Town town1 = getTown(player1);
        Town town2 = getTown(player2);

        Nation nation1 = getNation(player1);
        Nation nation2 = getNation(player2);

        boolean sameTown = town1 != null && town2 != null && (town1.hasResident(player2) || town1.hasAlly(town2));
        boolean sameNation = nation1 != null && nation2 != null && (nation1.equals(nation2) || nation1.hasAlly(nation2));

        return sameTown || sameNation;
    }
}
