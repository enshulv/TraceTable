package org.EMAC.listener;

import javax.sql.DataSource;

import org.EMAC.listener.database.GetForm;
import org.EMAC.listener.event.EventRegister;

import com.zaxxer.hikari.HikariDataSource;

public class TraceTable extends CommonPlugin {
    private GetForm hikari = new GetForm();
    private HikariDataSource db;

    @Override
    public void onEnable() {
        EventRegister eventRegister = new EventRegister(this);
        eventRegister.registerAll();

        db = hikari.getForm("TraceTable");
    }

    public HikariDataSource getdb() {
        return db;
    }
}

