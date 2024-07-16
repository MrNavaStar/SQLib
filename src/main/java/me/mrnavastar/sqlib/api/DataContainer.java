package me.mrnavastar.sqlib.api;

import lombok.*;
import me.mrnavastar.sqlib.api.types.SQLibType;
import me.mrnavastar.sqlib.impl.SQLConnection;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
public class DataContainer {

    @Getter
    private final DataStore store;
    @Getter
    private final int id;
    private final SQLConnection connection;

    @AllArgsConstructor
    public static class Transaction {

        public record Put(SQLibType<?> type, String field, Object value) {}

        private final DataContainer container;
        private final SQLConnection connection;
        private final ArrayList<Put> puts = new ArrayList<>();

        public <T> Transaction put(SQLibType<T> type, @NonNull String field, T value) {
            puts.add(new Put(type, field, type.serialize(value)));
            return this;
        }

        public DataContainer commit() {
            connection.writeField(container.store, container.id, puts);
            return container;
        }
    }

    public Transaction transaction() {
        return new Transaction(this, connection);
    }

    public <T> void put(SQLibType<T> type, @NonNull String field, T value) {
        connection.writeField(store, id, List.of(new Transaction.Put(type, field, type.serialize(value))));
    }

    public <T> T get(SQLibType<T> type, @NonNull String field) {
        return type.deserialize(connection.readField(store, id, field, type.getType().getClazz()));
    }

    /*@SneakyThrows
    public void clear(@NonNull String field) {
        connection.writeField(store, id, field, null, );
    }*/

    /**
     * Delete the {@link DataContainer} from the database
     */
    @SneakyThrows
    public void delete() {
        connection.deleteRow(store, id);
    }
}