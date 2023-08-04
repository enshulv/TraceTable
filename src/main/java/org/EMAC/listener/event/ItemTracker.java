package org.EMAC.listener.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.EMAC.listener.util.ItemInfo;
import org.EMAC.listener.util.ItemInfoUtil;
import org.EMAC.listener.util.ContainerMoveInfo;
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
    private final Map<UUID, String> playerContainerMap = new ConcurrentHashMap<>();

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

        ItemInfoUtil.computeItemInfo(pickupMap, playerId, player, itemName, enchString, amount);
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

        ItemInfoUtil.computeItemInfo(dropMap, playerId, player, itemName, enchString, amount);
        ;
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

        ItemInfoUtil.computeItemInfo(placeMap, playerId, player, itemName, enchString, amount);
    }

    // 监听玩家的容器打开操作
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player))
            return;
        UUID uuid = event.getPlayer().getUniqueId();
        String inventoryName = event.getInventory().getType().name();
        if (!(playerContainerMap.containsKey(uuid))) {
            playerContainerMap.put(uuid, inventoryName);
        }
        ;
    }

    // 监听玩家的容器关闭操作
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player))
            return;
        UUID uuid = event.getPlayer().getUniqueId();
        String inventoryName = event.getInventory().getType().name();
        if (playerContainerMap.containsKey(uuid)) {
            playerContainerMap.remove(uuid, inventoryName);
        }
        ;
    }

    // 监听玩家对容器的点击事件
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 确认是玩家触发的事件
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        //获取被点击的物品栏内容
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getType() == Material.AIR) return;

        Player player = (Player) event.getWhoClicked();
        UUID playerId = player.getUniqueId();
        //获取光标拿起的物品内容
        ItemStack cursor = event.getCursor();
        // 获取目前点击的容器的类型
        InventoryType inventoryType = event.getClickedInventory().getType();
        String clickedInventoryName = inventoryType.name();
        int amount;
        String enchString;

        // 物品从一个容器移动到另一个容器时
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
        && playerContainerMap.containsKey(playerId)) {
            //如果是从背包移动到其他
            String objectContainerName = playerContainerMap.get(playerId);
            if (clickedInventoryName.equals("PLAYER")){
                ItemInfoUtil.ItemMoveOperation(moveMap, playerId, player, currentItem.getItemMeta().getDisplayName(),objectContainerName, enchString, amount);
            }else if (clickedInventoryName.equals(objectContainerName)){
                ItemInfoUtil.ItemMoveOperation(moveMap, playerId, player, currentItem.getItemMeta().getDisplayName(),"PLAYER", enchString, amount);
            }
        }

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
