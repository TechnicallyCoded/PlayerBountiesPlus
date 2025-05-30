package com.tcoded.playerbountiesplus.listener;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.event.BountyClaimEvent;
import com.tcoded.playerbountiesplus.hook.team.TeamHook;
import com.tcoded.playerbountiesplus.manager.BountyDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class DeathListener implements Listener {

    private final PlayerBountiesPlus plugin;

    public DeathListener(PlayerBountiesPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            BountyDataManager bountyDataManager = this.plugin.getBountyDataManager();
            UUID victimId = victim.getUniqueId();

            // Bounty check
            if (!bountyDataManager.hasBounty(victimId)) {
                return;
            }

            int bounty = bountyDataManager.getBounty(victimId);

            // Clan check
            TeamHook teamHook = this.plugin.getTeamHook();
            if (teamHook != null && teamHook.isFriendly(killer, victim)) {
                killer.sendMessage(plugin.getLang().getColored("death.same-team"));
                return;
            }

            // Trigger Bounty Claimed Event
            BountyClaimEvent claimEvent = new BountyClaimEvent(killer, victim, bounty);
            this.plugin.getServer().getPluginManager().callEvent(claimEvent);

            // Check if event is cancelled
            if (claimEvent.isCancelled()) {
                return;
            }

            // Give reward!
            if (plugin.getConfig().getBoolean("bounty-claimable", true)) {
                double claimMultiplier = plugin.getConfig().getDouble("bounty-claim-multiplier", 1.0);
                double awardedAmount = bounty * claimMultiplier;

                if (awardedAmount > 0) {
                    this.plugin.getEcoHook().giveEco(killer, awardedAmount);
                } else if (awardedAmount < 0) {
                    this.plugin.getEcoHook().takeEco(killer, Math.abs(awardedAmount));
                }
            }

            // Optional - Take from victim as punishment
            if (plugin.getConfig().getBoolean("bounty-take-from-victim", false)) {
                this.plugin.getEcoHook().takeEco(victim, bounty, true);
            }

            // Announce Reward
            if (plugin.getConfig().getBoolean("bounty-claimed-announce", true)) {
                this.plugin.getServer().broadcastMessage(
                        plugin.getLang().getColored("death.announce-claimed")
                                .replace("{killer}", killer.getName())
                                .replace("{victim}", victim.getName())
                                .replace("{bounty}", String.valueOf(bounty))
                );
            }

            // Remove bounty
            bountyDataManager.removeBounty(victimId);
            bountyDataManager.saveBountiesAsync();
        }
    }

}
