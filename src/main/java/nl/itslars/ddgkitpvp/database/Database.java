package nl.itslars.ddgkitpvp.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class for managing a Hikari database.
 */
public class Database {

    /**
     * Stores the associated HikariDataSource
     */
    private HikariDataSource dataSource;

    /**
     * Initializes the {@link #dataSource} with database information
     * @param host The database host
     * @param port The database port
     * @param username The database username
     * @param password The database password
     * @param database The database name
     */
    public Database(String host, int port, String username, String password, String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.setPoolName("ServerCore-hikari");
        config.setMaximumPoolSize(3);
        config.setMinimumIdle(10);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(5000);
        config.setInitializationFailTimeout(-1);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Gives a connection from the {@link #dataSource}
     * @return A connection from the pool
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Closes the database pool
     */
    public void close() {
        dataSource.close();
    }
}
