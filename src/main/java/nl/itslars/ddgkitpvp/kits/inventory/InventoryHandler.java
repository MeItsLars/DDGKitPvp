package nl.itslars.ddgkitpvp.kits.inventory;

import nl.itslars.ddgkitpvp.Main;
import nl.itslars.ddgkitpvp.kits.Kit;
import nl.itslars.ddgkitpvp.kits.inventory.callback.Confirmation;
import nl.itslars.ddgkitpvp.kits.inventory.callback.ConfirmationCallback;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * This class handles custom inventories/GUI's.
 * It uses the Confirmation and ConfirmationCallback classes to create user interfaces.
 */
public class InventoryHandler {

    /**
     * Opens the kit selection inventory, for the given player.
     * It also registers a callback. If the player clicks on a kit, this kit will be given to the player,
     * and the inventory will be closed.
     * @param player The player to which the inventory is sent.
     */
    public static void openKitSelectionInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§cChoose your kit:");

        Map<ItemStack, Kit> kits = new HashMap<>();

        int index = 0;
        for (Kit kit : Main.getKitHandler().getKits()) {

            ItemStack icon = kit.getIcon();
            ItemMeta im = icon.getItemMeta();
            if (im != null) {
                im.setDisplayName("§c" + kit.getName());
                im.setLore(Collections.singletonList("§7Click to select this kit!"));
                icon.setItemMeta(im);
            }

            inventory.setItem(index, icon);
            kits.put(icon, kit);

            index++;
        }

        new Confirmation<>(player, new ConfirmationCallback<Kit>() {
            @Override
            public void callback(Kit response) {
                if (response == null) return;

                response.applyTo(player);
                player.closeInventory();
            }
        }, inventory, kits, false);
    }

    /**
     * Opens the kit editor GUI, for the given player and kit.
     * If there exists a kit with the given name, it will load this kit, and the user can edit it.
     * If not, it will show a new menu, where the user can create new kits.
     * The user can set the kit icon, armor, and items. When the user is finished,
     * he can click the 'confirm' or 'cancel' buttons. If the action is confirmed,
     * the kit will be saved, and will be available to other players.
     * If the action is cancelled, nothing happens.
     * In both cases the inventory is closed.
     * @param player The player to which the inventory is sent
     * @param kitName The name of the kit to be edited
     */
    public static void openKitEditor(Player player, String kitName) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§cKit Editor: §4" + kitName);

        ItemStack glass = getItemStack(Material.GRAY_STAINED_GLASS_PANE, " ");
        ItemStack confirm = getItemStack(Material.LIME_WOOL, "§aConfirm");
        ItemStack cancel = getItemStack(Material.RED_WOOL, "§cCancel");

        ItemStack icon = getItemStack(Material.DIAMOND, "§aKit icon");

        ItemStack boots = getItemStack(Material.LEATHER_BOOTS, "§aKit boots");
        ItemStack leggings = getItemStack(Material.LEATHER_LEGGINGS, "§aKit leggings");
        ItemStack chestplate = getItemStack(Material.LEATHER_CHESTPLATE, "§aKit chestplate");
        ItemStack helmet = getItemStack(Material.LEATHER_HELMET, "§aKit helmet");

        Kit kit = Main.getKitHandler().getKits().stream()
                .filter(k -> k.getName().equals(kitName))
                .findFirst()
                .orElse(null);

        if (kit != null) {
            icon = kit.getIcon();

            boots = kit.getBoots();
            leggings = kit.getLeggings();
            chestplate = kit.getChestplate();
            helmet = kit.getHelmet();

            int index = 18;
            for (ItemStack is : kit.getInventoryItems()) {
                inventory.setItem(index, is);
                index++;
            }
        }

        inventory.setItem(0, icon);
        inventory.setItem(1, glass);
        inventory.setItem(2, boots);
        inventory.setItem(3, leggings);
        inventory.setItem(4, chestplate);
        inventory.setItem(5, helmet);
        inventory.setItem(6, glass);
        inventory.setItem(7, confirm);
        inventory.setItem(8, cancel);

        for (int i = 9; i < 18; i++) inventory.setItem(i, glass);

        Map<ItemStack, String> callbacks = new HashMap<>();
        callbacks.put(confirm, "CONFIRM");
        callbacks.put(cancel, "CANCEL");

        int[] editableSlots = Stream.concat(
                Arrays.stream(new Integer[] {0, 2, 3, 4, 5}),
                Arrays.stream(IntStream
                        .range(18, 54)
                        .boxed()
                        .toArray(Integer[]::new)))
                .mapToInt(Integer::intValue).toArray();

        new Confirmation<>(player, new ConfirmationCallback<String>() {
            @Override
            public void callback(String response) {
                if (response == null || response.equalsIgnoreCase("CANCEL")) {
                    player.sendMessage("§cKit editing cancelled.");
                    player.closeInventory();
                } else if (response.equalsIgnoreCase("CONFIRM")) {
                    ItemStack newIcon = inventory.getItem(0);

                    ItemStack newBoots = inventory.getItem(2);
                    ItemStack newLeggings = inventory.getItem(3);
                    ItemStack newChestplate = inventory.getItem(4);
                    ItemStack newHelmet = inventory.getItem(5);

                    List<ItemStack> newInventoryItems = new ArrayList<>();
                    for (int i = 18; i < 54; i++) {
                        ItemStack inventoryItem = inventory.getItem(i);
                        if (inventoryItem != null && inventoryItem.getType() != Material.AIR) {
                            newInventoryItems.add(inventoryItem);
                        }
                    }

                    if (newIcon == null || newIcon.getType() == Material.AIR) {
                        player.sendMessage("§cA kit needs an icon!");
                        player.closeInventory();
                        return;
                    }

                    if (kit == null) {
                        Kit kit = new Kit(kitName, newIcon, newBoots, newLeggings, newChestplate, newHelmet, newInventoryItems);
                        Main.getKitHandler().addKit(kit);
                    } else {

                        kit.setIcon(newIcon);

                        kit.setBoots(newBoots);
                        kit.setLeggings(newLeggings);
                        kit.setChestplate(newChestplate);
                        kit.setHelmet(newHelmet);

                        kit.setInventoryItems(newInventoryItems);
                    }

                    player.closeInventory();
                    player.sendMessage("§aKit saved!");
                }
            }
        }, inventory, callbacks, true, editableSlots);
    }

    /**
     * Creates an ItemStack object from given parameters.
     * This method prevents a lot of duplicate code when creating ItemStacks.
     * @param material The ItemStack material
     * @param displayName The ItemStack displayed name
     * @param lore The ItemStack lore
     * @return The actual ItemStack
     */
    private static ItemStack getItemStack(Material material, String displayName, String... lore) {
        ItemStack is = new ItemStack(material, 1);
        ItemMeta im = is.getItemMeta();
        if (im == null) return is;

        im.setDisplayName("§r" + displayName);
        if (lore != null) im.setLore(Arrays.asList(lore));

        is.setItemMeta(im);
        return is;
    }
}
