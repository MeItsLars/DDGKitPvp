package nl.itslars.ddgkitpvp.kitpvp;

import nl.itslars.ddgkitpvp.Main;
import nl.itslars.ddgkitpvp.database.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class handles KitPvp locations.
 * It stores the lobby respawn location, and the arena spawn locations.
 */
public class LocationHandler {

    /**
     * The user-provided lobby location.
     */
    private Location lobbyLocation;

    /**
     * The user-provided arena spawn locations.
     * This maps the location name to the actual Location object.
     */
    private Map<String, Location> arenaSpawnLocations = new HashMap<>();

    /**
     * Loads the lobby location and the arena spawn locations from the config.yml file.
     */
    public void loadLocations() {
        FileConfiguration config = Main.getInstance().getConfig();

        ConfigurationSection section = config.getConfigurationSection("locations");
        if(section != null) {
            String lobbyLocationString = section.getString("lobby_location");
            if(lobbyLocationString == null) {
                lobbyLocation = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
            } else {
                lobbyLocation = LocationConverter.stringToLocation(lobbyLocationString);
            }

            List<String> arenaLocations = section.getStringList("arena_locations");
            for(String arenaLocation : arenaLocations) {
                String[] parts = arenaLocation.split("@");
                arenaSpawnLocations.put(parts[0], LocationConverter.stringToLocation(parts[1]));
            }
        } else {
            lobbyLocation = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        }
    }

    /**
     * Saves the kitpvp stored locations to the config.yml file.
     */
    public void saveLocations() {
        FileConfiguration config = Main.getInstance().getConfig();

        // First, we clear the previously stored locations:
        config.set("locations", null);
        Main.getInstance().saveConfig();

        // Then, we save the current locations:
        config.set("locations.lobby_location", LocationConverter.locationToString(lobbyLocation));
        List<String> arenaLocations = new ArrayList<>();
        for(Map.Entry<String, Location> entry : arenaSpawnLocations.entrySet()) {
            String arenaLocation = entry.getKey() + "@" + LocationConverter.locationToString(entry.getValue());
            arenaLocations.add(arenaLocation);
        }
        config.set("locations.arena_locations", arenaLocations);
        Main.getInstance().saveConfig();
    }

    /**
     * Sets the active lobby location
     * @param location The new lobby location
     */
    public void setLobbyLocation(Location location) {
        lobbyLocation = location;
    }

    /**
     * Adds an arena spawn location
     * @param name The name of the location
     * @param location The actual Location
     */
    public void addArenaLocation(String name, Location location) {
        arenaSpawnLocations.put(name, location);
    }

    /**
     * Removes an arena spawn location
     * @param name The name of the location to be removed
     */
    public void removeArenaLocation(String name) {
        arenaSpawnLocations.remove(name);
    }

    /**
     * Retrieves the current lobby location
     * @return The current lobby location
     */
    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    /**
     * Retrieves the arena location with the given name
     * @param name The name of the arena location
     * @return The Location
     */
    public Location getArenaLocation(String name) {
        return arenaSpawnLocations.get(name);
    }

    /**
     * Retrieves all the arena spawn locations and their names
     * @return A map with all arena spawn names and locations
     */
    public Map<String, Location> getArenaSpawnLocations() {
        return arenaSpawnLocations;
    }

    /**
     * Retrieves a random arena spawn location
     * @return A pair, containing the arena spawn name and location
     */
    public Pair<String, Location> getRandomArenaLocation() {
        int locationNumber = ThreadLocalRandom.current().nextInt(arenaSpawnLocations.size());

        String name = arenaSpawnLocations.keySet().stream()
                .skip(locationNumber)
                .findFirst().orElse(null);
        return (name == null) ? null : new Pair<>(name, arenaSpawnLocations.get(name));
    }
}
