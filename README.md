[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/Naereen/StrapDown.js/graphs/commit-activity)\
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)

[![name](https://github.com/modrinth/art/blob/main/Branding/Badge/badge-dark__184x72.png?raw=true)](https://modrinth.com/mod/sqlib)

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/G2G4DZF4D)

<img src="https://raw.githubusercontent.com/MrNavaStar/SQLib/master/src/main/resources/assets/sqlib/icon.png" width="300" height="300">

# SQLib
SQLib is the easiest way to store data for all your minecraft needs! Its simple fabric based sql wrapper made with a focus on minecraft use cases.

# Important Note:
This library is not a full-fledged sql wrapper, and does not provide full access to many sql features. 
The main focus of this library is to provide an easy and simple way to store data in your mods.
If you are looking for a more advanced database I recommend taking a look at something like [Nitrite](https://github.com/nitrite/nitrite-java).

# Config

The mod generates a config on first time start that allows you to configure the database used by all mods relying on sqlib. 
The default database is a sqlite database running in the sqlib directory.

# Supported Datatypes
The datatypes can be accessed with the `SQLDataType` class.
| Standard | Minecraft   | 
|----------|-------------|
| String   | BlockPos    |
| Int      | ChunkPos    |
| Double   | NbtElement  |
| Long     | Json        |
| Bool     | MutableText |
| UUID     | Identifier  |

# Setup
In your build.gradle include:
``` gradle
repositories {
    maven { url "https://api.modrinth.com/maven" }
}

dependencies {
  modImplementation("maven.modrinth:sqlib:2.2.0")
}
```
# Developer Usage
This example uses the built-in database managed by sqlib. for 99% of mods, using the built-in database is good, however 
further down are examples for custom database management.
```java
Database database = SQLib.getDatabase();

Table table = database.createTable("userdata")
        .addColumn("username", SQLDataType.STRING)
        .addColumn("home", SQLDataType.BLOCKPOS)
        .addColumn("nbt", SQLDataType.NBT)
        .finish();
        
DataContainer playerData = table.createDataContainer(UUID.randomUUID());
playerData.put("username", "CoolGuy123");
playerData.put("home", new BlockPos(304, 62, 37);
playerData.put("nbt", new NbtCompound());

System.out.println(playerdata.getString("username"));
System.out.println(playerdata.getBlockPos("home"));
System.out.println(playerdata.getNbt("nbt"));

data.close();
```

# Custom Database Management
```java
MySQLDatabase database = new MySQLDatabase("modId", "mydata", "192.168.1.69", "3306", "cooluser", "radman");
// OR
SQLiteDatabase database = new SQLiteDatabase("modId", "mydata", "some/dir");
```

# Auto Incrementing Tables
You can make a table that auto increments its id with the following:
```java
Table table = SQLib.getDatabase().createTable("towns")
        .setAutoIncrement()
        .addColumn("city", SQLDataType.STRING)
        .addColumn("portal", SQLDataType.BLOCKPOS)
        .finish();

DataContainer data = table.createDataContainerAutoID();
int id = data.getIdAsInt();
```

# Transaction Support
This approach will bach sql commands into one command for faster read/writes of large amounts of data. You can begin and end a transaction at anytime.
```java
Table table = data.createTable("userdata")
        .addColumn("username", SQLDataType.STRING)
        .addColumn("home", SQLDataType.BLOCKPOS)
        .addColumn("nbt", SQLDataType.NBT)
        .finish();

table.beginTransaction();
DataContainer playerData = table.createDataContainer(UUID.randomUUID());
playerData.put("username", "CoolGuy123");
playerData.put("home", new BlockPos(304, 62, 37);
table.endTransaction();

playerData.put("nbt", new NbtCompound());
```
# Custom SQL Commands
If you need to do more complex things than the api allows for, you can run custom SQL commands.
```java
MySQLDatabase database = new MySQLDatabase("modId", "mydata", "192.168.1.69", "3306", "cooluser", "radman");
Table table = data.createTable("userdata")
        .addColumn("username", SQLDataType.STRING)
        .addColumn("home", SQLDataType.BLOCKPOS)
        .addColumn("nbt", SQLDataType.NBT)
        .finish();

PreparedStatment stmt = database.executeCommand("SELECT ID FROM userdata WHERE username = ?", false, "bobross");
ResultSet result = stmt.getResultSet();

// Handle result

stmt.close();

//If you just want to run a command and not handle the result do the following. It will autoclose for you.
database.executeCommand("DELETE FROM userdata WHERE ID = ?", true, "bobross");
```
