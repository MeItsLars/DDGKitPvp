package nl.itslars.ddgkitpvp.kits.inventory.callback;

import nl.itslars.ddgkitpvp.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Map;

/**
 * Class that handles interactive user GUI's. It's a pretty useful system,
 * that prevents huge event classes for the InventoryClickEvent.
 * Each ItemStack is mapped to an object. This object can be used after the callback.
 * @param <T> The type of the object that is connected to each ItemStack
 */
public class Confirmation<T> implements Listener {

    /**
     * Player for which this confirmation is active
     */
    private Player player;

    /**
     * The callback, containing the method that should be called after a player clicks a certain item
     */
    private ConfirmationCallback<T> confirmationCallback;

    /**
     * Inventory, containing all the items provided.
     */
    private Inventory targetInventory;

    /**
     * Map, containing ItemStacks. Each ItemStack is mapped to an object that can be used in the callback.
     */
    private Map<ItemStack, T> items;

    /**
     * Whether the player is allowed to click in their own inventory while in a custom inventory
     */
    private boolean allowPlayerInventoryClicks;

    /**
     * All slots in the given {@link #targetInventory} that the player is allowed to edit
     */
    private int[] editableSlots;

    /**
     * Creates a Confirmation. This is a way of easily mapping user GUI clicks to specified actions.
     * @param player The Player
     * @param confirmationCallback The callback
     * @param targetInventory The given inventory
     * @param items The mapped items
     * @param allowPlayerInventoryClicks Whether a player can click in their own inventory
     * @param editableSlots All slots in the target inventory that are editable
     */
    public Confirmation(Player player, ConfirmationCallback<T> confirmationCallback, Inventory targetInventory,
                        Map<ItemStack, T> items, boolean allowPlayerInventoryClicks, int... editableSlots) {
        this.player = player;
        this.confirmationCallback = confirmationCallback;
        this.targetInventory = targetInventory;
        this.items = items;
        this.allowPlayerInventoryClicks = allowPlayerInventoryClicks;
        this.editableSlots = editableSlots;

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());

        // This task needs to be sync, and we have to force this since we are in an asynchronous environment
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(targetInventory);
            }
        }.runTask(Main.getInstance());
    }

    /**
     * This method gets triggered whenever a player clicks in the custom GUI.
     * If the clicked slot is in the {@link #editableSlots}, nothing happens.
     * If the clicked slot is in the players inventory and {@link #allowPlayerInventoryClicks} is true, nothing happens.
     * Else, the event is cancelled.
     * If the clicked ItemStack is in the given {@link #items}, there will be a callback,
     * containing the mapped object for the given ItemStack.
     * Also, the listener will be unregistered.
     * @param e The event
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inventory = e.getView().getTopInventory();
        if (inventory.equals(targetInventory)) {
            if (Arrays.stream(editableSlots).anyMatch(i -> i == e.getRawSlot())) return;
            if (e.getRawSlot() >= inventory.getSize() && allowPlayerInventoryClicks) return;

            e.setCancelled(true);

            T response = items.getOrDefault(e.getCurrentItem(), null);
            if (response != null) {
                HandlerList.unregisterAll(this);
                confirmationCallback.callback(response);
            }
        }
    }

    /**
     * This method gets triggered whenever a player closes the custom GUI.
     * It gives a null callback, indicating the inventory was closed.
     * Also, the listener will be unregistered.
     * @param e The event
     */
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(targetInventory)) {
            HandlerList.unregisterAll(this);
            confirmationCallback.callback(null);
        }
    }
}
