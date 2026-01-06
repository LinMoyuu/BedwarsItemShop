package me.ram.bedwarsitemshop;

import lombok.Getter;
import me.ram.bedwarsitemshop.commands.Commands;
import me.ram.bedwarsitemshop.config.Config;
import me.ram.bedwarsitemshop.config.LocaleConfig;
import me.ram.bedwarsitemshop.listener.ShopListener;
import me.ram.bedwarsitemshop.listener.upgrades.GameListener;
import me.ram.bedwarsitemshop.listener.upgrades.PlayerBoardRespawnListener;
import me.ram.bedwarsitemshop.listener.upgrades.PlayerRespawnListener;
import me.ram.bedwarsitemshop.upgrades.GameUpgradesManager;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    @Getter
    private LocaleConfig localeConfig;
    @Getter
    private GameUpgradesManager gameUpgradesManager;

    public void onEnable() {
        if (!getDescription().getName().equals("BedwarsItemShop") || !getDescription().getAuthors().contains("Ram") || !getDescription().getAuthors().contains("YukiEnd")) {
            new Exception("Please don't edit plugin.yml!").printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        instance = this;
        gameUpgradesManager = new GameUpgradesManager();
        localeConfig = new LocaleConfig();
        localeConfig.loadLocaleConfig();
        Bukkit.getConsoleSender().sendMessage("§f=========================================");
        Bukkit.getConsoleSender().sendMessage("§7");
        Bukkit.getConsoleSender().sendMessage("             §bBedwarsItemShop");
        Bukkit.getConsoleSender().sendMessage("§7");
        Bukkit.getConsoleSender().sendMessage(" §a" + localeConfig.getLanguage("version") + ": §a" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§7");
        Bukkit.getConsoleSender().sendMessage(" §a" + localeConfig.getLanguage("author") + ": §aRam" + ", §eModified By YukiEnd");
        Bukkit.getConsoleSender().sendMessage("§7");
        Bukkit.getConsoleSender().sendMessage("§f=========================================");
        Config.loadConfig();
        Bukkit.getPluginCommand("bedwarsitemshop").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new ShopListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        if (Config.isBedwarsScoreBoardAddonEnabled) {
            Bukkit.getPluginManager().registerEvents(new PlayerBoardRespawnListener(), this);
        }
        try {
            Metrics metrics = new Metrics(this, 12105);
            metrics.addCustomChart(new SimplePie("language", () -> localeConfig.getPluginLocale().getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
