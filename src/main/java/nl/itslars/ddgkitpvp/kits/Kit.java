package nl.itslars.ddgkitpvp.kits;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

/**
 * This class stores a kit. It contains all the data that is required for a kit.
 */
public class Kit {

    /**
     * The kit name
     */
    private String name;

    /**
     * The kit icon, this is displayed when users can select a kit from the menu.
     */
    private ItemStack icon;


    /**
     * The boots for this kit
     */
    private ItemStack boots;

    /**
     * The leggings for this kit
     */
    private ItemStack leggings;

    /**
     * The chestplate for this kit
     */
    private ItemStack chestplate;

    /**
     * The helemt for this kit
     */
    private ItemStack helmet;

    /**
     * This list contains all the additional inventory items that the kit has.
     */
    private List<ItemStack> inventoryItems;

    /**
     * Creates a new Kit object, containing the specified data
     * @param name The kit name
     * @param icon The kit icon
     * @param boots The kit boots
     * @param leggings The kit leggings
     * @param chestplate The kit chestplate
     * @param helmet The kit helmet
     * @param inventoryItems The kit's inventory items
     */
    public Kit(String name, ItemStack icon, ItemStack boots, ItemStack leggings, ItemStack chestplate, ItemStack helmet, List<ItemStack> inventoryItems) {
        this.name = name;
        this.icon = icon;
        this.boots = boots;
        this.leggings = leggings;
        this.chestplate = chestplate;
        this.helmet = helmet;
        this.inventoryItems = inventoryItems;
    }

    /**
     * Applies the kit to a player. This means that the player will equip the kit.
     * The player's armor items are changed to those given in the kit.
     * The player's inventory items are also changed to those given in the kit.
     * @param player
     */
    public void applyTo(Player player) {
        PlayerInventory inventory = player.getInventory();

        if(boots != null) inventory.setBoots(boots.clone());
        if(leggings != null) inventory.setLeggings(leggings.clone());
        if(chestplate != null) inventory.setChestplate(chestplate.clone());
        if(helmet != null) inventory.setHelmet(helmet.clone());

        inventory.addItem(inventoryItems.stream().map(ItemStack::clone).toArray(ItemStack[]::new));

        player.sendMessage("§aEquipped kit: §e" + name);
    }

    /**
     * Changes the kit icon
     * @param icon The new kit icon
     */
    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    /**
     * Changes the kit boots
     * @param boots The new kit boots
     */
    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    /**
     * Changes the kit leggings
     * @param leggings The new leggings
     */
    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    /**
     * Changes the kit chestplate
     * @param chestplate The new chestplate
     */
    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    /**
     * Changes the kit helmet
     * @param helmet The new helmet
     */
    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    /**
     * Changes the kit's inventory items
     * @param inventoryItems The new inventory items
     */
    public void setInventoryItems(List<ItemStack> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    /**
     * Retrieves the kit name
     * @return The kit name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the kit icon
     * @return The kit icon
     */
    public ItemStack getIcon() {
        return icon;
    }

    /**
     * Retrieves the kit boots
     * @return The kit boots
     */
    public ItemStack getBoots() {
        return boots;
    }

    /**
     * Retrieves the kit leggings
     * @return The kit leggings
     */
    public ItemStack getLeggings() {
        return leggings;
    }

    /**
     * Retrieves the kit chestplate
     * @return The kit chestplate
     */
    public ItemStack getChestplate() {
        return chestplate;
    }

    /**
     * Retrieves the kit helmet
     * @return The kit helmet
     */
    public ItemStack getHelmet() {
        return helmet;
    }

    /**
     * Retrieves the kit's inventory items
     * @return The kit's inventory items
     */
    public List<ItemStack> getInventoryItems() {
        return inventoryItems;
    }
}
