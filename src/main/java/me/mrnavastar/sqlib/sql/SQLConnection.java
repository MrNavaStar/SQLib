package me.mrnavastar.sqlib.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.mrnavastar.sqlib.api.Table;
import me.mrnavastar.sqlib.config.Config;
import me.mrnavastar.sqlib.util.ReflectionHacks;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;


public class SQLConnection {

    private final HikariDataSource ds;
    @Getter
    private final Jdbi sql;

    //TODO: This is broken
    static {
        // We do not care - Mike Tomlin

        try {
            Field logger = HikariDataSource.class.getDeclaredField("LOGGER");
            logger.setAccessible(true);

            ReflectionHacks.setFinalStatic(logger, LoggerFactory.getLogger("bruh"));


        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        /*Logger.getLogger("com.zaxxer.hikari.pool.PoolBase").setLevel(Level.OFF);
        Logger.getLogger("com.zaxxer.hikari.pool.HikariPool").setLevel(Level.OFF);
        Logger.getLogger("com.zaxxer.hikari.HikariDataSource").setLevel(Level.OFF);
        Logger.getLogger("com.zaxxer.hikari.HikariConfig").setLevel(Level.OFF);
        Logger.getLogger("com.zaxxer.hikari.util.DriverDataSource").setLevel(Level.OFF);*/
    }

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
        ds.close();
    }

    public void createTable(Table table) {
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
        sql.useHandle(h -> h.execute(table.getDatabase().getTableCreationQuery(table.getNoConflictName(), columnString.substring(0, columnString.length() - 1))));
    }

    public int createRow(Table table) {
        try (Handle h = sql.open()) {
            h.execute("REPLACE INTO %s DEFAULT VALUES".formatted(table.getNoConflictName()));
            return h.select("SELECT MAX(SQLIB_AUTO_ID) FROM %s LIMIT 1".formatted(table.getNoConflictName())).mapTo(Integer.class).one();
        }
    }

    public void deleteRow(Table table, int id) {
        sql.useHandle(h -> h.execute("DELETE FROM %s WHERE SQLIB_AUTO_ID = ?".formatted(table.getNoConflictName()), id));
    }

    public boolean rowExists(Table table, int id) {
        try (Handle h = sql.open()) {
            return h.select("SELECT 1 FROM %s WHERE SQLIB_AUTO_ID = ?".formatted(table.getNoConflictName()), id).mapTo(Object.class).one() != null;
        }
    }

    public List<Integer> findRows(Table table, String field, Object value) {
        try (Handle h = sql.open()) {
            return h.select("SELECT SQLIB_AUTO_ID FROM %s WHERE %s = ?".formatted(table.getNoConflictName(), field), value).mapTo(Integer.class).list();
        }
    }

    public List<Integer> listIds(Table table) {
        try (Handle h = sql.open()) {
            return h.select("SELECT SQLIB_AUTO_ID FROM %s".formatted(table.getNoConflictName())).mapTo(Integer.class).list();
        }
    }

    public <T> T readField(Table table, int id, String field, Class<T> clazz) {
        try (Handle h = sql.open()) {
            return h.select("SELECT %s FROM %s WHERE SQLIB_AUTO_ID = ?".formatted(field, table.getNoConflictName()), id).mapTo(clazz).one();
        }
    }

    public void writeField(Table table, int id, String field, Object value) {
        sql.useHandle(h -> h.execute("UPDATE %s SET %s = ? WHERE SQLIB_AUTO_ID = ?;".formatted(table.getNoConflictName(), field), value, id));
    }
}