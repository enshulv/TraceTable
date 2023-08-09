package org.EMAC.listener.task;

import org.EMAC.listener.database.DatabaseOperations;
import org.EMAC.listener.info.ItemInfo;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DatabaseTask extends BukkitRunnable {

    private DatabaseOperations repository;
    private Map<UUID, List<ItemInfo>> map;

    public DatabaseTask(DatabaseOperations repository,Map<UUID, List<ItemInfo>> map) {
        this.repository = repository;
        this.map = map;
    }

    @Override
    public void run() {
        repository.saveAll(map);
    }
}

