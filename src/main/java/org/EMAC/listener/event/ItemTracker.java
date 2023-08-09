package org.EMAC.listener.event;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import org.EMAC.listener.util.ItemInfoUtil;
import org.EMAC.listener.database.DatabaseOperations;
import org.EMAC.listener.info.ItemInfo;
import org.EMAC.listener.util.ItemDetailsUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ItemTracker implements Listener {

    private final Map<UUID, List<ItemInfo>> operationMap = new ConcurrentHashMap<>();
    private final Map<UUID, InventoryClickEvent> moveMap = new ConcurrentHashMap<>();
    private DatabaseOperations databaseOperations;

    public ItemTracker(DatabaseOperations databaseOperations) {
        this.databaseOperations = databaseOperations;
    }

    public Map<UUID, List<ItemInfo>> getMap() {
        return this.operationMap;
    }

    // 监听玩家拾取物品的事件
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player))
            return;
        System.out.println("Pickup");

        Player player = (Player) entity;
        UUID playerId = player.getUniqueId();
        ItemStack itemStack = event.getItem().getItemStack();
        String itemName = itemStack.getType().name();
        int amount = itemStack.getAmount();
        String enchString = ItemDetailsUtil.getEnchantmentString(itemStack);

        ItemInfoUtil.computeItemInfo(operationMap, playerId, player, itemName, "pickup", enchString, amount);
    }

    // 监听玩家丢弃物品的事件
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        System.out.println("Drop");
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
        System.out.println("Place");
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack itemStack = event.getItemInHand();
        String itemName = itemStack.getType().name();
        int amount = itemStack.getAmount();
        String enchString = ItemDetailsUtil.getEnchantmentString(itemStack);

        ItemInfoUtil.computeItemInfo(operationMap, playerId, player, itemName, "place", enchString, amount);
    }

    private Boolean getBackpackClick(String clickedInventoryName) {
        Boolean backpackClick = false;
        if (clickedInventoryName.equals("PLAYER")) {
            backpackClick = true;
        }
        return backpackClick;
    }

    private boolean isBurnable(Material material) {

        return false;
    }

    // 监听玩家对容器的点击事件
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 确认是玩家触发的事件
        if (!(event.getWhoClicked() instanceof Player))
            return;

        // 确认点击的不是空气和不可点的地方
        ItemStack currentItem = event.getCurrentItem();
        ItemStack curren = event.getCursor();

        if ((curren.getType() == Material.AIR && currentItem.getType() == Material.AIR) || currentItem == null)
            return;

        InventoryAction action = event.getAction();
        Player player = (Player) event.getWhoClicked();
        UUID playerId = player.getUniqueId();
        switch (action) {
            case PICKUP_ALL:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case PICKUP_SOME:
                moveMap.put(playerId, event);
                return;
            default:
                break;
        }

        String clickedInventoryName, topContainerName, enchString;
        int amount;
        Boolean backpackClick;
        SlotType slotType = event.getSlotType();
        clickedInventoryName = event.getClickedInventory().getType().name();
        topContainerName = event.getView().getTopInventory().getType().name();

        switch (action) {
            case MOVE_TO_OTHER_INVENTORY:
                backpackClick = getBackpackClick(clickedInventoryName);
                enchString = ItemDetailsUtil.getEnchantmentString(currentItem);
                amount = currentItem.getAmount();
                break;
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
            case SWAP_WITH_CURSOR:
                InventoryClickEvent lastEvent = moveMap.get(playerId);
                String lastClickedInventoryName = lastEvent.getClickedInventory().getType().name();
                // 确定本次和上次的点击是否发生在一个容器
                if (clickedInventoryName.equals(lastClickedInventoryName)) {
                    // moveMap.remove(playerId);
                    return;
                }
                //判断是否再往往无法放置物品的栏位里放置
                if (slotType == SlotType.RESULT) {
                    // System.out.println(slotType);
                    return;
                }
                backpackClick = getBackpackClick(lastClickedInventoryName);
                enchString = ItemDetailsUtil.getEnchantmentString(curren);
                amount = curren.getAmount();
                currentItem = curren;
                if (action == InventoryAction.SWAP_WITH_CURSOR)
                    moveMap.put(playerId, event);
                break;
            default:
                return;
        }
        // 从背包移动到其他容器
        if (backpackClick) {
            String stitching = String.format("%s>%s:%s", "PLAYER", topContainerName, slotType.name());
            ItemInfoUtil.computeItemInfo(operationMap, playerId, player,
                    currentItem.getType().name(),
                    stitching, enchString, amount);
        }
        // 从其他容器移动到背包
        else {
            String stitching = String.format("%s:%s>%s", topContainerName, slotType.name(), "PLAYER");
            ItemInfoUtil.computeItemInfo(operationMap, playerId, player,
                    currentItem.getType().name(),
                    stitching, enchString, amount);
        }
        System.out.println(operationMap);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!databaseOperations.isPlayerInDatabase(player.getUniqueId().toString())) {
            databaseOperations.insertPlayerIntoDatabase(player.getUniqueId().toString(), player.getName());
        }
    }
}
