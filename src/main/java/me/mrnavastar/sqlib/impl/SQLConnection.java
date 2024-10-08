package me.mrnavastar.sqlib.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.mrnavastar.sqlib.api.DataContainer;
import me.mrnavastar.sqlib.api.DataStore;
import me.mrnavastar.sqlib.impl.config.Config;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.*;


public class SQLConnection {

    private final HikariDataSource ds;
    @Getter
    private final Jdbi sql;

    public SQLConnection(String connectionUrl, Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionUrl);
        config.setUsername(properties.getProperty("user"));
        config.setPassword(properties.getProperty("password"));
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(Config.INSTANCE.database.timeout * 1000L);
        config.setMaxLifetime(1800000); // 30 min
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("db.log.active", "false");
        ds = new HikariDataSource(config);
        sql = Jdbi.create(ds);

        // TODO: This probably doesn't work lol - fix it
        if (properties.getProperty("config_timout") == null) return;
        sql.useHandle(h -> {
            // Try and set the connection's max lifetime to be half of the databases wait_timeout
            Integer timeout = h.select("SHOW VARIABLES LIKE 'wait_timeout';").mapTo(Integer.class).one();
            if (timeout != null) config.setMaxLifetime(timeout / 2);
        });
    }

    public void close() {
        if (ds != null) ds.close();
    }

    public void createTable(DataStore store) {
        sql.useHandle(h -> h.execute(store.getDatabase().getTableCreationQuery(store.toString())));
    }

    public int createRow(DataStore store) {
        try (Handle h = sql.open()) {
            return h.select(store.getDatabase().getRowCreationQuery(store.toString()))
                    .mapTo(Integer.class)
                    .one();
        }
    }

    public void deleteRow(DataStore store, int id) {
        sql.useHandle(h -> h.execute("DELETE FROM %s WHERE SQLIB_AUTO_ID = ?".formatted(store.toString()), id));
    }

    public boolean rowExists(DataStore store, int id) {
        try (Handle h = sql.open()) {
            return h.select("SELECT 1 FROM %s WHERE SQLIB_AUTO_ID = ?".formatted(store.toString()), id)
                    .mapTo(Integer.class)
                    .findFirst()
                    .isPresent();
        }
    }

    public List<Integer> findRows(DataStore store, String field, Object value) {
        try (Handle h = sql.open()) {
            return h.select("SELECT SQLIB_AUTO_ID FROM %s WHERE _%s = ?".formatted(store.toString(), field), value).mapTo(Integer.class).list();
        } catch(Exception ignore) {
            return new ArrayList<>();
        }
    }

    public List<Integer> listIds(DataStore store) {
        try (Handle h = sql.open()) {
            return h.select("SELECT SQLIB_AUTO_ID FROM %s".formatted(store.toString()))
                    .mapTo(Integer.class)
                    .list();
        }
    }

    public List<String> listColumns(DataStore store) {
        try (Handle h = sql.open()) {
            return h.select(store.getDatabase().getColumnListQuery(store.toString()))
                    .mapTo(String.class)
                    .filter(name -> !name.equals("SQLIB_AUTO_ID"))
                    .map(name -> name.substring(1))
                    .list();
        }
    }

    public <T> T readField(DataStore store, int id, String field, Class<T> clazz) {
        try (Handle h = sql.open()) {
            return h.select("SELECT _%s FROM %s WHERE SQLIB_AUTO_ID = ?".formatted(field, store.toString()), id)
                    .mapTo(clazz)
                    .one();
        } catch (Exception ignore) {
            return null;
        }
    }

    public void writeField(DataStore store, int id, List<DataContainer.Transaction.Put> puts) {
        sql.useHandle(h -> {
            StringJoiner fields = new StringJoiner(",");
            ArrayList<Object> values = new ArrayList<>();

            for (DataContainer.Transaction.Put put : puts) {
                fields.add("_%s = ?".formatted(put.field()));
                values.add(put.value());
            }
            values.add(id);

            String update = "UPDATE %s SET %s WHERE SQLIB_AUTO_ID = ?".formatted(store.toString(), fields);
            try {
                h.execute(update, values.toArray());
            } catch (Exception ignore) {
                h.inTransaction(t -> {
                    for (DataContainer.Transaction.Put put : puts)
                        t.execute("ALTER TABLE %s ADD COLUMN _%s %s".formatted(store.toString(), put.field(), store.getDatabase().getDataType(put.type().getType())));
                    return t.execute(update, values.toArray());
                });
            }
        });
    }

    public void clearField(DataStore store, int id, String field) {
        sql.useHandle(h -> h.execute("UPDATE %s SET %s = NULL WHERE SQLIB_AUTO_ID = ?".formatted(store.toString(), field), id));
    }
}