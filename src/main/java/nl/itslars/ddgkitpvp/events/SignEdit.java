package nl.itslars.ddgkitpvp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * This class performs actions when a player places a sign
 */
public class SignEdit implements Listener {

    /**
     * This method is triggered when a player clicks the 'done' button after editing a sign.
     * It will check if the sign is a special [KitPvp] sign. If it is, it will mark the header with a blue color.
     * @param e
     */
    @EventHandler
    public void onSignEdit(SignChangeEvent e) {
        Player player = e.getPlayer();

        if(player.hasPermission("kitpvp.admin")) {
            String[] lines = e.getLines();

            if(lines.length >= 2 && lines[0].equalsIgnoreCase("[KitPvp]")) {
                if((lines.length >= 3 && lines[1].equals("warp")) || lines[1].equalsIgnoreCase("select kit")) {
                    e.setLine(0, "§1[KitPvp]");
                }
            }
        }
    }
}
