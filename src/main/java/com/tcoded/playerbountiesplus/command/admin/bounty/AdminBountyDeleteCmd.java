package com.tcoded.playerbountiesplus.command.admin.bounty;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class AdminBountyDeleteCmd {

    private static final String PERMISSION = "playerbountiesplus.command.playerbountiesplus.admin.delete";

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
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.delete.missing-args"));
            return true;
        }

        String playerName = args[2];
        OfflinePlayer target = plugin.getServer().getOfflinePlayer(playerName);
        if (target == null || (!target.hasPlayedBefore() && !target.isOnline())) {
            sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.delete.player-not-found"));
            return true;
        }

        UUID uuid = target.getUniqueId();
        BountyDataManager m = plugin.getBountyDataManager();
        m.removeBounty(uuid);
        m.saveBountiesAsync();

        sender.sendMessage(plugin.getLang().getColored("command.admin.bounty.delete.success")
                .replace("{target}", target.getName()));
        return true;
    }
}
