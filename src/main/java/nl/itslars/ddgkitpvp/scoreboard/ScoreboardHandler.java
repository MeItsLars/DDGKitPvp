package nl.itslars.ddgkitpvp.scoreboard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the scoreboard for each online player.
 * This handler shows kills and deaths, as well as the K/D ratio for each online player.
 */
public class ScoreboardHandler {

    /**
     * Maps the player to their associated scoreboard.
     */
    private Map<Player, EasyScoreboard> scoreboardMap = new HashMap<>();

    /**
     * Creates a scoreboard for the given player. After creation, it also shows the scoreboard to the player.
     * @param player The player to which the scoreboard belongs
     * @param kills The amount of kills the player has
     * @param deaths The amount of deaths the player has
     */
    public void createScoreboard(Player player, int kills, int deaths) {
        EasyScoreboard scoreboard = new EasyScoreboard(player, "      §cDDG §f- §7KitPVP      ");

        scoreboard.setLineText(5, "");
        scoreboard.setLineText(4, "§eKills: §f" + kills);
        scoreboard.setLineText(3, "§eDeaths: §f" + deaths);
        scoreboard.setLineText(2, "§eK/D: §f" + computeKD(kills, deaths));
        scoreboard.setLineText(1, "");
        scoreboard.setLineText(0, "§cplay.dusdavidgames.nl");

        scoreboard.enable();

        scoreboardMap.put(player, scoreboard);
    }

    /**
     * Computes the K/D ratio, given the kills and deaths. If the amount of deaths is 0, the result will also be 0.
     * @param kills The amount of kills
     * @param deaths The amount of deaths
     * @return The formatted string, showing the K/D ratio.
     */
    private String computeKD(double kills, double deaths) {
        return deaths == 0 ? "0" : String.format("%.2f", kills / deaths);
    }

    /**
     * Removes the player and scoreboard from the {@link #scoreboardMap}
     * @param player The player that has to be removed from the map
     */
    public void destroyScoreboard(Player player) {
        scoreboardMap.remove(player);
    }

    /**
     * Sets the kills, deaths and K/D value in a player's scoreboard.
     * The K/D ratio is computed using the {@link #computeKD(double, double)} method.
     * @param player The player to which the kills and deaths belong
     * @param kills The amount of kills
     * @param deaths The amount of deaths
     */
    public void setKDValues(Player player, int kills, int deaths) {
        EasyScoreboard scoreboard = scoreboardMap.get(player);
        scoreboard.setLineText(4, "§eKills: §f" + kills);
        scoreboard.setLineText(3, "§eDeaths: §f" + deaths);
        scoreboard.setLineText(2, "§eK/D: §f" + computeKD(kills, deaths));
    }
}
