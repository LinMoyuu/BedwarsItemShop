package me.ram.bedwarsitemshop.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class UpgradeUtils {

    public static boolean upgradeArmor(Player player, ItemStack itemStack) {
        Material material = itemStack.getType();
        String materialName = material.name();

        ItemStack leggings = null;
        ItemStack boots = null;
        Integer newMaterialLevel = null;

        // 判断玩家购买
        if (materialName.startsWith("CHAINMAIL_")) {
            leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
            boots = new ItemStack(Material.CHAINMAIL_BOOTS);
            newMaterialLevel = 1;
        } else if (materialName.startsWith("IRON_")) {
            leggings = new ItemStack(Material.IRON_LEGGINGS);
            boots = new ItemStack(Material.IRON_BOOTS);
            newMaterialLevel = 2;
        } else if (materialName.startsWith("DIAMOND_")) {
            leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
            boots = new ItemStack(Material.DIAMOND_BOOTS);
            newMaterialLevel = 3;
        }

        // 如果没有匹配的材质，直接返回
        if (leggings == null) {
            return false;
        }

        PlayerInventory inventory = player.getInventory();

        // 获取玩家当前装备
        ItemStack currentLeggings = inventory.getLeggings();
        ItemStack currentBoots = inventory.getBoots();

        // 获取当前装备的材质等级
        Integer currentLeggingsLevel = getMaterialLevel(currentLeggings);
        Integer currentBootsLevel = getMaterialLevel(currentBoots);

        // 降级检查
        if (currentLeggingsLevel != null && currentLeggingsLevel >= newMaterialLevel) {
            return false;
        }

        if (currentBootsLevel != null && currentBootsLevel >= newMaterialLevel) {
            return false;
        }

        // 只升级那些需要升级的装备
        ItemMeta leggingsMeta = leggings.getItemMeta();
        if (leggingsMeta != null) {
            leggingsMeta.spigot().setUnbreakable(true);
            leggings.setItemMeta(leggingsMeta);
        }
        inventory.setLeggings(leggings);

        ItemMeta bootsMeta = boots.getItemMeta();
        if (bootsMeta != null) {
            bootsMeta.spigot().setUnbreakable(true);
            boots.setItemMeta(bootsMeta);
        }
        inventory.setBoots(boots);

        player.updateInventory();
        return true;
    }

    public static boolean upgradeSword(Player player, ItemStack itemStack) {
        PlayerInventory inventory = player.getInventory();
        ItemStack newSword = itemStack.clone();

        // 在购买剑后 花雨庭会将之前的剑放进背包内
        // 没能想出什么方法 也没见过背包满的花雨庭怎么处理的 就这么生草了
        // 在Hotbar中寻找玩家当前的剑
        ItemStack oldSword = null;
        int oldSwordSlot = -1;
        for (int j = 0; j < 9; j++) {
            ItemStack itemInHotbar = inventory.getItem(j);
            if (ItemUtils.isSword(itemInHotbar)) {
                oldSword = itemInHotbar.clone();
                oldSwordSlot = j;
                break;
            }
        }

        // 将新剑放到旧剑的位置上. 如果没找到旧剑 则直接添加
        if (oldSwordSlot != -1) {
            inventory.setItem(oldSwordSlot, newSword);
        } else {
            inventory.addItem(newSword);
        }

        // 将旧剑移动到背包
        if (oldSword != null) {
            boolean placedInInventory = false;
            for (int j = 9; j <= 35; j++) {
                if (inventory.getItem(j) == null || inventory.getItem(j).getType() == Material.AIR) {
                    inventory.setItem(j, oldSword);
                    placedInInventory = true;
                    break;
                }
            }
            if (!placedInInventory) {
                player.getWorld().dropItemNaturally(player.getLocation(), oldSword);
            }
        }

        player.updateInventory();
        return true;
    }

    private static Integer getMaterialLevel(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        Material material = item.getType();
        String materialName = material.name();

        if (materialName.startsWith("CHAINMAIL_")) {
            return 1;
        } else if (materialName.startsWith("IRON_")) {
            return 2;
        } else if (materialName.startsWith("DIAMOND_")) {
            return 3;
        } else if (materialName.startsWith("NETHERITE_")) {
            return 4;
        }

        return 0;
    }
}
