package org.EMAC.listener.util;

import java.util.List;

import lombok.Data;

@Data
public class ContainerMoveInfo {
    private String playerName;
    private String objectContainerName;
    private String itemName;
    private String enchant;
    private int amount;
    private List<Long> timestamps;

    public ContainerMoveInfo(String playerName, String objectContainerName, String itemName,
            String enchant, int amount, List<Long> timestamps) {
        this.playerName = playerName;
        this.objectContainerName = objectContainerName;
        this.itemName = itemName;
        this.enchant = enchant;
        this.amount = amount;
        this.timestamps = timestamps;
    }
}
