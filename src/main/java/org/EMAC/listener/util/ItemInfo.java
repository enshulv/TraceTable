package org.EMAC.listener.util;

import java.util.List;

// 关于物品的信息，包括物品名称、数量和时间戳
public class ItemInfo {
    public String playerName;
    public String itemName;
    public String enchant;
    public int damage;
    public int amount;
    public List<Long> timestamps;

    public ItemInfo(String playerName, String itemName, String enchant, int amount, int damage, List<Long> timestamps) {
        this.playerName = playerName;
        this.itemName = itemName;
        this.enchant = enchant;
        this.damage = damage;
        this.amount = amount;
        this.timestamps = timestamps;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setEnchant(String enchant) {
        this.enchant = enchant;
    }

    public void increaseAmount() {
        this.amount++;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
    }

}
