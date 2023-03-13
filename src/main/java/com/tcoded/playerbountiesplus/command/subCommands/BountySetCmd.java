package com.tcoded.playerbountiesplus.command.subCommands;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.models.Bounty;
import com.tcoded.playerbountiesplus.utils.BountiesStorageUtils;
import com.tcoded.playerbountiesplus.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BountySetCmd {

    FileConfiguration configFile = PlayerBountiesPlus.getPlugin().getConfig();
    BountiesStorageUtils bountiesStorageUtils = new BountiesStorageUtils();

    public boolean handleCmd(CommandSender sender, String[] args, int amount) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length < 3) {
                player.sendMessage(ColorUtils.translateColorCodes("&cYou need to specify a player and an amount! /bounty set [player] [amount]"));
                return true;
            }

            String targetName = args[1];
            Player onlineTarget = Bukkit.getPlayerExact(targetName);

            if (onlineTarget != null){
                if (amount < configFile.getInt("bounty.min-bounty-value")){
                    player.sendMessage(ColorUtils.translateColorCodes("&cPlease provide a larger bounty value."));
                    return true;
                }
                boolean allowed = PlayerBountiesPlus.getPlugin().getVaultHook().takeMoney(player, amount);
                if (allowed){
                    Bounty bounty = bountiesStorageUtils.findBountyByOnlineTarget(onlineTarget);
                    if (bounty != null){
                        if (bountiesStorageUtils.findBountyByOnlineTarget(onlineTarget).getBountyValue() >= configFile.getInt("bounty.max-bounty-value")){
                            player.sendMessage(ColorUtils.translateColorCodes("&cThat player already has the maximum allowed bounty set!"));
                            return true;
                        }
                    }
                    if (!bountiesStorageUtils.hasExistingBounty(onlineTarget)){
                        if (bountiesStorageUtils.createOnlineBounty(onlineTarget, player, amount) != null){
                            player.sendMessage(ColorUtils.translateColorCodes("&aYou placed a bounty of &e&l" + amount + " &aon &e&l" + targetName + "'s &ahead!"));
                            Bukkit.getServer().broadcastMessage(ColorUtils.translateColorCodes("\n&4&lA bounty of &b&l" + amount + "&4&lwas placed on &b&l" + targetName + "'s &4&lhead by &e&l" + player.getName()+ "&4&l!\n"));
                            return true;
                        }
                    }else {
                        bountiesStorageUtils.updateOnlineBountyValue(onlineTarget, amount);
                        player.sendMessage(ColorUtils.translateColorCodes("&aYou updated &e&l" + targetName + "'s &abounty to &e&l" + amount));
                        Bukkit.getServer().broadcastMessage(ColorUtils.translateColorCodes(
                                "\n&4&lThe bounty on &b&l" + targetName + "'s &4&lhead was updated by &e&l" + player.getName()+ "&4&l!\n"
                                + "&4&lThe new bounty is &b&l" + amount + "\n"));
                        return true;
                    }
                }else {
                    player.sendMessage(ColorUtils.translateColorCodes("&cYou don't have enough money for that!"));
                    return true;
                }
            }else {
                player.sendMessage(ColorUtils.translateColorCodes("&cSorry, &d" + targetName + " &cdoes not appear to be online."));
                return true;
            }
            return true;
        }
        return true;
    }
}
