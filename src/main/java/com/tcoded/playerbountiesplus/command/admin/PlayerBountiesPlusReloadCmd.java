package com.tcoded.playerbountiesplus.command.admin;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PlayerBountiesPlusReloadCmd {

    private static final String RELOAD_PERMISSION = "playerbountiesplus.command.playerbountiesplus.reload";

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String cmdName, String[] args) {

        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            String noPerm = plugin.getLang().getColored("command.no-permission");
            String noPermDetailed = plugin.getLang().getColored("command.no-permission-detailed")
                            .replace("{no-permission-msg}", noPerm)
                            .replace("{permission}", RELOAD_PERMISSION);
            sender.sendMessage(noPermDetailed);
            return true;
        }

        plugin.reloadConfig();
        plugin.reloadLang();
        plugin.checkUpdate();

        sender.sendMessage(plugin.getLang().getColored("command.admin.reload.reloaded"));

        return true;
    }

}
