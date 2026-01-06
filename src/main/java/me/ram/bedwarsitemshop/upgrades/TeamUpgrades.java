package me.ram.bedwarsitemshop.upgrades;

import io.github.bedwarsrel.game.Game;
import io.github.bedwarsrel.game.Team;
import lombok.Getter;
import me.ram.bedwarsitemshop.Main;
import me.ram.bedwarsitemshop.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TeamUpgrades implements Listener {

    private final Game game;
    private Map<String, Integer> teamSharpnessLevel;
    private Map<String, Integer> teamLeggingsProtectionLevel;
    private Map<String, Integer> teamBootsProtectionLevel;

    /**
     * 升级团队增强功能
     *
     * @param player    玩家
     * @param itemStack 要升级的物品
     * @return 如果升级成功返回true，否则返回false
     */
    public boolean upgradeTeamEnhanced(Player player, ItemStack itemStack) {
        if (game == null) return false;

        Team playerTeam = game.getPlayerTeam(player);
        if (playerTeam == null) return false;

        ItemStack stack = itemStack.clone();
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return false;

        String displayName = ChatColor.stripColor(meta.getDisplayName());
        if (displayName == null) return false;

        return processUpgrade(displayName, playerTeam);
    }

    public TeamUpgrades(Game game) {
        this.game = game;
        Main.getInstance().getGameUpgradesManager().addArena(game.getName(), this);
        teamSharpnessLevel = new HashMap<>();
        teamLeggingsProtectionLevel = new HashMap<>();
        teamBootsProtectionLevel = new HashMap<>();
    }

    public void onEnd() {
        teamSharpnessLevel = null;
        teamLeggingsProtectionLevel = null;
        teamBootsProtectionLevel = null;
    }

    public void givePlayerTeamUpgrade(Player player) {
        Team playerTeam = game.getPlayerTeam(player);
        if (playerTeam == null) return;
        String teamName = playerTeam.getName();

        new BukkitRunnable() {
            @Override
            public void run() {
                int teamSharpnessLvl = getTeamSharpnessLevel().getOrDefault(teamName, 0);
                if (teamSharpnessLvl != 0) {
                    ItemUtils.givePlayerSharpness(player, teamSharpnessLvl);
                }

                int teamLeggingsLvl = getTeamLeggingsProtectionLevel().getOrDefault(teamName, 0);
                if (teamLeggingsLvl != 0) {
                    ItemUtils.giveLeggingsProtection(player, teamLeggingsLvl);
                }

                int teamBootsLvl = getTeamBootsProtectionLevel().getOrDefault(teamName, 0);
                if (teamBootsLvl != 0) {
                    ItemUtils.giveBootsProtection(player, teamBootsLvl);
                }
            }
        }.runTaskLater(me.ram.bedwarsscoreboardaddon.Main.getInstance(), 1L);
    }

    /**
     * 处理升级逻辑
     *
     * @param displayName 物品显示名称
     * @param team        玩家队伍
     * @return 升级是否成功
     */
    private boolean processUpgrade(String displayName, Team team) {
        // 解析升级类型和等级
        UpgradeInfo upgradeInfo = parseUpgradeInfo(displayName);
        if (upgradeInfo == null) {
            return false; // 无效的升级物品
        }

        // 根据升级类型获取当前等级
        String teamName = team.getName();
        Integer currentLevel = getCurrentLevelForUpgradeType(upgradeInfo.upgradeType, teamName);
        if (currentLevel != null && currentLevel >= upgradeInfo.level) {
            return false; // 已达到或超过目标等级
        }

        // 执行升级
        setLevelForUpgradeType(upgradeInfo.upgradeType, teamName, upgradeInfo.level);
        return true;
    }

    /**
     * 获取特定升级类型的当前等级
     *
     * @param upgradeType 升级类型
     * @param teamName 队伍名称
     * @return 当前等级
     */
    private Integer getCurrentLevelForUpgradeType(UpgradeType upgradeType, String teamName) {
        switch (upgradeType) {
            case SHARPNESS:
                return teamSharpnessLevel.get(teamName);
            case LEGGINGS_PROTECTION:
                return teamLeggingsProtectionLevel.get(teamName);
            case BOOTS_PROTECTION:
                return teamBootsProtectionLevel.get(teamName);
            default:
                return 0;
        }
    }

    /**
     * 设置特定升级类型的等级
     *
     * @param upgradeType 升级类型
     * @param teamName 队伍名称
     * @param level       等级
     */
    private void setLevelForUpgradeType(UpgradeType upgradeType, String teamName, Integer level) {
        switch (upgradeType) {
            case SHARPNESS:
                teamSharpnessLevel.put(teamName, level);
                break;
            case LEGGINGS_PROTECTION:
                teamLeggingsProtectionLevel.put(teamName, level);
                break;
            case BOOTS_PROTECTION:
                teamBootsProtectionLevel.put(teamName, level);
                break;
        }
        Team team = game.getTeam(teamName);
        for (Player player : team.getPlayers()) {
            givePlayerTeamUpgrade(player);
        }
    }

    /**
     * 解析升级信息
     *
     * @param displayName 显示名称
     * @return 升级信息，如果无法解析则返回null
     */
    private UpgradeInfo parseUpgradeInfo(String displayName) {
        // 尝试匹配升级类型和等级
        for (UpgradeType upgradeType : UpgradeType.values()) {
            if (displayName.startsWith(upgradeType.getDisplayNamePrefix())) {
                int level = parseLevelFromDisplayName(displayName);
                if (level > 0) {
                    return new UpgradeInfo(upgradeType, level);
                }
            }
        }
        return null;
    }

    /**
     * 从显示名称中解析等级
     *
     * @param displayName 显示名称
     * @return 解析到的等级，如果无法解析则返回-1
     */
    private int parseLevelFromDisplayName(String displayName) {
        // 检查是否包含"I"或"II"
        if (displayName.endsWith("I")) {
            if (displayName.endsWith("II")) {
                return 2; // 包含"II"，返回等级2
            } else {
                return 1; // 只包含"I"，返回等级1
            }
        }
        return -1; // 无法解析等级
    }

    // 定义升级类型枚举
    @Getter
    public enum UpgradeType {
        SHARPNESS("武器附魔"),
        LEGGINGS_PROTECTION("护腿保护"),
        BOOTS_PROTECTION("靴子保护");

        private final String displayNamePrefix;

        UpgradeType(String displayNamePrefix) {
            this.displayNamePrefix = displayNamePrefix;
        }

    }

    /**
     * 升级信息类
     */
    private static class UpgradeInfo {
        final UpgradeType upgradeType;
        final int level;

        UpgradeInfo(UpgradeType upgradeType, int level) {
            this.upgradeType = upgradeType;
            this.level = level;
        }
    }
}
