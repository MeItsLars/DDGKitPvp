package nl.itslars.ddgkitpvp.kits.inventory.callback;

/**
 * This class contains the method that should be called whenever a player clicks an item in the custom GUI.
 * @param <T> The type of the callback object, to be returned when the player clicks an item.
 */
public abstract class ConfirmationCallback<T> {

    /**
     * The body of this method can be adapted to specific cases, and is provided by the programmer.
     * @param response The response of the click.
     */
    public abstract void callback(T response);

}
