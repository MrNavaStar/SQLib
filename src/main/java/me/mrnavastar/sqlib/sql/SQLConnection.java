package me.mrnavastar.sqlib.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.mrnavastar.sqlib.SQLib;
import me.mrnavastar.sqlib.Table;
import org.apache.logging.log4j.Level;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class SQLConnection {

    private final HikariDataSource ds;

    public SQLConnection(String connectionUrl, Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionUrl);
        config.setUsername(properties.getProperty("user"));
        config.setPassword(properties.getProperty("password"));
        config.setMaximumPoolSize(8);
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
        stmt.setQueryTimeout(30);

        int i = 1;
        for (Object param : params) {
            stmt.setObject(i, param);
            i++;
        }

        stmt.execute();
        return stmt;
    }

    public void beginTransaction(String transactionString) throws SQLException {
        executeCommand(transactionString).close();
    }

    public void endTransaction() throws SQLException {
        executeCommand("COMMIT;").close();
    }

    public void createTable(Table table, boolean autoIncrementId) throws SQLException {
        HashMap<String, SQLDataType> columns = table.getColumns();
        StringBuilder columnString = new StringBuilder();
        columns.forEach((name, dataType) -> columnString.append("%s %s,".formatted(name, dataType)));

        executeCommand(table.getDatabase().getTableCreationQuery(table.getNoConflictName(), columnString.substring(0, columnString.length() - 1), autoIncrementId)).close();
    }

    public int createRow(Table table, String id, boolean autoIncrementId) throws SQLException {
        if (autoIncrementId) {
            executeCommand("REPLACE INTO %s DEFAULT VALUES".formatted(table.getNoConflictName())).close();
            PreparedStatement stmt = executeCommand("SELECT MAX(ID) FROM %s LIMIT 1".formatted(table.getNoConflictName()));
            ResultSet resultSet = stmt.getResultSet();
            resultSet.next();
            int autoId = resultSet.getInt(1);
            stmt.close();
            stmt.getConnection().close();
            return autoId;
        }

        executeCommand("REPLACE INTO %s (ID) VALUES(?)".formatted(table.getNoConflictName()), id).close();
        return -1;
    }

    public void deleteRow(Table table, String id) throws SQLException {
        executeCommand("DELETE FROM %s WHERE ID = ?".formatted(table.getNoConflictName()), id).close();
    }

    public boolean rowExists(Table table, String id) throws SQLException {
        try (PreparedStatement stmt = executeCommand("SELECT 1 FROM %s WHERE ID = ?".formatted(table.getNoConflictName()), id)) {
            return stmt.getResultSet().next();
        }
    }

    public List<String> listPrimaryKeys(Table table) throws SQLException {
        try (PreparedStatement stmt = executeCommand("SELECT ID FROM %s".formatted(table.getNoConflictName()))) {
            List<String> ids = new ArrayList<>();
            ResultSet resultSet = stmt.getResultSet();
            while (resultSet.next()) ids.add(resultSet.getString(1));
            return ids;
        }
    }

    public <T> T readField(Table table, Object primaryKey, String field, Class<T> type) throws SQLException {
        try (PreparedStatement stmt = executeCommand("SELECT %s FROM %s WHERE ID = ?".formatted(field, table.getNoConflictName()), primaryKey)) {
            ResultSet resultSet = stmt.getResultSet();
            resultSet.next();
            Object object = resultSet.getObject(field);
            return type.cast(object);
        }
    }

    public void writeField(Table table, Object primaryKey, String field, Object value) throws SQLException {
        executeCommand("UPDATE %s SET %s = ? WHERE ID = ?".formatted(table.getNoConflictName(), field), value, primaryKey).close();
    }
}