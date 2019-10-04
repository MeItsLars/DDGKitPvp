package nl.itslars.ddgkitpvp.events;

import nl.itslars.ddgkitpvp.Main;
import nl.itslars.ddgkitpvp.database.KDHandler;
import nl.itslars.ddgkitpvp.database.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This class performs actions when a player joins the server
 */
public class PlayerJoin implements Listener {

    /**
     * This method is triggered whenever a player joins the server.
     * First, it retrieves the players kills an deaths from the KDHandler.
     * If the player is new on the server, the KDHandler will take appropriate actions,
     * the kills and deaths are then 0.
     * Then, it creates a scoreboard for this player, with the given kills and deaths.
     * @param e The event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Pair<Integer, Integer> playerKD = KDHandler.getPlayerKD(player.getUniqueId().toString());

        Main.getScoreboardHandler().createScoreboard(player, playerKD.getFirst(), playerKD.getSecond());
    }
}
