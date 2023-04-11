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
            sender.sendMessage(ChatColor.RED + "You need to specify a player and an amount! /bounty set <player> <amount>");
            return true;
        }

        String playerNameArg = args[1];
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("That's not a valid number!");
            return true;
        }

        Player target = plugin.getServer().getPlayerExact(playerNameArg);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online or doesn't exist!");
            return true;
        }

        // Check money
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean allowed = plugin.getVaultHook().takeMoney(player, amount);

            if (!allowed) {
                sender.sendMessage(ChatColor.RED + "You don't have enough money for that!");
                return true;
            }
        }

        UUID playerUUID = target.getUniqueId();

        ConcurrentHashMap<UUID, Integer> bounties = plugin.getBountyDataManager().getBounties();
        Integer bountyAlreadyPresent = bounties.getOrDefault(playerUUID, 0);
        int totalBounty = amount + bountyAlreadyPresent;
        bounties.put(playerUUID, totalBounty);

        // Confirmation
        sender.sendMessage(ChatColor.GREEN + String.format("You placed a bounty of %s on %s's head!", amount, target.getName()));

        // Announce
        String extra = bountyAlreadyPresent == 0 ? "" : " (Total: " + totalBounty + ")";
        plugin.getServer().broadcastMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD +
                String.format("\nA bounty of %s was placed on %s's head by %s!%s\n", amount, target.getName(), sender.getName(), extra));

        plugin.getBountyDataManager().saveBountiesAsync();

        return true;
    }
}
