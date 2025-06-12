package com.tcoded.playerbountiesplus.command.admin.bounty;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AdminBountyCmd {

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.no-action"));
            return true;
        }

        String action = args[1].toLowerCase();
        switch (action) {
            case "set":
                return AdminBountySetCmd.handleCmd(plugin, sender, cmd, label, args);
            case "add":
                return AdminBountyAddCmd.handleCmd(plugin, sender, cmd, label, args);
            case "remove":
                return AdminBountyRemoveCmd.handleCmd(plugin, sender, cmd, label, args);
            case "delete":
                return AdminBountyDeleteCmd.handleCmd(plugin, sender, cmd, label, args);
            case "get":
                return AdminBountyGetCmd.handleCmd(plugin, sender, cmd, label, args);
            default:
                sender.sendMessage(plugin.getLang().getColored("command.bounty.invalid-action"));
                return true;
        }
    }
}
