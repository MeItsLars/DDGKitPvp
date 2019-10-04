package nl.itslars.ddgkitpvp.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Class for converting locations to and from strings.
 */
public class LocationConverter {

    /**
     * Converting a given location to a string.
     * @param location The location to be converted
     * @return The resulting string
     */
    public static String locationToString(Location location) {
        return location.getWorld().getName() + "#"
                + location.getX() + "#"
                + location.getY() + "#"
                + location.getZ() + "#"
                + location.getYaw() + "#"
                + location.getPitch();
    }

    /**
     * Converting a given string to a location.
     * @param location The string to be converted
     * @return the resulting location
     */
    public static Location stringToLocation(String location) {
        String[] parts = location.split("#");

        Location result;
        try {
            result = new Location(Bukkit.getWorld(parts[0]),
                    Double.parseDouble(parts[1]),
                    Double.parseDouble(parts[2]),
                    Double.parseDouble(parts[3]),
                    Float.parseFloat(parts[4]),
                    Float.parseFloat(parts[5]));
        } catch(Exception e) {
            result = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        }
        return result;
    }
}
