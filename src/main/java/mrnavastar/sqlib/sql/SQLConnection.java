package mrnavastar.sqlib.sql;

import mrnavastar.sqlib.SQLib;
import mrnavastar.sqlib.Table;
import org.apache.logging.log4j.Level;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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

    public void beginTransaction(boolean exclusive) {
        try {
            Statement stmt = connection.createStatement();
            stmt.setQueryTimeout(30);
            stmt.execute((exclusive) ? "BEGIN;" : "BEGIN EXCLUSIVE;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void endTransaction() {
        try {
            Statement stmt = connection.createStatement();
            stmt.setQueryTimeout(30);
            stmt.execute("COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(Table table) {
        HashMap<String, SQLDataType> columns = table.getColumns();
        StringBuilder columnString = new StringBuilder();
        columns.forEach((name, dataType) -> columnString.append("%s %s,".formatted(name, dataType)));

        try {
            String sql = table.getDatabase().getTableCreationQuery(table.getNoConflictName(), columnString.toString());
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setQueryTimeout(30);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createRow(Table table, String id) {
        try {
            String sql = "REPLACE INTO %s (ID) VALUES(?)".formatted(table.getNoConflictName());
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setQueryTimeout(30);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(Table table, String id) {
        try {
            String sql = "DELETE FROM %s WHERE ID = ?".formatted(table.getNoConflictName());
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setQueryTimeout(30);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> listPrimaryKeys(Table table) {
        try {
            String sql = "SELECT ID FROM %s".formatted(table.getNoConflictName());
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setQueryTimeout(30);
            List<String> ids = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery();
            
            while (resultSet.next()) ids.add(resultSet.getString(1));
            return ids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public <T> T readField(Table table, Object primaryKey, String field, Class<T> type) {
        try {
            String sql = "SELECT %s FROM %s WHERE ID = ?".formatted(field, table.getNoConflictName());
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setQueryTimeout(30);
            stmt.setObject(1, primaryKey);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            return type.cast(resultSet.getObject(field));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeField(Table table, Object primaryKey, String field, Object value) {
        try {
            String sql = "UPDATE %s SET %s = ? WHERE ID = ?".formatted(table.getNoConflictName(), field);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setQueryTimeout(30);
            stmt.setObject(1, value);
            stmt.setObject(2, primaryKey);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}