package org.EMAC.listener.event;

import org.EMAC.listener.TraceTable;
import org.bukkit.Bukkit;

public class EventRegister {

    private TraceTable plugin;

    public EventRegister(TraceTable plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        // Bukkit.getPluginManager().registerEvents(new CraftEventListener(plugin), plugin);
        // Bukkit.getPluginManager().registerEvents(new SmeltEventListener(plugin), plugin);
        // Bukkit.getPluginManager().registerEvents(new EnchantEventListener(plugin), plugin);
        // Bukkit.getPluginManager().registerEvents(new PlayerQuitEventListener(plugin), plugin);
    }
}
