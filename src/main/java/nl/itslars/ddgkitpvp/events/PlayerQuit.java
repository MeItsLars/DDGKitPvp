package nl.itslars.ddgkitpvp.events;

import nl.itslars.ddgkitpvp.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This class performs actions when a player quits the server
 */
public class PlayerQuit implements Listener {

    /**
     * This method is triggered whenever a player leaves the server.
     * It will shutdown the scoreboard for the given player.
     * This makes sure the plugin doesn't use huge amounts of memory.
     * @param e The event
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        Main.getScoreboardHandler().destroyScoreboard(player);
    }
}
