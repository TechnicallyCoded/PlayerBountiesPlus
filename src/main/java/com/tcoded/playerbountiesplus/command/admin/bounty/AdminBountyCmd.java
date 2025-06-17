package com.tcoded.playerbountiesplus.command.admin.bounty;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = Collections.emptyList();

        if (args.length == 2) {
            suggestions = Arrays.asList("set", "add", "remove", "delete", "get");
        } else if (args.length > 2) {
            String sub = args[1].toLowerCase();
            switch (sub) {
                case "add":
                    suggestions = AdminBountyAddCmd.onTabComplete(sender, null, null, args);
                    break;
                case "remove":
                    suggestions = AdminBountyRemoveCmd.onTabComplete(sender, null, null, args);
                    break;
                case "delete":
                    suggestions = AdminBountyDeleteCmd.onTabComplete(sender, null, null, args);
                    break;
                case "get":
                    suggestions = AdminBountyGetCmd.onTabComplete(sender, null, null, args);
                    break;
                case "set":
                    suggestions = AdminBountySetCmd.onTabComplete(sender, null, null, args);
                    break;
            }
        }

        String input = args[args.length - 1].toLowerCase();
        return suggestions.stream()
                .filter(opt -> opt.toLowerCase().startsWith(input))
                .collect(Collectors.toList());
    }
}
