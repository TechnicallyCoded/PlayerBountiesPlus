package com.tcoded.playerbountiesplus.command.admin;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.util.LangUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PlayerBountiesPlusVersionCmd {

    private static final String VERSION_PERMISSION = "playerbountiesplus.command.playerbountiesplus.version";

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String cmdName, String[] args) {
        LangUtil lang = plugin.getLang();

        String version;
        if (sender.hasPermission(VERSION_PERMISSION)) {
            version = "v" + plugin.getDescription().getVersion();
        } else {
            version = "\n" + lang.getColored("command.admin.version.no-permission");
        }


        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&f[&bPlayerBountiesPlus&f] Plugin Information:\n" +
                        "&fPlugin: &7Running PlayerBountiesPlus " + version + "\n" +
                        "&fAuthor: &7TechnicallyCoded\n" +
                        "&fUpdate URL: &9https://www.spigotmc.org/resources/player-bounties-plus.108637/"
        ));

        return true;
    }

}
