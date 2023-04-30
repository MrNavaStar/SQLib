[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/Naereen/StrapDown.js/graphs/commit-activity)

<img src="https://raw.githubusercontent.com/MrNavaStar/SQLib/master/src/main/resources/assets/sqlib/icon.png" width="300" height="300">

# SQLib
SQLib is the easiest way to store data for all your minecraft needs! Its simple fabric based sql wrapper made with a focus on minecraft use cases.

# Important Note:
This library is not a full fledged sql wrapper, and does not provide full access to many sql features. 
The main focus of this library is to provide an easy and simple way to store data in your mods.
If you are looking for a more advanced database I recommend taking a look at something like [Nitrite](https://github.com/nitrite/nitrite-java).

# Setup
In your build.gradle include:

``` gradle
repositories {
    maven { url "https://api.modrinth.com/maven" }
}

dependencies {
  modImplementation("maven.modrinth:sqlib:2.0.0")
}
```

# Supported Datatypes
The datatypes can be accessed with the `SQLDataType` class.
| Standard | Minecraft   |
|----------|-------------|
| String   | BlockPos    |
| Int      | ChunkPos    |
| Double   | NbtElement  |
| Long     | Json        |
| Bool     | MutableText |
| UUID     |             |

# General Usage
```java
MySQLDatabase data = new MySQLDatabase("mydata", "192.168.1.69", "3306", "cooluser", "radman");
// OR
SQLiteDatabase data = new SQLiteDatabase("mydata", "some/dir");

Table table = data.createTable("userdata")
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
# Transaction support
This approuch will bach sql commands into one command for faster read/writes of large amounts of data. You can begin and end a transaction at anytime.
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
