package com.tcoded.playerbountiesplus.command;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.command.subCommands.BountyCheckCmd;
import com.tcoded.playerbountiesplus.command.subCommands.BountySetCmd;
import com.tcoded.playerbountiesplus.command.subCommands.BountyTopCmd;
import com.tcoded.playerbountiesplus.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class BountyCommand implements CommandExecutor {

    Logger logger = PlayerBountiesPlus.getPlugin().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length < 1) {
                sender.sendMessage(ColorUtils.translateColorCodes("&6Bounty Commands Usage: &3" +
                        "\n/bounty set <player> <amount>" +
                        "\n/bounty check <player>" +
                        "\n/bounty top"));
                return true;
            }

            String action = args[0].toLowerCase();

            switch (action) {
                case "set":
                    if (args.length == 3){
                        if (args[2] != null){
                            int amount = Integer.parseInt(args[2]);
                            return new BountySetCmd().handleCmd(sender,args, amount);
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3/bounty set <player> <amount>"));
                            return true;
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3/bounty set <player> <amount>"));
                        return true;
                    }
                case "check":
                    if (args.length == 2){
                        if (args[1] != null){
                            return new BountyCheckCmd().handleCmd(sender, args);
                        }else {
                            player.sendMessage(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3/bounty check <player>"));
                            return true;
                        }
                    }else {
                        player.sendMessage(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &3/bounty check <player>"));
                        return true;
                    }
                case "top":
                    return new BountyTopCmd().handleCmd(sender);
                default:
                    player.sendMessage(ColorUtils.translateColorCodes("&cSorry, that command is not recognised.\n&cPlease use &3/bounty"));
            }
        }else {
            logger.warning(ColorUtils.translateColorCodes("&cSorry, that command can only be used in game."));
            return true;
        }
        return true;
    }
}
