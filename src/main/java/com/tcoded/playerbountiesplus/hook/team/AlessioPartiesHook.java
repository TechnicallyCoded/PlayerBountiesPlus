package com.tcoded.playerbountiesplus.hook.team;

import com.alessiodp.parties.api.Parties;
import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AlessioPartiesHook extends AbstractTeamHook {

    public AlessioPartiesHook(PlayerBountiesPlus plugin, Plugin partiesPlugin) {
        super(plugin, partiesPlugin);
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        return Parties.getApi().areInTheSameParty(player1.getUniqueId(), player2.getUniqueId());
    }
}
