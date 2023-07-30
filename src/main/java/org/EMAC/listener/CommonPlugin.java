package org.EMAC.listener;

import com.zaxxer.hikari.HikariDataSource;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommonPlugin extends JavaPlugin {

    protected HikariDataSource getDataSource() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("MultiCurrency");
        if (plugin instanceof CommonPlugin) {
            CommonPlugin corePlugin = (CommonPlugin) plugin;
            HikariDataSource dataSource = corePlugin.getDataSource();
            return dataSource;
        }else {
            throw new RuntimeException("CommonPlugin not found or not the right type");
        }
    }
}
