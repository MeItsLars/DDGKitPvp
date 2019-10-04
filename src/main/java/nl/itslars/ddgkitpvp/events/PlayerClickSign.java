package nl.itslars.ddgkitpvp.events;

import nl.itslars.ddgkitpvp.Main;
import nl.itslars.ddgkitpvp.database.Pair;
import nl.itslars.ddgkitpvp.kits.inventory.InventoryHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class performs actions when a player clicks a sign
 */
public class PlayerClickSign implements Listener {

    /**
     * This method is triggered whenever a player clicks a sign.
     * It checks whether the sign is a special [KitPvp] sign.
     * If it is, it checks whether the sign is a 'warp' or 'select kit' sign.
     * If the sign is a 'warp' sign, it will teleport the player to the arena spawn location with the name
     * that is provided on the sign. If the name is 'random', it will teleport the player to a random spawn.
     * If the sign is a 'select kit' sign, it will open the kit selection menu for the player
     * @param e The event
     */
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            if(block == null) return;

            if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN) {
                Sign sign = (Sign) block.getState();
                String[] lines = sign.getLines();

                if(lines.length >= 2 && lines[0].equals("§1[KitPvp]")) {
                    Player player = e.getPlayer();

                    if(lines.length >= 3 && lines[1].equalsIgnoreCase("warp")) {
                        String warpName = lines[2];
                        if(warpName.equalsIgnoreCase("random")) {
                            Pair<String, Location> target = Main.getLocationHandler().getRandomArenaLocation();
                            if(target == null) {
                                player.sendMessage("§cThere are no available spawns.");
                            } else {
                                player.teleport(target.getSecond());
                                player.sendMessage("§aYou were teleported to §e" + target.getFirst());
                            }
                        } else {
                            Location target = Main.getLocationHandler().getArenaLocation(warpName);
                            if(target == null) {
                                player.sendMessage("§cThere is no spawn with name §e" + warpName);
                            } else {
                                player.teleport(target);
                                player.sendMessage("§aYou were teleported to §e" + warpName);
                            }
                        }
                    } else if(lines[1].equalsIgnoreCase("select kit")) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                InventoryHandler.openKitSelectionInventory(player);
                            }
                        }.runTaskAsynchronously(Main.getInstance());
                    }
                }
            }
        }
    }
}
