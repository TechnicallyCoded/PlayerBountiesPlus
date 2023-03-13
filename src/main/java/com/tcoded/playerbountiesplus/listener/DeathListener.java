package com.tcoded.playerbountiesplus.listener;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.hooks.clan.AbstractClanHook;
import com.tcoded.playerbountiesplus.utils.BountiesStorageUtils;
import com.tcoded.playerbountiesplus.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

public class DeathListener implements Listener {

    Logger logger = PlayerBountiesPlus.getPlugin().getLogger();
    BountiesStorageUtils bountiesStorageUtils = new BountiesStorageUtils();

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player victim = event.getEntity();
        UUID victimUUID = victim.getUniqueId();
        OfflinePlayer offlineVictim = Bukkit.getOfflinePlayer(victimUUID);
        Player killer = victim.getKiller();

        if (killer != null) {
            int bounty = bountiesStorageUtils.findBountyByOnlineTarget(victim).getBountyValue();
            String victimName = victim.getName();
            String killerName = killer.getName();

            //Bounty check
            if (bounty == 0) {
                return;
            }

            //Clan check
            AbstractClanHook clanHook = PlayerBountiesPlus.getPlugin().getClanHook();
            String clanIdVictim = clanHook.getClanId(victim);
            if (clanIdVictim != null && clanIdVictim.equals(clanHook.getClanId(killer))) {
                killer.sendMessage(ColorUtils.translateColorCodes(
                        "&aThe player you killed has an active bounty but you are in the same clan as they are!"));
                return;
            }

            //Apply rewards
            PlayerBountiesPlus.getPlugin().getVaultHook().addMoney(killer, bounty);
            try {
                if (bountiesStorageUtils.removeOfflineBounty(offlineVictim)){
                    Bukkit.getServer().broadcastMessage(ColorUtils.translateColorCodes(
                            "&b&l" + killerName +" &4claimed the bounty that was placed on &b&l" + victimName +" &4worth &b&l$" + bounty));
                }
            } catch (IOException e) {
                logger.severe(xyz.gamlin.clans.utils.ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Failed to update bounties in bounties.yml!"));
                logger.severe(xyz.gamlin.clans.utils.ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4See below error for reason!"));
                e.printStackTrace();
            }
        }
    }
}
