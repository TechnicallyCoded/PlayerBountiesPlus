package com.tcoded.playerbountiesplus.command;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.command.bounty.BountyCheckCmd;
import com.tcoded.playerbountiesplus.command.bounty.BountySetCmd;
import com.tcoded.playerbountiesplus.command.bounty.BountyTopCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
