package me.mrnavastar.sqlib.api.database;

import lombok.Getter;
import lombok.NonNull;
import me.mrnavastar.sqlib.api.DataStore;
import me.mrnavastar.sqlib.impl.SQLConnection;
import me.mrnavastar.sqlib.impl.SQLPrimitive;
import me.mrnavastar.sqlib.impl.config.SQLibConfig;

import java.util.*;

/**
 * This class can be extended to allow for new database implementations
 */
public abstract class Database {

    private static final HashSet<Database> databases = new HashSet<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> databases.forEach(Database::close)));
        SQLibConfig.load();
    }

    public static List<Database> getDatabases() {
        return List.copyOf(databases);
    }

    @Getter
    protected final String name;
    protected SQLConnection connection;

    public Database(@NonNull String name) {
        this.name = name;
        databases.add(this);
    }

    protected abstract String getConnectionUrl();

    protected Properties getConnectionProperties() {
        return new Properties();
    }

    public abstract String getTableCreationQuery(String tableName);

    public String getRowCreationQuery(String rowName) {
        return "INSERT INTO %s DEFAULT VALUES RETURNING SQLIB_AUTO_ID".formatted(rowName);
    }

    public abstract String getColumnListQuery(String tableName);

    public abstract String getDataType(SQLPrimitive<?> dataType);

    protected void connect() {
        connection = new SQLConnection(getConnectionUrl(), getConnectionProperties());
    }

    public void close() {
        connection.close();
    }

    public DataStore dataStore(String modId, String name) {
        return new DataStore(modId, name, this, connection);
    }
}