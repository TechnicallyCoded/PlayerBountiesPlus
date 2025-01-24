package com.tcoded.playerbountiesplus.command.bounty;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BountyCheckCmd {

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String cmdName, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.check.no-player-specified"));
            return true;
        }

        String playerNameArg = args[1];

        Player target = plugin.getServer().getPlayerExact(playerNameArg);

        if (target == null) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.check.player-not-found"));
            return true;
        }

        UUID playerUUID = target.getUniqueId();

        BountyDataManager bountyDataManager = plugin.getBountyDataManager();
        boolean hasBounty = bountyDataManager.hasBounty(playerUUID);

        if (hasBounty) {
            // Confirmation
            int bounty = bountyDataManager.getBounty(playerUUID);
            sender.sendMessage(
                    plugin.getLang().getColored("command.bounty.check.bounty-found")
                            .replace("{target}", target.getName())
                            .replace("{bounty}", Integer.toString(bounty))
            );
        } else {
            sender.sendMessage(
                    plugin.getLang().getColored("command.bounty.check.no-bounty")
                            .replace("{target}", target.getName())
            );
        }

        return true;
    }
}
