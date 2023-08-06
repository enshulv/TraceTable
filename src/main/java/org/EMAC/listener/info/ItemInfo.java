package org.EMAC.listener.info;

import java.util.List;
import lombok.Data;

// 关于物品的信息，包括物品名称、数量和时间戳
@Data
public class ItemInfo {
    private String playerName;
    private String itemName;
    private String enchant;
    private String operation;
    private int damage;
    private int amount;
    private List<Long> timestamps;

    public ItemInfo(String playerName, String itemName, String operation ,String enchant, int amount, List<Long> timestamps) {
        this.playerName = playerName;
        this.itemName = itemName;
        this.operation = operation;
        this.enchant = enchant;
        this.amount = amount;
        this.timestamps = timestamps;
    }
}
