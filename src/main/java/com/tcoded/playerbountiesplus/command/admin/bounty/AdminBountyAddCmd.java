package com.tcoded.playerbountiesplus.command.admin.bounty;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdminBountyAddCmd implements TabCompleter {

    private static final String PERMISSION = "playerbountiesplus.command.admin.bounty.add";

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            String noPerm = plugin.getLang().getColored("command.no-permission");
            String noPermDetailed = plugin.getLang().getColored("command.no-permission-detailed")
                    .replace("{no-permission-msg}", noPerm)
                    .replace("{permission}", PERMISSION);
            sender.sendMessage(noPermDetailed);
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.add.missing-args"));
            return true;
        }

        String playerName = args[2];
        int amount;
        try {
            amount = Integer.parseInt(args[3]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.add.amount-nan"));
            return true;
        }

        OfflinePlayer target = plugin.getServer().getOfflinePlayer(playerName);
        if (target == null || (!target.hasPlayedBefore() && !target.isOnline())) {
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.add.player-not-found"));
            return true;
        }

        UUID uuid = target.getUniqueId();
        BountyDataManager m = plugin.getBountyDataManager();
        int current = m.getBounty(uuid);
        int total = current + amount;
        m.setBounty(uuid, total);
        m.saveBountiesAsync();

        sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.add.success")
                .replace("{target}", target.getName())
                .replace("{amount}", String.valueOf(amount))
                .replace("{total}", String.valueOf(total)));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            // Suggest online player names for the username
            return sender.getServer().getOnlinePlayers().stream()
                    .map(player -> player.getName())
                    .filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 4) {
            // Suggest a placeholder for the amount
            return Collections.singletonList("<amount>");
        }
        return Collections.emptyList();
    }
}
