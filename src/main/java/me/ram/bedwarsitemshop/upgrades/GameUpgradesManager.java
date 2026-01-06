package me.ram.bedwarsitemshop.upgrades;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GameUpgradesManager {

    private final Map<String, TeamUpgrades> arenas = new HashMap<>();

    public void addArena(String game, TeamUpgrades teamUpgrades) {
        arenas.put(game, teamUpgrades);
    }

    public void removeArena(String game) {
        arenas.remove(game);
    }

    public TeamUpgrades getArena(String game) {
        return arenas.get(game);
    }

}
