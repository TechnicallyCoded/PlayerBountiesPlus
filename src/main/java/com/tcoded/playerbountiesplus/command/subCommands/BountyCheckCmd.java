package com.tcoded.playerbountiesplus.command.subCommands;

import com.tcoded.playerbountiesplus.models.Bounty;
import com.tcoded.playerbountiesplus.utils.BountiesStorageUtils;
import com.tcoded.playerbountiesplus.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCheckCmd {

    BountiesStorageUtils bountiesStorageUtils = new BountiesStorageUtils();

    public boolean handleCmd(CommandSender sender, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length < 2) {
                player.sendMessage(ColorUtils.translateColorCodes("&cYou need to specify a player and an amount! /bounty check [player]"));
                return true;
            }

            String targetName = args[1];
            Player target = Bukkit.getServer().getPlayerExact(targetName);

            if (target != null){
                if (bountiesStorageUtils.hasExistingBounty(target)){
                    Bounty bounty = bountiesStorageUtils.findBountyByOnlineTarget(target);
                    int amount = bounty.getBountyValue();
                    player.sendMessage(ColorUtils.translateColorCodes("&aThere is a bounty worth &e&l" + amount + "&aon &e&l" + targetName +"'s &ahead!"));
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes("&cThere is no bounty on &b&l" + targetName +"'s &chead!"));
                }
                return true;
            }
        }
        return true;
    }
}
