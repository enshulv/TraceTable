package org.EMAC.listener.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class ItemInfoUtil {
    public static void computeItemInfo(Map<UUID, ItemInfo> itemInfoMap, UUID playerId, Player player,
            String itemName, String enchString, int amount) {
        itemInfoMap.compute(playerId, (uuid, itemInfo) -> {
            if (itemInfo != null
                    && itemInfo.getItemName().equals(itemName)
                    && itemInfo.getEnchant().equals(enchString)) {
                itemInfo.setAmount(itemInfo.getAmount() + amount);
                itemInfo.getTimestamps().add(System.currentTimeMillis());
                return itemInfo;
            } else {
                ItemInfo newItemDropInfo = new ItemInfo(
                        player.getName(), itemName, enchString, amount,
                        new ArrayList<>(Arrays.asList(System.currentTimeMillis())));
                return newItemDropInfo;
            }
        });
    }

    public static void ItemMoveOperation(Map<UUID, ContainerMoveInfo> itemInfoMap, UUID playerId, Player player,
            String itemName, String objectContainerName, String enchString, int amount) {
        itemInfoMap.compute(playerId, (uuid, moveInfo) -> {
            if (moveInfo != null
                    && moveInfo.getItemName().equals(itemName)
                    && moveInfo.getEnchant().equals(enchString)
                    && moveInfo.getObjectContainerName().equals(objectContainerName)) {
                moveInfo.setAmount(moveInfo.getAmount() + amount);
                moveInfo.getTimestamps().add(System.currentTimeMillis());
                return moveInfo;
            } else {
                ContainerMoveInfo newContainerMoveInfo = new ContainerMoveInfo(player.getName(), objectContainerName,
                        itemName, enchString, amount, new ArrayList<>(Arrays.asList(System.currentTimeMillis())));
                return newContainerMoveInfo;
            }
        });

    }
}
