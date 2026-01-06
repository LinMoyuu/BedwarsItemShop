package me.ram.bedwarsitemshop.upgrades;

import io.github.bedwarsrel.game.Game;
import io.github.bedwarsrel.game.Team;
import lombok.Getter;
import me.ram.bedwarsitemshop.Main;
import me.ram.bedwarsitemshop.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class TeamUpgrades implements Listener {

    @Getter
    private final Game game;
    @Getter
    private final HashMap<Team, Integer> teamSharpnessLevel;
    @Getter
    private final HashMap<Team, Integer> teamLeggingsProtectionLevel;
    @Getter
    private final HashMap<Team, Integer> teamBootsProtectionLevel;

    public TeamUpgrades(Game game) {
        this.game = game;
        Main.getInstance().getGameUpgradesManager().addArena(game.getName(), this);
        teamSharpnessLevel = new HashMap<>();
        teamLeggingsProtectionLevel = new HashMap<>();
        teamBootsProtectionLevel = new HashMap<>();
    }

    public void givePlayerTeamUpgrade(Player player) {
        Team playerTeam = game.getPlayerTeam(player);
        if (playerTeam == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                int teamSharpnessLvl = getTeamSharpnessLevel().getOrDefault(playerTeam, 0);
                if (teamSharpnessLvl != 0) {
                    ItemUtils.givePlayerSharpness(player, teamSharpnessLvl);
                }

                int teamLeggingsLvl = getTeamLeggingsProtectionLevel().getOrDefault(playerTeam, 0);
                if (teamLeggingsLvl != 0) {
                    ItemUtils.giveLeggingsProtection(player, teamLeggingsLvl);
                }

                int teamBootsLvl = getTeamBootsProtectionLevel().getOrDefault(playerTeam, 0);
                if (teamBootsLvl != 0) {
                    ItemUtils.giveBootsProtection(player, teamBootsLvl);
                }
            }
        }.runTaskLater(me.ram.bedwarsscoreboardaddon.Main.getInstance(), 5L);
    }

}
