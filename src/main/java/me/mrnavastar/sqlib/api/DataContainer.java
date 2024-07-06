package me.mrnavastar.sqlib.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.sql.SQLConnection;
import me.mrnavastar.sqlib.sql.ColumnType;

import java.sql.SQLException;

/**
 * This class represents a row in the {@link Table} in the {@link me.mrnavastar.sqlib.database.Database}
 */
@AllArgsConstructor
public class DataContainer {

    @NonNull private final Table table;
    @Getter
    private final int id;
    @NonNull private final SQLConnection sqlConnection;

    public <T> DataContainer put(ColumnType<T> type, @NonNull String field, T value) throws SQLException {
        sqlConnection.writeField(table, id, field, type.serialize(value));
        return this;
    }

    public <T> T get(ColumnType<T> type, @NonNull String field) throws SQLException {
        return type.deserialize(type.sqlType().getClazz().cast(sqlConnection.readField(table, id, field)));
    }

    @SneakyThrows
    public void clear(@NonNull String field) {
        sqlConnection.writeField(table, id, field, null);
    }

    /**
     * Delete the {@link DataContainer} from the database
     */
    @SneakyThrows
    public void delete() {
        sqlConnection.deleteRow(table, id);
    }
}