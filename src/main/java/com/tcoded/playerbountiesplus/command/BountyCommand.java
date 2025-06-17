package com.tcoded.playerbountiesplus.command;

import com.google.common.collect.Lists;
import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.command.bounty.BountyCheckCmd;
import com.tcoded.playerbountiesplus.command.bounty.BountySetCmd;
import com.tcoded.playerbountiesplus.command.bounty.BountyTopCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BountyCommand implements CommandExecutor, TabCompleter {

    private PlayerBountiesPlus plugin;

    public BountyCommand(PlayerBountiesPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.no-action"));
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
                sender.sendMessage(plugin.getLang().getColored("command.bounty.invalid-action"));
        }

        return true;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList("set", "top", "check").stream()
                    .filter(action -> action.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length > 1) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "set":
                    return BountySetCmd.onTabComplete(sender, args).stream()
                            .filter(suggestion -> suggestion.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                case "check":
                    return BountyCheckCmd.onTabComplete(sender, args).stream()
                            .filter(suggestion -> suggestion.toLowerCase().startsWith(args[1].toLowerCase()))
                            .collect(Collectors.toList());
                case "top":
                    return Collections.emptyList(); // No additional arguments for top
                default:
                    return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }
}
