package me.mrnavastar.sqlib.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.mrnavastar.sqlib.api.types.SQLibType;
import me.mrnavastar.sqlib.impl.SQLConnection;
import me.mrnavastar.sqlib.api.database.Database;

/**
 * This class represents a row a {@link Table} in a {@link Database}.
 */
@AllArgsConstructor
public class DataContainer {

    @NonNull private final Table table;
    @Getter
    private final int id;
    @NonNull private final SQLConnection sqlConnection;

    public <T> DataContainer put(SQLibType<T> type, @NonNull String field, T value) {
        sqlConnection.writeField(table, id, field, type.serialize(value));
        return this;
    }

    public <T> T get(SQLibType<T> type, @NonNull String field) {
        return type.deserialize(sqlConnection.readField(table, id, field, type.getType().getClazz()));
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