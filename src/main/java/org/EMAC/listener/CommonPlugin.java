package org.EMAC.listener;

import com.zaxxer.hikari.HikariDataSource;

import org.bukkit.plugin.java.JavaPlugin;
import com.zjyl1994.minecraftplugin.multicurrency.MultiCurrencyPlugin;

public abstract class CommonPlugin extends JavaPlugin{

    private HikariDataSource hikari;

    protected HikariDataSource getDataSource() {
        hikari =(HikariDataSource) MultiCurrencyPlugin.getInstance().getHikari();
        return hikari;
    }

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();
}
