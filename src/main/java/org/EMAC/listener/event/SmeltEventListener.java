package org.EMAC.listener.event;

import org.EMAC.listener.TraceTable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class SmeltEventListener implements Listener {

    private TraceTable plugin;

    public SmeltEventListener(TraceTable plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        // 你原来的 onFurnaceSmelt 方法的代码在这里
    }
}
