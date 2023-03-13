package com.tcoded.playerbountiesplus.utils;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import com.tcoded.playerbountiesplus.models.Bounty;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class BountiesStorageUtils {

    public int taskID1;

    Logger logger = PlayerBountiesPlus.getPlugin().getLogger();

    FileConfiguration bountiesFile = PlayerBountiesPlus.getPlugin().bountiesFileManager.getBountiesConfig();

    private HashMap<UUID, Bounty> bountiesMap = new HashMap<>();
    private HashMap<UUID, Integer> strippedBountyValues = new HashMap<>();

    public void runStrippedBountyValuePopulate(){
        taskID1 = Bukkit.getScheduler().scheduleSyncDelayedTask(PlayerBountiesPlus.getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Bounty> bountyEntry : bountiesMap.entrySet()){
                    UUID uuid = UUID.fromString(bountyEntry.getValue().getTargetUUID());
                    int bountyValue = bountyEntry.getValue().getBountyValue();
                    strippedBountyValues.put(uuid, bountyValue);
                }
            }
        }, 10);
    }


    public void saveBounties() throws IOException{
        for (Map.Entry<UUID, Bounty> entry : bountiesMap.entrySet()){
            bountiesFile.set("bounties.data." + entry.getKey() + ".targetUUID", entry.getValue().getTargetUUID());
            bountiesFile.set("bounties.data." + entry.getKey() + ".creatorUUID", entry.getValue().getBountyCreator());
            bountiesFile.set("bounties.data." + entry.getKey() + ".bountyValue", entry.getValue().getBountyValue());
        }
        PlayerBountiesPlus.getPlugin().bountiesFileManager.saveBountiesConfig();
    }

    public void restoreBounties() throws IOException{
        bountiesMap.clear();
        bountiesFile.getConfigurationSection("bounties.data").getKeys(false).forEach(key ->{
            UUID uuid = UUID.fromString(key);
            String targetUUID = bountiesFile.getString("bounties.data." + key + ".targetUUID");
            String creatorUUID = bountiesFile.getString("bounties.data." + key + ".creatorUUID");
            int bountyValue = bountiesFile.getInt("bounties.data." + key + ".bountyValue");

            Bounty bounty = new Bounty(targetUUID, creatorUUID, bountyValue);

            bountiesMap.put(uuid, bounty);
        });
    }

    public Bounty createOnlineBounty(Player bountyTarget, Player bountyCreator, int bountyValue){
        UUID targetUUID = bountyTarget.getUniqueId();
        String targetUUIDString = targetUUID.toString();
        UUID creatorUUID = bountyCreator.getUniqueId();
        String creatorUUIDString = creatorUUID.toString();

        Bounty bounty = new Bounty(targetUUIDString, creatorUUIDString, bountyValue);

        bountiesMap.put(targetUUID, bounty);

        return bounty;
    }

    public Bounty createOfflineBounty(OfflinePlayer offlineBountyTarget, Player bountyCreator, int bountyValue){
        UUID targetUUID = offlineBountyTarget.getUniqueId();
        String targetUUIDString = targetUUID.toString();
        UUID creatorUUID = bountyCreator.getUniqueId();
        String creatorUUIDString = creatorUUID.toString();

        Bounty bounty = new Bounty(targetUUIDString, creatorUUIDString, bountyValue);

        bountiesMap.put(targetUUID, bounty);

        return bounty;
    }

    public boolean hasExistingBounty(Player bountyTarget){
        UUID uuid = bountyTarget.getUniqueId();
        return bountiesMap.containsKey(uuid);
    }

    public boolean hasExistingBounty(OfflinePlayer offlineBountyTarget){
        UUID uuid = offlineBountyTarget.getUniqueId();
        return bountiesMap.containsKey(uuid);
    }

    public boolean removeOnlineBounty(Player bountyTarget) throws IOException{
        UUID uuid = bountyTarget.getUniqueId();
        String key = uuid.toString();
        if (findBountyByOnlineTarget(bountyTarget) != null){
            if (bountiesMap.containsKey(uuid)){
                bountiesMap.remove(uuid);
                bountiesFile.set("bounties.data." + key, null);
                PlayerBountiesPlus.getPlugin().bountiesFileManager.saveBountiesConfig();
                return true;
            }else {
                logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Could not save bounties.yml"));
                return false;
            }
        }
        return false;
    }

    public boolean removeOfflineBounty(OfflinePlayer offlineBountyTarget) throws IOException{
        UUID uuid = offlineBountyTarget.getUniqueId();
        String key = uuid.toString();
        if (findBountyByOfflineTarget(offlineBountyTarget) != null){
            if (bountiesMap.containsKey(uuid)){
                bountiesMap.remove(uuid);
                bountiesFile.set("bounties.data." + key, null);
                PlayerBountiesPlus.getPlugin().bountiesFileManager.saveBountiesConfig();
                return true;
            }else {
                logger.severe(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &4Could not save bounties.yml"));
                return false;
            }
        }
        return false;
    }

    public Bounty findBountyByOnlineTarget(Player bountyTarget){
        UUID uuid = bountyTarget.getUniqueId();
        return bountiesMap.get(uuid);
    }

    public Bounty findBountyByOfflineTarget(OfflinePlayer bountyTarget){
        UUID uuid = bountyTarget.getUniqueId();
        return bountiesMap.get(uuid);
    }

    public Bounty findBountyByOnlineCreator(Player bountyCreator){
        for (Bounty bounty : bountiesMap.values()){
            if (findBountyByOnlineTarget(bountyCreator) != null){
                return bounty;
            }
            if (bounty.getBountyCreator().equals(bountyCreator.getUniqueId().toString())){
                return bounty;
            }
        }
        return null;
    }

    public Bounty findBountyByOfflineCreator(OfflinePlayer offlineBountyCreator){
        for (Bounty bounty : bountiesMap.values()){
            if (findBountyByOfflineTarget(offlineBountyCreator) != null){
                return bounty;
            }
            if (bounty.getBountyCreator().equals(offlineBountyCreator.getUniqueId().toString())){
                return bounty;
            }
        }
        return null;
    }

    public void updateOnlineBountyValue(Player bountyTarget, int newBountyValue){
        UUID uuid = bountyTarget.getUniqueId();
        if (!hasExistingBounty(bountyTarget)){
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &cPlayer &d" + bountyTarget.getName() + " &cdoes not have a bounty already set!"));
            return;
        }
        Bounty bounty = bountiesMap.get(uuid);
        if (bounty.getBountyValue() > 0){
            bounty.setBountyValue(bounty.getBountyValue() + newBountyValue);
        }else {
            bounty.setBountyValue(newBountyValue);
        }
    }

    public void updateOfflineBountyValue(OfflinePlayer offlineBountyTarget, int newBountyValue){
        UUID uuid = offlineBountyTarget.getUniqueId();
        if (!hasExistingBounty(offlineBountyTarget)){
            logger.warning(ColorUtils.translateColorCodes("&6PlayerBountiesPlus: &cPlayer &d" + offlineBountyTarget.getName() + " &cdoes not have a bounty already set!"));
            return;
        }
        Bounty bounty = bountiesMap.get(uuid);
        if (bounty.getBountyValue() >= newBountyValue){
            bounty.setBountyValue(newBountyValue);
        }else {
            bounty.setBountyValue(bounty.getBountyValue() + newBountyValue);
        }
    }

    public Set<Map.Entry<UUID, Bounty>> getBounties(){
        return bountiesMap.entrySet();
    }

    public Set<UUID> getRawBountiesList(){
        return bountiesMap.keySet();
    }

    public Collection<Bounty> getBountiesList(){
        return bountiesMap.values();
    }

    public Set<Map.Entry<UUID, Integer>> getStrippedBounties(){
        return strippedBountyValues.entrySet();
    }
}
