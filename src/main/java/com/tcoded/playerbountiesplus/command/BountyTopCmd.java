package com.tcoded.playerbountiesplus.command;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class BountyTopCmd {

    public static boolean handleCmd(PlayerBountiesPlus plugin, CommandSender sender, Command cmd, String cmdName, String[] args) {

        Set<Map.Entry<UUID, Integer>> bountiesSet = plugin.getBounties().entrySet();
        List<Map.Entry<UUID, Integer>> bounties = new ArrayList<>(bountiesSet);
        
        bounties.sort((a, b) -> b.getValue() - a.getValue());

        StringBuilder strb = new StringBuilder();
        strb.append(ChatColor.YELLOW);
        strb.append("Top 10 bounties:\n");

        int bountiesSize = bounties.size();
        int maxInList = Math.min(10, bountiesSize);

        if (bountiesSize > 0) {
            for (int i = 0; i < maxInList; i++) {
                strb.append(ChatColor.GRAY);
                strb.append(" - ");
                Map.Entry<UUID, Integer> entry = bounties.get(i);
                strb.append(plugin.getServer().getOfflinePlayer(entry.getKey()).getName());
                strb.append(": ");
                strb.append(entry.getValue());
                strb.append('\n');
            }
        }
        else {
            strb.append(ChatColor.RED);
            strb.append(" No bounties were set!\n");
        }

        String message = strb.toString();
        message = message.substring(0, message.length() - 1); // remove \n
        sender.sendMessage(message);
        
        return true;
    }

}
