package org.EMAC.listener.event;

import org.EMAC.listener.TraceTable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantEventListener implements Listener {

    private TraceTable plugin;

    public EnchantEventListener(TraceTable plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        // 你原来的 onEnchantItem 方法的代码在这里
    }
}
