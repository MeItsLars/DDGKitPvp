package nl.itslars.ddgkitpvp;

import nl.itslars.ddgkitpvp.events.*;
import nl.itslars.ddgkitpvp.database.Database;
import nl.itslars.ddgkitpvp.database.KDHandler;
import nl.itslars.ddgkitpvp.kitpvp.LocationHandler;
import nl.itslars.ddgkitpvp.kits.KitHandler;
import nl.itslars.ddgkitpvp.scoreboard.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    /**
     * Contains the JavaPlugin instance. Can be accessed via the {@link #getInstance()} method.
     */
    private static JavaPlugin instance;

    /**
     * Contains the Hikari Database pool instance. Can be accessed via the {@link #getDatabase()} method.
     */
    private static Database database;

    /**
     * Contains the KitPvp scoreboard handler. This class contains all players and their scoreboards.
     * It is used when the kills or deaths need to be updated.
     */
    private static ScoreboardHandler scoreboardHandler;

    /**
     * Contains the KitPvp location handler. This class contains all relevant KitPvp locations:
     * - The lobby (respawn) location
     * - The arena spawn locations
     */
    private static LocationHandler locationHandler;

    /**
     * Contains the KitPvp kit handler. This class contains all kits that a user can choose.
     * The class is used whenever a player wants to acces, add, or remove a kit.
     */
    private static KitHandler kitHandler;

    /**
     * This method gets called when the plugin is enabled.
     * It performs the following actions:
     * - Copies the default config values
     * - Creates a few instances of objects, described above
     * - Initializes the Kills/Deaths handler
     * - Loads data from the config.yml file into the memory
     * - Registers commands and events
     */
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        instance = this;
        database = new Database("localhost", 3306, "root", "password", "KitPVP");
        scoreboardHandler = new ScoreboardHandler();
        locationHandler = new LocationHandler();
        kitHandler = new KitHandler();

        KDHandler.initializeTable();
        locationHandler.loadLocations();
        kitHandler.loadKits();

        Bukkit.getPluginManager().registerEvents(new PlayerClickSign(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKillDeath(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawn(), this);
        Bukkit.getPluginManager().registerEvents(new SignEdit(), this);
        getCommand("kitpvp").setExecutor(new KitPvpCommand());
    }

    /**
     * This method gets called when the plugin is disabled.
     * It closes the database, and saves memory data to the config.yml file.
     */
    @Override
    public void onDisable() {
        database.close();
        locationHandler.saveLocations();
        kitHandler.saveKits();
    }

    /**
     * Retrieves the JavaPlugin instance, used for getting access to the config, using schedulers, etc.
     * @return The JavaPlugin instance
     */
    public static JavaPlugin getInstance() {
        return instance;
    }

    /**
     * Retrieves the Database object, used for performing database operations
     * @return The Database object
     */
    public static Database getDatabase() {
        return database;
    }

    /**
     * Retrieves the scoreboard handler, used for performing scoreboard-related actions
     * @return The scoreboard handler
     */
    public static ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    /**
     * Retrieves the location handler, used for performing KitPvp-location-related actions
     * @return The location handler
     */
    public static LocationHandler getLocationHandler() {
        return locationHandler;
    }

    /**
     * Retrieves the kit handler, used for performing KitPvp-kit-related actions
     * @return The kit handler
     */
    public static KitHandler getKitHandler() {
        return kitHandler;
    }
}
