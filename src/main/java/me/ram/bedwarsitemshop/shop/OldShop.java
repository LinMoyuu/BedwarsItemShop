package me.ram.bedwarsitemshop.shop;

import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.game.Game;
import io.github.bedwarsrel.utils.SoundMachine;
import me.ram.bedwarsitemshop.utils.ItemShopUtils;
import me.ram.bedwarsitemshop.utils.ItemUtils;
import me.ram.bedwarsitemshop.xpshop.ItemShop;
import me.ram.bedwarsitemshop.xpshop.XPItemShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class OldShop implements Shop {

    public void onOpen(Game game, Player player, Inventory shop) {
        List<ItemStack> shops = ItemShopUtils.getShops(shop);
        List<ItemStack> shop_items = ItemShopUtils.getShopItems(shop);
        int line;
        if (shop_items.isEmpty()) {
            line = shops.size() / 5;
            if (line * 5 < shops.size()) {
                line++;
            }
            line--;
        } else {
            line = shop_items.size() / 7;
            if (line * 7 < shop_items.size()) {
                line++;
            }
        }
        Inventory inventory = Bukkit.createInventory(null, line * 9 + 27, BedwarsRel._l(player, "ingame.shop.name") + "§n§e§w");
        int slot = 11;
        if (shop_items.isEmpty()) {
            for (ItemStack item : shops) {
                if (slot == 16) {
                    slot = 20;
                } else if (slot == 25) {
                    slot = 29;
                } else if (slot == 34) {
                    slot = 38;
                } else if (slot == 43) {
                    slot = 47;
                } else if (slot == 52) {
                    break;
                }
                inventory.setItem(slot, item);
                slot++;
            }
        } else {
            slot = 10;
            for (ItemStack shopItem : shop_items) {
                if (slot == 17 || slot == 18) {
                    slot = 19;
                } else if (slot == 26 || slot == 27) {
                    slot = 28;
                } else if (slot == 35 || slot == 36) {
                    slot = 37;
                } else if (slot == 44 || slot == 45) {
                    slot = 46;
                } else if (slot == 53) {
                    break;
                }
                inventory.setItem(slot, shopItem);
                slot++;
            }
            inventory.setItem(inventory.getSize() - 5, ItemShopUtils.getBackItem());
        }
        player.openInventory(inventory);
    }

    public void onClick(Game game, InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getCurrentItem().isSimilar(ItemShopUtils.getFrameItem(7)) && e.getCurrentItem().isSimilar(ItemShopUtils.getFrameItem(5))) {
            return;
        }

        if (ItemShopUtils.getBackItem().isSimilar(e.getCurrentItem())) {
            game.getNewItemShop(player).openCategoryInventory(player);
            return;
        }
        Map<String, ItemStack> resname = ItemShopUtils.getResourceList();
        ItemStack currentItem = e.getCurrentItem();
        if (!ItemShopUtils.isShopItem(e.getCurrentItem())) {
            if (ItemShopUtils.isXpMode(game)) {
                new XPItemShop(game.getNewItemShop(player).getCategories(), game).handleInventoryClick(e, game, player);
            } else {
                new ItemShop(game.getNewItemShop(player).getCategories()).handleInventoryClick(e, game, player);
            }
            return;
        }

        if (ItemUtils.isUpgradeItem(currentItem) && !ItemShopUtils.isXpMode(game)) {
            ItemShopUtils.buyUpgrade(game, player, currentItem, resname);
            return;
        }

        if (e.isShiftClick()) {
            int ba = 64 / currentItem.getAmount();
            if (game.getPlayerSettings(player).oneStackPerShift()) {
                ItemShopUtils.buyItem(game, player, currentItem, resname, ba);
            } else {
                while (ItemShopUtils.buyItem(game, player, currentItem, resname, ba));
            }
        } else {
            ItemShopUtils.buyItem(game, player, currentItem, resname, 1);
        }
        player.playSound(player.getLocation(), SoundMachine.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1.0f, 1.0f);
    }
}
