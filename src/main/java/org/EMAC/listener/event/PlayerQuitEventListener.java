package org.EMAC.listener.event;

import org.EMAC.listener.TraceTable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener implements Listener {

    private TraceTable plugin;

    public PlayerQuitEventListener(TraceTable plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // 你原来的 onPlayerQuit 方法的代码在这里
    }
}