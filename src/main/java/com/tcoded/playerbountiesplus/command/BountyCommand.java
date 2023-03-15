package com.tcoded.playerbountiesplus.command;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BountyCommand implements CommandExecutor, TabCompleter {

    private PlayerBountiesPlus plugin;

    public BountyCommand(PlayerBountiesPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "You need to specify an action! /bounty <set/top/check>");
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "set":
                return BountySetCmd.handleCmd(plugin, sender, cmd, cmdName, args);
            case "top":
                return BountyTopCmd.handleCmd(plugin, sender, cmd, cmdName, args);
            case "check":
                return BountyCheckCmd.handleCmd(plugin, sender, cmd, cmdName, args);
            default:
                sender.sendMessage(ChatColor.RED + "That's not a valid action!");
        }

        return true;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        @Nullable List<String> options = new ArrayList<>();

        if (args.length < 2) {
            String arg0 = args[0].toLowerCase();
            String[] actions = {"set", "top", "check"};
            for (String action : actions) {
                if (action.startsWith(arg0)) {
                    options.add(action);
                }
            }
        }
        else if (args.length == 2) {
            String arg0 = args[0].toLowerCase();
            String arg1 = args[1].toLowerCase();
            if (arg0.equals("set") || arg0.equals("check")) {
                this.plugin.getServer().getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(arg1))
                        .forEach(options::add);
            }
        }
        else if (args.length == 3) {
            String arg0 = args[0].toLowerCase();
            String arg2 = args[2].toLowerCase();
            if (arg0.equals("set")) {
                if (arg2.equals("")) options.add("<amount>");
            }
        }

        return options;
    }
}
