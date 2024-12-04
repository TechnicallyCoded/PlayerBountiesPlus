package com.tcoded.playerbountiesplus.manager;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class BountyDataManager {

    private final PlayerBountiesPlus plugin;

    private File bountiesFile;
    private FileConfiguration bountiesConfig;

    private final Object savingFileLock = new Object();
    private final AtomicBoolean savingAsync = new AtomicBoolean(false);
    private final ConcurrentHashMap<UUID, Integer> bounties;

    public BountyDataManager(PlayerBountiesPlus plugin) {
        this.plugin = plugin;
        this.bounties = new ConcurrentHashMap<>();
    }

    public void init() {
        // Bounties file
        String bountiesFileName = "bounties.yml";
        bountiesFile = new File(this.plugin.getDataFolder(), bountiesFileName);
        if (!bountiesFile.exists()) this.plugin.saveResource(bountiesFileName, false);
        bountiesConfig = YamlConfiguration.loadConfiguration(bountiesFile);

        // Load bounties
        Set<String> keys = bountiesConfig.getKeys(false);
        for (String key: keys) {
            try {
                this.bounties.put(UUID.fromString(key), bountiesConfig.getInt(key));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public ConcurrentHashMap<UUID, Integer> getBounties() {
        return this.bounties;
    }

    public boolean hasBounty(UUID player) {
        return this.bounties.containsKey(player) && this.bounties.get(player) > 0;
    }

    public void setBounty(UUID player, int amount) {
        this.bounties.put(player, amount);
    }

    public int getBounty(UUID player) {
        return this.bounties.getOrDefault(player, 0);
    }

    public void removeBounty(UUID player) {
        this.bounties.remove(player);
    }

    public void saveBounties() {
        synchronized (this.savingFileLock) {
            // Clear existing
            Set<String> keys = bountiesConfig.getKeys(false);
            for (String key : keys) {
                bountiesConfig.set(key, null);
            }

            // Write changes
            for (Map.Entry<UUID, Integer> entry : this.bounties.entrySet()) {
                this.bountiesConfig.set(entry.getKey().toString(), entry.getValue());
            }

            // Save to file
            try {
                this.bountiesConfig.save(this.bountiesFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveBountiesAsync() {
        // Wait until the previous save is done
        while (this.savingAsync.getAndSet(true)) {
            try {
                synchronized (this.savingAsync) {
                    this.savingAsync.wait();
                }
            } catch (InterruptedException ignored) {}
        }

        // Save
        this.plugin.getFoliaLib().getImpl().runAsync(() -> {
            this.saveBounties();

            this.savingAsync.set(false);
            synchronized (this.savingAsync) {
                this.savingAsync.notifyAll();
            }
        });
    }

}
