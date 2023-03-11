package com.tcoded.playerbountiesplus.command;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BountyCheckCmd {
    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String cmdName, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "You need to specify a player! /bounty check <player>");
            return true;
        }

        String playerNameArg = args[1];

        Player target = plugin.getServer().getPlayerExact(playerNameArg);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online or doesn't exist!");
            return true;
        }

        UUID playerUUID = target.getUniqueId();

        Integer amount = plugin.getBounties().get(playerUUID);

        if (amount != null && amount > 0) {
            // Confirmation
            sender.sendMessage(ChatColor.GREEN + String.format("There is a bounty worth %s on %s's head!", amount, target.getName()));
        } else {
            sender.sendMessage(ChatColor.RED + String.format("There is no bounty on %s's head!", target.getName()));
        }

        return true;
    }
}
