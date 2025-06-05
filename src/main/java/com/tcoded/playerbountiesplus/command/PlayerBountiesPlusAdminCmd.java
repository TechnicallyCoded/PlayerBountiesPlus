package com.tcoded.playerbountiesplus.command;

import com.google.common.collect.Lists;
import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.command.admin.PlayerBountiesPlusReloadCmd;
import com.tcoded.playerbountiesplus.command.admin.PlayerBountiesPlusVersionCmd;
import com.tcoded.playerbountiesplus.command.admin.bounty.AdminBountyCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerBountiesPlusAdminCmd implements CommandExecutor, TabCompleter {
    private static final ArrayList<String> completions = Lists.newArrayList("reload", "version", "bounty", "help");

    private final PlayerBountiesPlus plugin;

    public PlayerBountiesPlusAdminCmd(PlayerBountiesPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            sendHelpMsg(sender);
            return true;
        }

        String arg0Lower = args[0].toLowerCase();

        switch (arg0Lower) {
            case "reload":
                return PlayerBountiesPlusReloadCmd.handleCmd(plugin, sender, command, label, args);
            case "version":
                return PlayerBountiesPlusVersionCmd.handleCmd(plugin, sender, command, label, args);
            case "bounty":
                return AdminBountyCmd.handleCmd(plugin, sender, command, label, args);
            default:
                sendHelpMsg(sender);
                return true;
        }
    }

    private void sendHelpMsg(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&f[&bPlayerBountiesPlus&f] &7PlayerBountiesPlus by TechnicallyCoded\n" +
                        "&fAdmin Commands:\n" +
                        "&f/pbp reload &7- Reload the configuration file and the messages.\n" +
                        "&f/pbp version &7- Check the version of the plugin.\n" +
                        "&f/pbp bounty set <player> <amount> &7- Set a player's bounty.\n" +
                        "&f/pbp bounty add <player> <amount> &7- Add to a player's bounty.\n" +
                        "&f/pbp bounty remove <player> <amount> &7- Remove from a player's bounty.\n" +
                        "&f/pbp bounty delete <player> &7- Delete a player's bounty.\n" +
                        "&f/pbp help &7- Get this message."
        ));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return completions.stream()
                .filter(s -> s.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }
}
