package me.ram.bedwarsitemshop.listener.upgrades;

import io.github.bedwarsrel.events.BedwarsGameEndEvent;
import io.github.bedwarsrel.events.BedwarsGameStartEvent;
import io.github.bedwarsrel.game.Game;
import me.ram.bedwarsitemshop.Main;
import me.ram.bedwarsitemshop.upgrades.TeamUpgrades;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GameListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onStart(BedwarsGameStartEvent e) {
        if (e.isCancelled()) return;
        Game game = e.getGame();
        new TeamUpgrades(game);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnd(BedwarsGameEndEvent e) {
        String gameName = e.getGame().getName();
        if (Main.getInstance().getGameUpgradesManager().getArenas().containsKey(gameName)) {
            Main.getInstance().getGameUpgradesManager().getArenas().get(gameName).onEnd();
        }
        Main.getInstance().getGameUpgradesManager().removeArena(gameName);
    }

}
