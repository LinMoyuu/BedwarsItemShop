package me.ram.bedwarsitemshop.shop;

import io.github.bedwarsrel.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface Shop {

    void onOpen(Game game, Player player, Inventory shop);

    void onClick(Game game, InventoryClickEvent e);
}
