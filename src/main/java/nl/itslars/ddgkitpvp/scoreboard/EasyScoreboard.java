package nl.itslars.ddgkitpvp.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

public class EasyScoreboard {

    /**
     * A string containing entries for new teams.
     * Each time, a new character from this list is picked, thus making sure each team has a unique new name.
     */
    private final String ENTRY_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * The player to which the scoreboard belongs
     */
    private Player player;

    /**
     * The associated Bukkit scoreboard
     */
    private Scoreboard scoreboard;

    /**
     * The associated Bukkit objective
     */
    private Objective objective;

    /**
     * Map for storing scores, and mapping them to a team.
     * This can be used to change a line text. If a score is already in the map, the connected team's prefix is changed.
     * Otherwise, a new team is created, and added to the map.
     */
    private Map<Integer, Team> lineValues = new HashMap<>();

    /**
     * Generates a new scoreboard for the given player, with the given title.
     * @param player The scoreboard owner
     * @param title The scoreboard title
     */
    public EasyScoreboard(Player player, String title) {
        this.player = player;

        // Bukkit.getScoreboadManager() can produce a NPE. We prevent a console error by checking this.
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        // Registering the new scoreboard and objective
        this.scoreboard = manager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("DDGKitPvp", "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Sends the scoreboard to the player.
     * This is especially useful if a scoreboard has to been initialized with lines, before sending it to a player.
     */
    public void enable() {
        player.setScoreboard(scoreboard);
    }

    /**
     * Sets the text in the scoreboard, on the given line.
     * @param lineScore The line at which the text is displayed
     * @param text The text to be displayed
     */
    public void setLineText(int lineScore, String text) {
        Team lineTeam;
        if (lineValues.containsKey(lineScore)) {
            lineTeam = lineValues.get(lineScore);
        } else {
            String uniqueEntry = "§" + ENTRY_CHARACTERS.charAt(lineValues.size());
            lineTeam = scoreboard.registerNewTeam(uniqueEntry);
            lineTeam.addEntry(uniqueEntry);
            lineValues.put(lineScore, lineTeam);
            objective.getScore(lineTeam.getEntries().iterator().next()).setScore(lineScore);
        }

        lineTeam.setPrefix(text);
    }
}
