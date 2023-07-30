package org.EMAC.listener;

import org.EMAC.listener.database.GetForm;
import org.EMAC.listener.event.EventRegister;

import com.zaxxer.hikari.HikariDataSource;

public class TraceTable extends CommonPlugin {
    private GetForm hikari = new GetForm();
    private static HikariDataSource db;

    @Override
    public void onEnable() {
        EventRegister eventRegister = new EventRegister(this);
        eventRegister.registerAll();
        db = hikari.getForm();
    }

    public HikariDataSource getdb() {
        return db;
    }
}

