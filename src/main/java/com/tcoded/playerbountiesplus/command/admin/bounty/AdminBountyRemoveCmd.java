package com.tcoded.playerbountiesplus.command.admin.bounty;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class AdminBountyRemoveCmd {

    private static final String PERMISSION = "playerbountiesplus.command.playerbountiesplus.bounty.remove";

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
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.remove.missing-args"));
            return true;
        }

        String playerName = args[2];
        int amount;
        try {
            amount = Integer.parseInt(args[3]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.remove.amount-nan"));
            return true;
        }

        OfflinePlayer target = plugin.getServer().getOfflinePlayer(playerName);
        if (target == null || (!target.hasPlayedBefore() && !target.isOnline())) {
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.remove.player-not-found"));
            return true;
        }

        UUID uuid = target.getUniqueId();
        BountyDataManager m = plugin.getBountyDataManager();
        int current = m.getBounty(uuid);
        int total = current - amount;
        if (total <= 0) {
            m.removeBounty(uuid);
            total = 0;
        } else {
            m.setBounty(uuid, total);
        }
        m.saveBountiesAsync();

        sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.remove.success")
                .replace("{target}", target.getName())
                .replace("{amount}", String.valueOf(amount))
                .replace("{total}", String.valueOf(total)));
        return true;
    }
}
