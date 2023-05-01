package mrnavastar.sqlib.database;

public class SQLiteDatabase extends Database {

    private final String directory;

    public SQLiteDatabase(String modId, String name, String directory) {
        super(modId, name);
        this.directory = directory;
        open();
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:sqlite:" + directory + "/" + name + ".db";
    }

    @Override
    public String getTableCreationQuery(String tableName, String columns) {
        return "CREATE TABLE IF NOT EXISTS %s (%s ID MEDIUMTEXT PRIMARY KEY);".formatted(tableName, columns);
    }

    @Override
    public void beginTransaction() {
        sqlConnection.beginTransaction(true);
    }
}