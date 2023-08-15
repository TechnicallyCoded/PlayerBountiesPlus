package com.tcoded.playerbountiesplus.command;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BountySetCmd {

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String cmdName, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.missing-args"));
            return true;
        }

        String playerNameArg = args[1];
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.amount-nan"));
            return true;
        }

        Player target = plugin.getServer().getPlayerExact(playerNameArg);

        if (target == null) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.player-not-found"));
            return true;
        }

        // Check money
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean allowed = plugin.getVaultHook().takeMoney(player, amount);

            if (!allowed) {
                sender.sendMessage(plugin.getLang().getColored("command.bounty.set.not-enough-money"));
                return true;
            }
        }

        UUID playerUUID = target.getUniqueId();

        ConcurrentHashMap<UUID, Integer> bounties = plugin.getBountyDataManager().getBounties();
        Integer bountyAlreadyPresent = bounties.getOrDefault(playerUUID, 0);
        int totalBounty = amount + bountyAlreadyPresent;
        bounties.put(playerUUID, totalBounty);

        // Confirmation
//        sender.sendMessage(ChatColor.GREEN + String.format("You placed a bounty of %s on %s's head!", amount, target.getName()));
        sender.sendMessage(
                plugin.getLang().getColored("command.bounty.set.success")
                        .replace("{bounty}", String.valueOf(amount))
                        .replace("{target}", target.getName())
        );

        // Announce
        String extra;
        if (bountyAlreadyPresent == 0) extra = "";
        else {
            extra = plugin.getLang().getColored("command.bounty.set.announce-extra")
                    .replace("{total}", String.valueOf(totalBounty));
        }

        plugin.getServer().broadcastMessage(
                plugin.getLang().getColored("command.bounty.set.announce")
                        .replace("{bounty}", String.valueOf(amount))
                        .replace("{target}", target.getName())
                        .replace("{player}", sender.getName())
                        .replace("{extra}", extra)
        );

        plugin.getBountyDataManager().saveBountiesAsync();

        return true;
    }
}
