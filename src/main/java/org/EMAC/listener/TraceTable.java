package org.EMAC.listener;

import org.EMAC.listener.database.GetForm;
import org.EMAC.listener.database.DatabaseOperations;
import org.EMAC.listener.event.ItemTracker;
import org.EMAC.listener.task.DatabaseTask;

import com.zaxxer.hikari.HikariDataSource;

public class TraceTable extends CommonPlugin {
    private GetForm hikari = new GetForm();
    private static HikariDataSource db;

    @Override
    public void onEnable() {
        db = getDataSource();
        DatabaseOperations repository = new DatabaseOperations(db);
        ItemTracker event = new ItemTracker(repository);
        getServer().getPluginManager().registerEvents(event, this);
        db = hikari.getForm(db);
        new DatabaseTask(repository,event.getMap()).runTaskTimer(this, 20L*10, 20L*10);
    }

    public HikariDataSource getdb() {
        return db;
    }

    @Override
    public void onDisable() {

    }
}

