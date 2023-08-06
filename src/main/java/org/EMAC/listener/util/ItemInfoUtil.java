package org.EMAC.listener.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.EMAC.listener.info.ItemInfo;
import org.bukkit.entity.Player;

public class ItemInfoUtil {
    public static void computeItemInfo(Map<UUID, List<ItemInfo>> itemInfoMap, UUID playerId, Player player,
            String itemName, String operation, String enchString, int amount) {
        itemInfoMap.compute(playerId, (uuid, itemInfoList) -> {
            if (itemInfoList == null) {
                itemInfoList = new ArrayList<>();
            }
            boolean found = false;
            for (ItemInfo itemInfo : itemInfoList) {
                if (itemInfo.getItemName().equals(itemName)) {
                    if (itemInfo.getEnchant().equals(enchString)
                            && itemInfo.getOperation().equals(operation)) {
                        itemInfo.setAmount(itemInfo.getAmount() + amount);
                        itemInfo.getTimestamps().add(System.currentTimeMillis());
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                ItemInfo newItemInfo = new ItemInfo(player.getName(), itemName, operation, enchString, amount,
                        new ArrayList<>(Arrays.asList(System.currentTimeMillis())));
                itemInfoList.add(newItemInfo);
            }
            return itemInfoList;
        });
    }
}
