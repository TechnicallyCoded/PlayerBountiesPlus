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
        Town town = getTown(player1);
        Nation player1Nation = getNation(player1);
        Nation player2Nation = getNation(player2);
        boolean sameTown = town != null && town.hasResident(player2);
        boolean sameNation = player1Nation != null && player1Nation.equals(player2Nation);

        return sameTown || sameNation;
    }
}
