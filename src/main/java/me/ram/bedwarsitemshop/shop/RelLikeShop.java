package me.ram.bedwarsitemshop.shop;

import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.game.Game;
import me.ram.bedwarsitemshop.utils.ItemShopUtils;
import me.ram.bedwarsitemshop.utils.UpgradeUtils;
import me.ram.bedwarsitemshop.xpshop.ItemShop;
import me.ram.bedwarsitemshop.xpshop.XPItemShop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.ram.bedwarsscoreboardaddon.utils.Utils.isXpMode;

public class RelLikeShop implements Shop {

    public void onOpen(Game game, Player player, Inventory shop) {
        List<ItemStack> shops = ItemShopUtils.getShops(shop);
        List<ItemStack> shop_items = ItemShopUtils.getShopItems(shop);

        int categoryRows = shops.size() / 9;
        if (categoryRows * 9 < shops.size()) {
            categoryRows++;
        }

        int itemRows = shop_items.size() / 9;
        if (itemRows * 9 < shop_items.size()) {
            itemRows++;
        }
        if (itemRows == 0) {
            itemRows++;
        }

        Inventory inventory = Bukkit.createInventory(null, (categoryRows + itemRows) * 9,
                BedwarsRel._l(player, "ingame.shop.name") + "§n§e§w");

        int slot = 0;
        for (ItemStack item : shops) {
            inventory.setItem(slot, item);
            slot++;
        }

        slot = categoryRows * 9;

        for (ItemStack shopItem : shop_items) {
            if (slot >= inventory.getSize()) {
                break;
            }
            inventory.setItem(slot, shopItem);
            slot++;
        }

        if (isXpMode(game) && shop_items.isEmpty()) {
            ItemStack stack;
            if (game.getPlayerSettings(player).oneStackPerShift()) {
                stack = new ItemStack(Material.BUCKET, 1);
                ItemMeta meta = stack.getItemMeta();

                meta.setDisplayName(
                        ChatColor.AQUA + BedwarsRel._l(player, "default.currently") + ": " + ChatColor.WHITE
                                + BedwarsRel._l(player, "ingame.shop.onestackpershift"));
                meta.setLore(new ArrayList<>());
                stack.setItemMeta(meta);
            } else {
                stack = new ItemStack(Material.LAVA_BUCKET, 1);
                ItemMeta meta = stack.getItemMeta();

                meta.setDisplayName(
                        ChatColor.AQUA + BedwarsRel._l(player, "default.currently") + ": " + ChatColor.WHITE
                                + BedwarsRel._l(player, "ingame.shop.fullstackpershift"));
                meta.setLore(new ArrayList<>());
                stack.setItemMeta(meta);
            }
            inventory.setItem(inventory.getSize() - 4, stack);
        }

        player.openInventory(inventory);
    }

    public void onClick(Game game, InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (ItemShopUtils.getBackItem().isSimilar(e.getCurrentItem())) {
            game.getNewItemShop(player).openCategoryInventory(player);
            return;
        }
        Map<String, ItemStack> resname = ItemShopUtils.getResourceList();
        ItemStack currentItem = e.getCurrentItem();
        if (!ItemShopUtils.isShopItem(e.getCurrentItem())) {
            if (isXpMode(game)) {
                new XPItemShop(game.getNewItemShop(player).getCategories(), game).handleInventoryClick(e, game, player);
            } else {
                new ItemShop(game.getNewItemShop(player).getCategories()).handleInventoryClick(e, game, player);
            }
            return;
        }

        if (UpgradeUtils.isUpgradeItem(currentItem) && !isXpMode(game)) {
            ItemShopUtils.buyUpgrade(game, player, currentItem, resname);
            return;
        }

        if (e.isShiftClick()) {
            int ba = 64 / currentItem.getAmount();
            ItemShopUtils.buyItem(game, player, currentItem, resname, ba);
        } else {
            ItemShopUtils.buyItem(game, player, currentItem, resname, 1);
        }
    }
}