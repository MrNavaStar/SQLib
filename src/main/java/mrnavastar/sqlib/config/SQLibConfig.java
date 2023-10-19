package mrnavastar.sqlib.config;

public class SQLibConfig {

    public Database database;
    public SQLite sqlite;
    public MySQL mysql;

    public static class Database {
        public String type;
        public String name;
    }

    public static class SQLite {
        public String directory;
    }

    public static class MySQL {
        public String address;
        public int port = -1;
        public String username;
        public String password;
    }

    public boolean validateBase() {
        return database != null && database.name != null && database.type != null;
    }

    public boolean validateSQLite() {
        return validateBase() && sqlite != null && sqlite.directory != null;
    }

    public boolean validateMySQL() {
        return validateBase() && mysql != null && mysql.address != null && mysql.port !=-1 && mysql.username != null && mysql.password != null;
    }
}