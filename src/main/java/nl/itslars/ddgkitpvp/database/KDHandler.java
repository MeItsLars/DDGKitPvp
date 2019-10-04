package nl.itslars.ddgkitpvp.database;

import nl.itslars.ddgkitpvp.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles all database-K/D-related actions. It can change and retrieve a player's kills and deaths
 */
public class KDHandler {

    /**
     * Makes sure there is a 'kitpvp' table in the database. If it doesn't exist, it will create one.
     */
    public static void initializeTable() {
        try (Connection con = Main.getDatabase().getConnection()) {
            String tableQuery = "CREATE TABLE IF NOT EXISTS `kitpvp` (" +
                                    "`uuid` VARCHAR(36) NOT NULL," +
                                    "`kills` INT," +
                                    "`deaths` INT," +
                                    "PRIMARY KEY (`uuid`)" +
                                ");";

            try(PreparedStatement stmt = con.prepareStatement(tableQuery)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a players kills and player deaths from the database
     * @param playerUUID The player from which we want to retrieve the kills and deaths
     * @return A Pair, containing the kills and deaths
     */
    public static Pair<Integer, Integer> getPlayerKD(String playerUUID) {
        try (Connection con = Main.getDatabase().getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("SELECT kills, deaths FROM kitpvp WHERE uuid=?;")) {
                stmt.setString(1, playerUUID);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Pair<>(rs.getInt("kills"), rs.getInt("deaths"));
                    } else {
                        addPlayerToTable(con, playerUUID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Pair<>(0, 0);
    }

    /**
     * Adds a new player to the kitpvp table, initializes kills and deaths with 0
     * @param con The database connection that can be used. Prevents pool from overflowing.
     * @param playerUUID The UUID of the player that got a kill
     * @throws SQLException An exception that provides information on a database access error or other errors
     */
    private static void addPlayerToTable(Connection con, String playerUUID) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement("INSERT INTO kitpvp VALUES (?, 0, 0);")) {
            stmt.setString(1, playerUUID);

            stmt.executeUpdate();
        }
    }

    /**
     * Increments the 'kill' count for a player in the database.
     * @param playerUUID The UUID of the player that got a kill
     * @return The current amount of kills and deaths, stored in a pair
     */
    public static Pair<Integer, Integer> addKill(String playerUUID) {
        try (Connection con = Main.getDatabase().getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("UPDATE kitpvp SET kills=kills+1 WHERE uuid=?;")) {
                stmt.setString(1, playerUUID);

                stmt.executeUpdate();
            }
            return getKillsAndDeaths(con, playerUUID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Pair<>(-1, -1);
    }

    /**
     * Increments the 'death' count for a player in the database
     * @param playerUUID the UUID of the player that got a death
     * @return The current amount of kills and deaths, stored in a pair
     */
    public static Pair<Integer, Integer> addDeath(String playerUUID) {
        try (Connection con = Main.getDatabase().getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("UPDATE kitpvp SET deaths=deaths+1 WHERE uuid=?;")) {
                stmt.setString(1, playerUUID);

                stmt.executeUpdate();
            }
            return getKillsAndDeaths(con, playerUUID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Pair<>(-1, -1);
    }

    /**
     * Retrieves the current amount of player kills and deaths, for a given player.
     * It has a Connection as input, to prevent the pool from overflowing.
     * @param con The database connection that can be used. Prevents pool from overflowing.
     * @param playerUUID The UUID of the player that got a kill
     * @return The current amount of kills and deaths, stored in a pair
     * @throws SQLException An exception that provides information on a database access error or other errors
     */
    private static Pair<Integer, Integer> getKillsAndDeaths(Connection con, String playerUUID) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement("SELECT kills, deaths FROM kitpvp WHERE uuid=?;")) {
            stmt.setString(1, playerUUID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pair<>(rs.getInt("kills"), rs.getInt("deaths"));
                }
            }
        }
        return new Pair<>(-1, -1);
    }
}