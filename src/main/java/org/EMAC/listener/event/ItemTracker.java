package org.EMAC.listener.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import org.EMAC.listener.util.ItemInfo;
import org.EMAC.listener.util.ItemDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ItemTracker implements Listener {

    private final Map<UUID, ItemInfo> pickupMap = new ConcurrentHashMap<>();
    private final Map<UUID, ItemInfo> dropMap = new ConcurrentHashMap<>();
    private final Map<UUID, ItemInfo> placeMap = new ConcurrentHashMap<>();
    private final Map<UUID, ContainerMoveInfo> moveMap = new ConcurrentHashMap<>();

    // 监听玩家拾取物品的事件
    @EventHandler
    public void onItemPickup(ItemSpawnEvent event) {
        if (!(event.getEntity().getNearbyEntities(1, 1, 1).get(0) instanceof Player))
            return;

        Player player = (Player) event.getEntity().getNearbyEntities(1, 1, 1).get(0);
        UUID playerId = player.getUniqueId();
        ItemStack itemStack = event.getEntity().getItemStack();
        String itemName = itemStack.getType().name();
        int amount = itemStack.getAmount();
        String enchString = ItemDetails.getEnchantmentString(itemStack);
        int durability = ItemDetails.getDurability(itemStack);

        pickupMap.compute(playerId, (uuid, iteminfo) -> {
            if (iteminfo != null
                    && iteminfo.itemName.equals(itemName)
                    && iteminfo.enchant.equals(enchString)
                    && iteminfo.damage == durability) {
                iteminfo.setAmount(iteminfo.amount + amount);
                iteminfo.timestamps.add(System.currentTimeMillis());
                return iteminfo;
            } else {
                ItemInfo newItemDropInfo = new ItemInfo(
                        player.getName(), itemName, enchString, durability, amount,
                        new ArrayList<>(Arrays.asList(System.currentTimeMillis())));
                return newItemDropInfo;
            }
        });
    }

    // 监听玩家丢弃物品的事件
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack itemStack = event.getItemDrop().getItemStack();
        String itemName = itemStack.getType().name();
        int amount = itemStack.getAmount();
        String enchString = ItemDetails.getEnchantmentString(itemStack);
        int durability = ItemDetails.getDurability(itemStack);

        dropMap.compute(playerId, (uuid, iteminfo) -> {
            if (iteminfo != null
                    && iteminfo.itemName.equals(itemName)
                    && iteminfo.enchant.equals(enchString)
                    && iteminfo.damage == durability) {
                iteminfo.setAmount(iteminfo.amount + amount);
                iteminfo.timestamps.add(System.currentTimeMillis());
                return iteminfo;
            } else {
                ItemInfo newItemDropInfo = new ItemInfo(
                        player.getName(), itemName, enchString, durability, amount,
                        new ArrayList<>(Arrays.asList(System.currentTimeMillis())));
                return newItemDropInfo;
            }
        });
    }

    // 监听玩家放置物品的事件
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack itemStack = event.getItemInHand();
        String itemName = itemStack.getType().name();
        int amount = itemStack.getAmount();
        String enchString = ItemDetails.getEnchantmentString(itemStack);
        int durability = ItemDetails.getDurability(itemStack);

        placeMap.compute(playerId, (uuid, iteminfo) -> {
            if (iteminfo != null
                    && iteminfo.itemName.equals(itemName)
                    && iteminfo.enchant.equals(enchString)
                    && iteminfo.damage == durability) {
                iteminfo.setAmount(iteminfo.amount + amount);
                iteminfo.timestamps.add(System.currentTimeMillis());
                return iteminfo;
            } else {
                ItemInfo newItemPlaceInfo = new ItemInfo(
                        player.getName(), itemName, enchString, durability, amount,
                        new ArrayList<>(Arrays.asList(System.currentTimeMillis())));
                return newItemPlaceInfo;
            }
        });
    }

    // 监听玩家在背包和其他容器之间移动物品的事件
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 确认是玩家触发的事件
        if (!(event.getWhoClicked() instanceof Player))
            return;
        // 获取移动物品的玩家
        Player player = (Player) event.getWhoClicked();
        // 获取移动的物品堆
        ItemStack itemStack = event.getCurrentItem();
        // 确保物品堆不为空
        if (itemStack == null || itemStack.getType() == Material.AIR)
            return;
        // 如果玩家按住shift键点击，那么物品将会直接移动到背包或者容器
        // 这种情况我们认为是移动到容器
        if (event.isShiftClick()) {
            moveMap.put(player.getUniqueId().toString(), new ContainerMoveInfo(itemStack.getType(),
                    itemStack.getAmount(), event.getInventory().getType().name(), System.currentTimeMillis()));
        } else {
            // 否则我们认为是从容器移动到背包
            pickupMap.put(player.getUniqueId().toString(),
                    new ItemInfo(itemStack.getType(), itemStack.getAmount(), System.currentTimeMillis()));
        }
    }

    // 容器类型字段
    static class ContainerMoveInfo extends ItemInfo {
        String containerType;

        ContainerMoveInfo(Material material, int quantity, String containerType, long timestamp) {
            super(material, quantity, timestamp);
            this.containerType = containerType;
        }
    }
}
