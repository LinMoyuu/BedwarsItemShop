package me.ram.bedwarsitemshop.listener.upgrades;

import io.github.bedwarsrel.game.Game;
import me.ram.bedwarsitemshop.Main;
import me.ram.bedwarsitemshop.upgrades.TeamUpgrades;
import me.ram.bedwarsscoreboardaddon.events.BoardAddonPlayerRespawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerBoardRespawnListener implements Listener {

    @EventHandler
    public void onBoardReSpawn(BoardAddonPlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Game game = event.getGame();
        if (game == null) return;
        TeamUpgrades teamUpgrades = Main.getInstance().getGameUpgradesManager().getArena(game.getName());
        if (teamUpgrades == null) return;
        teamUpgrades.givePlayerTeamUpgrade(player);
    }

}
