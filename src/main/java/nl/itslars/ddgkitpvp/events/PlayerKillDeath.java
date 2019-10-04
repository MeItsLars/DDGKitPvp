package nl.itslars.ddgkitpvp.events;

import nl.itslars.ddgkitpvp.Main;
import nl.itslars.ddgkitpvp.database.KDHandler;
import nl.itslars.ddgkitpvp.database.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * This class performs actions when a player dies
 */
public class PlayerKillDeath implements Listener {

    /**
     * This method is triggered whenever a player dies.
     * The amount of deaths for the given player will be incremented by one. This will immediately be shown
     * on the players scoreboard.
     * If the player was killed by another player, the killers kills will be incremented by one.
     * This will immediately be shown on the killers scoreboard.
     * @param e The event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        Pair<Integer, Integer> playerKDValues = KDHandler.addDeath(player.getUniqueId().toString());
        Main.getScoreboardHandler().setKDValues(player, playerKDValues.getFirst(), playerKDValues.getSecond());

        Player killer = player.getKiller();
        if(killer != null && killer.isOnline()) {
            Pair<Integer, Integer> killerKDValues = KDHandler.addKill(killer.getUniqueId().toString());
            Main.getScoreboardHandler().setKDValues(killer, killerKDValues.getFirst(), killerKDValues.getSecond());
        }
    }
}
