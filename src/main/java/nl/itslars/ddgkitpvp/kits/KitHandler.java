package nl.itslars.ddgkitpvp.kits;

import nl.itslars.ddgkitpvp.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all kits.
 * It stores the current kits in the memory, and manages the loading/saving of the kits
 * when the server is enabled/disabled
 */
public class KitHandler {

    /**
     * List containing all the kits that have been provided by the user
     */
    private List<Kit> kits = new ArrayList<>();

    /**
     * Loads the kits from the config.yml file, and stores them in the kit list
     */
    public void loadKits() {
        FileConfiguration config = Main.getInstance().getConfig();

        ConfigurationSection section = config.getConfigurationSection("kits");
        if(section != null) {
            for(String kitName : section.getKeys(false)) {
                String location = "kits." + kitName;

                ItemStack icon = config.getItemStack(location + ".icon");

                ItemStack boots = config.getItemStack(location + ".boots");
                ItemStack leggings = config.getItemStack(location + ".leggings");
                ItemStack chestplate = config.getItemStack(location + ".chestplate");
                ItemStack helmet = config.getItemStack(location + ".helmet");

                List<ItemStack> inventoryItems = (List<ItemStack>) config.getList(location + ".inventory_items");

                kits.add(new Kit(kitName, icon, boots, leggings, chestplate, helmet, inventoryItems));
            }
        }
    }

    /**
     * Saves the kits in the list to the config.yml file.
     */
    public void saveKits() {
        FileConfiguration config = Main.getInstance().getConfig();

        // First, we clear the previously stored kits:
        config.set("kits", null);
        Main.getInstance().saveConfig();

        // Then, we save the current kits:
        for(Kit kit : kits) {
            String location = "kits." + kit.getName();

            config.set(location + ".icon", kit.getIcon());

            config.set(location + ".boots", kit.getBoots());
            config.set(location + ".leggings", kit.getLeggings());
            config.set(location + ".chestplate", kit.getChestplate());
            config.set(location + ".helmet", kit.getHelmet());

            config.set(location + ".inventory_items", kit.getInventoryItems());
        }

        Main.getInstance().saveConfig();
    }

    /**
     * Adds a new kit to the list
     * @param kit The kit to be added
     */
    public void addKit(Kit kit) {
        kits.add(kit);
    }

    /**
     * Removes a kit from the list
     * @param kit The kit to be removed
     */
    public void removeKit(Kit kit) {
        kits.remove(kit);
    }

    /**
     * Retrieves the list of kits
     * @return The kits
     */
    public List<Kit> getKits() {
        return kits;
    }
}
