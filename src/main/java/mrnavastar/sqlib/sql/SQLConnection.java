package mrnavastar.sqlib.sql;

import mrnavastar.sqlib.SQLib;
import mrnavastar.sqlib.Table;
import org.apache.logging.log4j.Level;

import java.sql.*;
import java.util.*;

public class SQLConnection {

    private Connection connection;

    public SQLConnection(String connectionUrl, Properties properties) {
        try {
            connection = DriverManager.getConnection(connectionUrl, properties);
        } catch (SQLException e) {
            SQLib.log(Level.ERROR, "Failed to connect to database!");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            SQLib.log(Level.ERROR, "Gonna be honest, not sure how you got this one.");
            e.printStackTrace();
        }
    }

    public PreparedStatement executeCommand(String sql, boolean autoClose, Object... params) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setQueryTimeout(30);

            int i = 1;
            for (Object param : params) {
                stmt.setObject(i, param);
                i++;
            }

            stmt.execute();
            if (autoClose) stmt.close();
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void beginTransaction(boolean exclusive) {
        executeCommand((exclusive) ? "BEGIN;" : "BEGIN EXCLUSIVE;", true);
    }

    public void endTransaction() {
        executeCommand("COMMIT;", true);
    }

    public void createTable(Table table) {
        HashMap<String, SQLDataType> columns = table.getColumns();
        StringBuilder columnString = new StringBuilder();
        columns.forEach((name, dataType) -> columnString.append("%s %s,".formatted(name, dataType)));

        executeCommand(table.getDatabase().getTableCreationQuery(table.getNoConflictName(), columnString.substring(0, columnString.length() - 1)), true);
    }

    public void createRow(Table table, String id) {
        executeCommand("REPLACE INTO %s (ID) VALUES(?)".formatted(table.getNoConflictName()), true, id);
    }

    public void deleteRow(Table table, String id) {
        executeCommand("DELETE FROM %s WHERE ID = ?".formatted(table.getNoConflictName()), true, id);
    }

    public List<String> listPrimaryKeys(Table table) {
        try {
            PreparedStatement stmt = executeCommand("SELECT ID FROM %s".formatted(table.getNoConflictName()), false);
            List<String> ids = new ArrayList<>();
            ResultSet resultSet = stmt.getResultSet();
            while (resultSet.next()) ids.add(resultSet.getString(1));
            stmt.close();
            return ids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public <T> T readField(Table table, Object primaryKey, String field, Class<T> type) {
        try {
            PreparedStatement stmt = executeCommand("SELECT %s FROM %s WHERE ID = ?".formatted(field, table.getNoConflictName()), false, primaryKey);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            Object object = resultSet.getObject(field);
            stmt.close();

            return type.cast(object);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeField(Table table, Object primaryKey, String field, Object value) {
        executeCommand("UPDATE %s SET %s = ? WHERE ID = ?".formatted(table.getNoConflictName(), field), true, value, primaryKey);
    }
}