package nl.itslars.ddgkitpvp;

import nl.itslars.ddgkitpvp.kitpvp.LocationConverter;
import nl.itslars.ddgkitpvp.kits.Kit;
import nl.itslars.ddgkitpvp.kits.inventory.InventoryHandler;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * Class handling the /kitpvp command
 */
public class KitPvpCommand implements CommandExecutor {

    /**
     * This method gets triggered whenever a player executes the /kitpvp command.
     * It can perform the following actions, based on the users arguments:
     * - List all relevant locations
     * - Change the lobby (respawn) location
     * - Add an arena spawn
     * - Remove an arena spawn
     * - Edit/create a kit
     * - Remove a kit
     * - Provide the user with information on how to create a custom sign
     * @param sender The instance that sent the command
     * @param cmd The command that was sent
     * @param label The command label
     * @param args The user provided arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("§cYou need to be a player to execute this command.");
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("kitpvp.admin")) {
            player.sendMessage("§cInsufficient permissions.");
            return false;
        }

        if(args.length == 0) {
            showHelpMenu(player);
        } else {
            if(args[0].equalsIgnoreCase("list")) {
                StringBuilder locationMessage = new StringBuilder("§7------- §eKitPvp Locations §7-------\n" +
                        "§eLobby: §7" + LocationConverter.locationToString(Main.getLocationHandler().getLobbyLocation()) + "\n" +
                        "§a§lArena spawn locations:");

                for(Map.Entry<String, Location> entry : Main.getLocationHandler().getArenaSpawnLocations().entrySet()) {
                    locationMessage
                            .append("\n§e")
                            .append(entry.getKey())
                            .append("§f: ")
                            .append(LocationConverter.locationToString(entry.getValue()));
                }

                player.sendMessage(locationMessage.toString());
            } else if(args[0].equalsIgnoreCase("setlobbyspawn")) {
                Main.getLocationHandler().setLobbyLocation(player.getLocation());
                player.sendMessage("§aLobby location set!");
            } else if(args[0].equalsIgnoreCase("addspawn")) {
                if(args.length == 2) {
                    Main.getLocationHandler().addArenaLocation(args[1], player.getLocation());
                    player.sendMessage("§aArena location added!");
                } else {
                    player.sendMessage("§c/kitpvp addspawn [name]");
                }
            } else if(args[0].equalsIgnoreCase("removespawn")) {
                if(args.length == 2) {
                    Main.getLocationHandler().removeArenaLocation(args[1]);
                    player.sendMessage("§aArena location removed!");
                } else {
                    player.sendMessage("§c/kitpvp removespawn [name]");
                }
            } else if(args[0].equalsIgnoreCase("editkit")) {
                if(args.length == 2) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            InventoryHandler.openKitEditor(player, args[1]);
                        }
                    }.runTaskAsynchronously(Main.getInstance());
                } else {
                    player.sendMessage("§c/kitpvp editkit [name]");
                }
            } else if(args[0].equalsIgnoreCase("removekit")) {
                if(args.length == 2) {
                    Kit kit = Main.getKitHandler().getKits().stream().filter(k -> k.getName().equals(args[1])).findFirst().orElse(null);
                    if(kit == null) {
                        player.sendMessage("§cThere is no kit with name " + args[1]);
                    } else {
                        Main.getKitHandler().removeKit(kit);
                        player.sendMessage("§aKit removed!");
                    }
                } else {
                    player.sendMessage("§c/kitpvp removekit [name]");
                }
            } else if(args[0].equalsIgnoreCase("sign")) {
                String signHelpMessage = "§7------- §eKitPvp Signs §7-------\n" +
                        "§7You can create signs like this:\n" +
                        "§e(1) §7On the first rule, put [KitPvp]\n" +
                        "§e(2) §7On the second rule, put 'select kit' or 'warp'\n" +
                        "§e(3) §7If you chose 'warp' in §e(2)§7, you have two options:\n" +
                        "§e    - §7'random', teleporting the player to a random spawn\n" +
                        "§e    - §7The spawn name, teleporting the player to that spawn.";

                player.sendMessage(signHelpMessage);
            } else {
                showHelpMenu(player);
            }
        }
        return true;
    }

    /**
     * Shows the help menu to a player. This is used when the user gave no, or wrong arguments.
     * @param player The player to which the help menu should be sent.
     */
    private void showHelpMenu(Player player) {
        String helpMessage = "§7------- §eKitPvp Commands §7-------\n" +
                "§e/kitpvp list §f- §7Shows information about the KitPvp spawns\n" +
                "§e/kitpvp setlobbyspawn §f- §7Sets the lobby spawn to your current location\n" +
                "§e/kitpvp addspawn [name] §f- §7Adds a KitPvp arena spawn at your current location\n" +
                "§e/kitpvp removespawn [name] §f- §7Removes a KitPvp arena spawn\n" +
                "§e/kitpvp editkit [name] §f- §7Opens the kit editor for the given name\n" +
                "§e/kitpvp removekit [name] §f- §7Removes the given kit\n" +
                "§e/kitpvp sign §f- §7Info about how to create signs";

        player.sendMessage(helpMessage);
    }
}
