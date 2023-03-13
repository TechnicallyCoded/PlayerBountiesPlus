package com.tcoded.playerbountiesplus.command.subCommands;

import com.tcoded.playerbountiesplus.utils.BountiesStorageUtils;
import com.tcoded.playerbountiesplus.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class BountyTopCmd {

    BountiesStorageUtils bountiesStorageUtils = new BountiesStorageUtils();

    public boolean handleCmd(CommandSender sender) {

        if (sender instanceof Player){
            Player player = (Player) sender;

            Set<Map.Entry<UUID, Integer>> bountiesSet = bountiesStorageUtils.getStrippedBounties();
            List<Map.Entry<UUID, Integer>> bounties = new ArrayList<>(bountiesSet);

            bounties.sort(Comparator.comparingInt(Map.Entry::getValue));

            StringBuilder messageString = new StringBuilder();
            messageString.append(ColorUtils.translateColorCodes("&eTop 10 Bounties:\n"));
            messageString.append(ColorUtils.translateColorCodes("&f\n"));
            if (bounties.size() > 0){
                for (int i = 0; i < 10; i++){
                    messageString.append(ColorUtils.translateColorCodes("&7 - "));
                    String playerName = Bukkit.getOfflinePlayer(bounties.get(i).getKey()).getName();
                    messageString.append(ColorUtils.translateColorCodes("&3" + playerName));
                    messageString.append(ColorUtils.translateColorCodes("&7: "));
                    messageString.append(ColorUtils.translateColorCodes("&6$" + bounties.get(i).getValue() + "&f\n"));
                }
            }else {
                messageString.append(ColorUtils.translateColorCodes("&cNo bounties have been set!\n"));
                messageString.append(ColorUtils.translateColorCodes("&3Use /bounty to see available commands."));
            }
            player.sendMessage(messageString.toString());
            return true;
        }
        return true;
    }
}
