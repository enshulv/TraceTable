package org.EMAC.listener.util;

import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Damageable;

public class ItemDetailsUtil {
    // 获取附魔
    public static String getEnchantmentString(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        Map<Enchantment, Integer> enchants = meta.getEnchants();

        if (enchants.isEmpty()) {
            return "null";
        }

        StringBuilder enchantsStringBuilder = new StringBuilder();

        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            Enchantment enchantment = entry.getKey();
            Integer level = entry.getValue();
    
            // 把附魔名和等级添加到字符串
            enchantsStringBuilder.append(enchantment.getKey()).append(": ").append(level).append(", ");
        }
    
        // 删除最后的逗号和空格
        if (enchantsStringBuilder.length() > 0) {
            enchantsStringBuilder.setLength(enchantsStringBuilder.length() - 2);
        }
    
        return enchantsStringBuilder.toString();
    }

    // 获取耐久度
    public static int getDurability(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if(meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            return damageable.getDamage();
        } else {
            return -1;  // 返回-1表示物品不能损坏，没有耐久度
        }
    }
}
