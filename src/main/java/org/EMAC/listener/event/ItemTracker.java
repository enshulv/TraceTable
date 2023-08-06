package org.EMAC.listener.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import org.EMAC.listener.util.ItemInfoUtil;
import org.EMAC.listener.info.ItemInfo;
import org.EMAC.listener.util.ItemDetailsUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ItemTracker implements Listener {

    private final Map<UUID, List<ItemInfo>> operationMap = new ConcurrentHashMap<>();

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
        String enchString = ItemDetailsUtil.getEnchantmentString(itemStack);

        ItemInfoUtil.computeItemInfo(operationMap, playerId, player, itemName, "pickup", enchString, amount);
    }

    // 监听玩家丢弃物品的事件
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack itemStack = event.getItemDrop().getItemStack();
        String itemName = itemStack.getType().name();
        int amount = itemStack.getAmount();
        String enchString = ItemDetailsUtil.getEnchantmentString(itemStack);

        ItemInfoUtil.computeItemInfo(operationMap, playerId, player, itemName, "drop", enchString, amount);
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
        String enchString = ItemDetailsUtil.getEnchantmentString(itemStack);

        ItemInfoUtil.computeItemInfo(operationMap, playerId, player, itemName, "place", enchString, amount);
    }

    // 监听玩家对容器的点击事件
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 确认是玩家触发的事件
        if (!(event.getWhoClicked() instanceof Player))
            return;

        // 确认点击的不是空气
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getType() == Material.AIR)
            return;

        // 确认是容器移动操作
        if (event.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY)
            return;

        Player player = (Player) event.getWhoClicked();
        UUID playerId = player.getUniqueId();
        ItemStack cursor = event.getCursor();
        InventoryType inventoryType = event.getClickedInventory().getType();
        String clickedInventoryName = inventoryType.name();
        String objectContainerName = event.getView().getTopInventory().getType().name();
        SlotType slotType = event.getSlotType();
        int amount = ItemDetailsUtil.getDurability(cursor);
        String enchString = ItemDetailsUtil.getEnchantmentString(cursor);
        Boolean backpackClick = false;
        Boolean playerClickObject = objectContainerName.equals("PLAYER");

        // 判断点击容器是否为玩家背包栏位
        switch (slotType) {
            case QUICKBAR:
                backpackClick = true;
                break;
            case CONTAINER:
                if (clickedInventoryName.equals("PLAYER")) {
                    backpackClick = true;
                }
                break;
            case ARMOR:
                backpackClick = true;
                break;
            default:
                break;
        }

        // 从背包移动到其他容器
        if (backpackClick && !(playerClickObject)) {
            String stitching = String.format("%s:%s", objectContainerName, slotType.name());
            ItemInfoUtil.computeItemInfo(operationMap, playerId, player, currentItem.getItemMeta().getDisplayName(),
                    stitching, enchString, amount);
        }
        // 从其他容器移动到背包
        else if (!(backpackClick) && playerClickObject) {
            ItemInfoUtil.computeItemInfo(operationMap, playerId, player, currentItem.getItemMeta().getDisplayName(),
                    "PLAYER", enchString, amount);
        }
    }
}
