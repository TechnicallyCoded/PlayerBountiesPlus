package com.tcoded.playerbountiesplus.command.bounty;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.event.BountySetEvent;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BountySetCmd {

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String cmdName, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.missing-args"));
            return true;
        }

        if (sender instanceof Player && !sender.hasPermission("playerbountiesplus.command.bounty.set")) {
            String noPerm = plugin.getLang().getColored("command.no-permission");
            String noPermDetailed = plugin.getLang().getColored("command.no-permission-detailed")
                    .replace("{no-permission-msg}", noPerm)
                    .replace("{permission}", "playerbountiesplus.command.bounty.set");
            sender.sendMessage(noPermDetailed);
            return true;
        }

        String playerNameArg = args[1];
        float amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.amount-nan"));
            return true;
        }

        Player target = plugin.getServer().getPlayerExact(playerNameArg);

        if (target == null) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.player-not-found"));
            return true;
        }

        // Check limits
        double minimum = plugin.getConfig().getDouble("bounty-minimum", 1.0);
        if (amount < minimum) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.under-minimum")
                    .replace("{minimum}", String.valueOf(minimum))
            );
            return true;
        }

        double maximum = plugin.getConfig().getDouble("bounty-maximum", 1000000.0);
        if (amount > maximum) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.over-maximum")
                    .replace("{maximum}", String.valueOf(minimum))
            );
            return true;
        }

        // Check money
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean allowed = plugin.getEcoHook().takeEco(player, amount);

            if (!allowed) {
                sender.sendMessage(plugin.getLang().getColored("command.bounty.set.not-enough-money"));
                return true;
            }
        }

        // Apply bounty multiplier
        amount *= (float) plugin.getConfig().getDouble("bounty-multiplier", 1.0);

        // Trigger bounty set event
        BountySetEvent event = new BountySetEvent(sender instanceof Player ? (Player) sender : null, target, amount);
        plugin.getServer().getPluginManager().callEvent(event);
        amount = event.getAmount();

        // Check if event was cancelled
        if (event.isCancelled()) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.cancelled"));
            return true;
        }

        // Sanity check final amount
        if (amount <= 0) {
            sender.sendMessage(plugin.getLang().getColored("command.bounty.set.internal-invalid-value"));
            return true;
        }

        UUID playerUUID = target.getUniqueId();

        // Calculate total bounty including previous bounties
        BountyDataManager bountyDataManager = plugin.getBountyDataManager();
        int bountyAlreadyPresent = bountyDataManager.getBounty(playerUUID);
        int totalBounty = ((int) amount) + bountyAlreadyPresent;
        bountyDataManager.setBounty(playerUUID, totalBounty);

        // Confirmation
        sender.sendMessage(
                plugin.getLang().getColored("command.bounty.set.success")
                        .replace("{bounty}", String.valueOf(amount))
                        .replace("{total}", String.valueOf(totalBounty))
                        .replace("{target}", target.getName())
                        .replace("{player}", sender.getName())
        );

        // Announce
        String extra;
        if (bountyAlreadyPresent == 0) extra = "";
        else {
            extra = plugin.getLang().getColored("command.bounty.set.announce-extra")
                    .replace("{total}", String.valueOf(totalBounty));
        }

        if (plugin.getConfig().getBoolean("bounty-placed-announce", true)) {
            plugin.getServer().broadcastMessage(
                    plugin.getLang().getColored("command.bounty.set.announce")
                            .replace("{bounty}", String.valueOf(amount))
                            .replace("{target}", target.getName())
                            .replace("{player}", sender.getName())
                            .replace("{extra}", extra)
            );
        }

        bountyDataManager.saveBountiesAsync();

        return true;
    }

    @Nullable
    public static List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 2) {
            // Suggest online player names for the username
            return sender.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 3) {
            // Suggest a placeholder for the amount
            return Collections.singletonList("<amount>");
        }
        return Collections.emptyList();
    }
}
