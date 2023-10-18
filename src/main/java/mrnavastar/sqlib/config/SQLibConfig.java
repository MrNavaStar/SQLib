package mrnavastar.sqlib.config;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecNotNull;
import com.electronwill.nightconfig.core.conversion.SpecStringInArray;

public class SQLibConfig {

    @SpecNotNull
    public Database database = new Database();
    @SpecNotNull
    public SQLite sqlite = new SQLite();
    @SpecNotNull
    public MySQL mysql = new MySQL();

    public static class Database {
        @SpecNotNull
        //@Path("enabled")
        @Comment("Setting this to false will disable the internal database, disabling/breaking any mods that rely on this feature. Proceed at your own risk")
        public boolean enabled = true;
        @SpecStringInArray(ignoreCase = true, value = {"sqlite", "mysql"})
        public String type = "sqlite";
        @SpecNotNull
        public String name = "default";
    }

    public static class SQLite {
        @SpecNotNull
        public String directory = "";
    }

    public static class MySQL {
        @SpecNotNull
        public String address = "127.0.0.1";
        @SpecNotNull
        public int port = 3306;
        @SpecNotNull
        public String username = "user";
        @SpecNotNull
        public String password = "pass";
    }
}