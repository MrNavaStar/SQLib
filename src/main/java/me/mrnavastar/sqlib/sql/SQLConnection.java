package me.mrnavastar.sqlib.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.mrnavastar.sqlib.SQLib;
import me.mrnavastar.sqlib.api.Table;
import me.mrnavastar.sqlib.config.Config;
import org.apache.logging.log4j.Level;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SQLConnection {

    private record CacheEntry(Table table, int id, String field, Object value) {}

    private final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    private final HikariDataSource ds;
    private final ConcurrentLinkedQueue<CacheEntry> queue = new ConcurrentLinkedQueue<>();
    private Connection connection;

    public SQLConnection(String connectionUrl, Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionUrl);
        config.setUsername(properties.getProperty("user"));
        config.setPassword(properties.getProperty("password"));
        config.setMaximumPoolSize(8);
        config.setConnectionTimeout(Config.INSTANCE.database.timeout * 1000L);
        config.setMaxLifetime(1800000); // 30 min
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);

        try {
            String shouldConfigTimeout = properties.getProperty("config_timout");
            if (shouldConfigTimeout == null) {
                ds.getConnection().close();
                return;
            }

            // Try and set the connection's max lifetime to be half of the databases wait_timeout
            try (PreparedStatement stmt = executeCommand("SHOW VARIABLES LIKE 'wait_timeout';")) {
                ResultSet resultSet = stmt.getResultSet();
                if (resultSet.next()) config.setMaxLifetime(resultSet.getInt("value") / 2);
            }
        } catch (SQLException e) {
            SQLib.log(Level.ERROR, "Failed to connect to database!");
            SQLib.log(Level.ERROR, e.getLocalizedMessage());
            System.exit(1);
        }
    }

    public void close() {
        ds.close();
    }

    public PreparedStatement executeCommand(String sql, Object... params) throws SQLException {
        Connection connection = ds.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setQueryTimeout(Config.INSTANCE.database.timeout);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        stmt.execute();
        return stmt;
    }

    public void holdTransaction() throws SQLException {
        if (connection == null) {
            connection = ds.getConnection();
            connection.setAutoCommit(false);
        }
    }

    public void createTable(Table table) throws SQLException {
        Map<String, Column> columns = table.getColumns();
        StringBuilder columnString = new StringBuilder();
        columns.forEach((name, column) -> {
                columnString.append(column.getName());
                columnString.append(" ");
                columnString.append(table.getDatabase().getDataType(column.getType().sqlType()));
                if (column.isUnique()) columnString.append(" UNIQUE");
                columnString.append(",");
            }
        );
        executeCommand(table.getDatabase().getTableCreationQuery(table.getNoConflictName(), columnString.substring(0, columnString.length() - 1))).close();
    }

    public int createRow(Table table) throws SQLException {
        executeCommand("REPLACE INTO %s DEFAULT VALUES;".formatted(table.getNoConflictName())).close();
        PreparedStatement stmt = executeCommand("SELECT MAX(SQLIB_AUTO_ID) FROM %s LIMIT 1;".formatted(table.getNoConflictName()));
        ResultSet resultSet = stmt.getResultSet();
        resultSet.next();
        int autoId = resultSet.getInt(1);
        stmt.close();
        stmt.getConnection().close();
        return autoId;
    }

    public void deleteRow(Table table, int id) throws SQLException {
        executeCommand("DELETE FROM %s WHERE SQLIB_AUTO_ID = ?;".formatted(table.getNoConflictName()), id).close();
    }

    public boolean rowExists(Table table, int id) throws SQLException {
        try (PreparedStatement stmt = executeCommand("SELECT 1 FROM %s WHERE SQLIB_AUTO_ID = ?;".formatted(table.getNoConflictName()), id)) {
            return stmt.getResultSet().next();
        }
    }

    public List<Integer> findRows(Table table, String field, Object value) throws SQLException {
        try (PreparedStatement stmt = executeCommand("SELECT SQLIB_AUTO_ID FROM %s WHERE %s = ?;".formatted(table.getNoConflictName(), field), value)) {
            List<Integer> ids = new ArrayList<>();
            ResultSet resultSet = stmt.getResultSet();
            while (resultSet.next()) ids.add(resultSet.getInt(1));
            return ids;
        }
    }

    public List<Integer> listIds(Table table) throws SQLException {
        try (PreparedStatement stmt = executeCommand("SELECT SQLIB_AUTO_ID FROM %s;".formatted(table.getNoConflictName()))) {
            List<Integer> ids = new ArrayList<>();
            ResultSet resultSet = stmt.getResultSet();
            while (resultSet.next()) ids.add(resultSet.getInt(1));
            return ids;
        }
    }

    public Object readField(Table table, int id, String field) throws SQLException {
        try (PreparedStatement stmt = executeCommand("SELECT %s FROM %s WHERE SQLIB_AUTO_ID = ?;".formatted(field, table.getNoConflictName()), id)) {
            ResultSet resultSet = stmt.getResultSet();
            resultSet.next();
            Object o = resultSet.getObject(field);
            System.out.println("READ: " + o.getClass().getName());
            return o;
        }
    }

    public void writeField(Table table, int id, String field, Object value) throws SQLException {
        /*queue.add(new CacheEntry(table, id, field, value));

        exec.getQueue().isEmpty()

        exec.scheduleAtFixedRate(() -> {
            if (queue.isEmpty()) {
                exec.shutdown();
                return;
            }
        }, 0, 1, TimeUnit.SECONDS);*/
        System.out.println("pain");

        executeCommand("UPDATE %s SET %s = ? WHERE SQLIB_AUTO_ID = ?;".formatted(table.getNoConflictName(), field), value, id).close();
        System.out.println("pain2");
    }
}