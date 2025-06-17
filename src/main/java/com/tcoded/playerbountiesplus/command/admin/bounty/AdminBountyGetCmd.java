package com.tcoded.playerbountiesplus.command.admin.bounty;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdminBountyGetCmd {

    private static final String PERMISSION = "playerbountiesplus.command.admin.bounty.get";

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            String noPerm = plugin.getLang().getColored("command.no-permission");
            String noPermDetailed = plugin.getLang().getColored("command.no-permission-detailed")
                    .replace("{no-permission-msg}", noPerm)
                    .replace("{permission}", PERMISSION);
            sender.sendMessage(noPermDetailed);
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.get.missing-args"));
            return true;
        }

        String playerName = args[2];

        OfflinePlayer target = plugin.getServer().getOfflinePlayer(playerName);
        if (target == null || (!target.hasPlayedBefore() && !target.isOnline())) {
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.get.player-not-found"));
            return true;
        }

        UUID uuid = target.getUniqueId();
        BountyDataManager m = plugin.getBountyDataManager();
        int current = m.getBounty(uuid);

        sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.get.success")
                .replace("{target}", target.getName())
                .replace("{bounty}", String.valueOf(current)));
        return true;
    }

    @Nullable
    public static List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            // Suggest all online player names for the username
            return sender.getServer().getOnlinePlayers().stream()
                    .map(player -> player.getName())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
