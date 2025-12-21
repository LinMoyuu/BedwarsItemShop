package me.ram.bedwarsitemshop.shop;

import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.game.Game;
import me.ram.bedwarsitemshop.utils.ItemShopUtils;
import me.ram.bedwarsitemshop.xpshop.ItemShop;
import me.ram.bedwarsitemshop.xpshop.XPItemShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

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

        player.openInventory(inventory);
    }

    public void onClick(Game game, InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (ItemShopUtils.getBackItem().isSimilar(e.getCurrentItem())) {
            game.getNewItemShop(player).openCategoryInventory(player);
            return;
        }
        Map<String, ItemStack> resname = ItemShopUtils.getResourceList();
        if (ItemShopUtils.isShopItem(e.getCurrentItem())) {
            if (e.isShiftClick()) {
                int ba = 64 / e.getCurrentItem().getAmount();
                ItemShopUtils.buyItem(game, player, e.getCurrentItem(), resname, ba);
            } else {
                ItemShopUtils.buyItem(game, player, e.getCurrentItem(), resname, 1);
            }
        } else {
            if (Bukkit.getPluginManager().isPluginEnabled("BedwarsXP")) {
                new XPItemShop(game.getNewItemShop(player).getCategories(), game).handleInventoryClick(e, game, player);
            } else {
                new ItemShop(game.getNewItemShop(player).getCategories()).handleInventoryClick(e, game, player);
            }
        }
    }
}