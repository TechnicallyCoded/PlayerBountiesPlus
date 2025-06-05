package com.tcoded.playerbountiesplus.hook.team;

import com.alessiodp.parties.api.Parties;
import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class AlessioPartiesHook extends AbstractTeamHook {

    public AlessioPartiesHook(PlayerBountiesPlus plugin, Plugin partiesPlugin) {
        super(plugin, partiesPlugin);
    }

    @Override
    public boolean isFriendly(Player player1, Player player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();

        if (!Parties.getApi().isPlayerInParty(uuid1)) return false;
        if (!Parties.getApi().isPlayerInParty(uuid2)) return false;

        return Parties.getApi().areInTheSameParty(uuid1, uuid2);
    }
}
