package nl.itslars.ddgkitpvp.events;

import nl.itslars.ddgkitpvp.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * This class performs actions when a player respawns after dying
 */
public class PlayerRespawn implements Listener {

    /**
     * This method is triggered whenever a player respawns.
     * It will set the player respawn location to the provided lobby location.
     * @param e The event
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(Main.getLocationHandler().getLobbyLocation());
    }
}
