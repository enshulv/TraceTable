package org.EMAC.listener.event;

import org.EMAC.listener.TraceTable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftEventListener implements Listener {

    private TraceTable plugin;

    public CraftEventListener(TraceTable plugin) {
        this.plugin = plugin;
    }

    //当使用工作台时
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {

        // 投产都存JsonText
    }
}
