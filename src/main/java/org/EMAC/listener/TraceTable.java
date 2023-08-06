package org.EMAC.listener;

import org.EMAC.listener.database.GetForm;
import org.EMAC.listener.event.ItemTracker;
import org.bukkit.Bukkit;

import com.zaxxer.hikari.HikariDataSource;

public class TraceTable extends CommonPlugin {
    private GetForm hikari = new GetForm();
    private static HikariDataSource db;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ItemTracker(), this);
        db = hikari.getForm();
    }

    public HikariDataSource getdb() {
        return db;
    }
}

