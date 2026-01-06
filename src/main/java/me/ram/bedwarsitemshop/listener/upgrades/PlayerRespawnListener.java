package me.ram.bedwarsitemshop.listener.upgrades;

import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.game.Game;
import me.ram.bedwarsitemshop.Main;
import me.ram.bedwarsitemshop.config.Config;
import me.ram.bedwarsitemshop.upgrades.TeamUpgrades;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onReSpawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Game game = BedwarsRel.getInstance().getGameManager().getGameOfPlayer(player);
        if (game == null) return;
        if (Config.isBedwarsScoreBoardAddonEnabled && me.ram.bedwarsscoreboardaddon.config.Config.respawn_enabled)
            return;
        TeamUpgrades teamUpgrades = Main.getInstance().getGameUpgradesManager().getArena(game.getName());
        if (teamUpgrades == null) return;
        teamUpgrades.givePlayerTeamUpgrade(player);
    }

}
