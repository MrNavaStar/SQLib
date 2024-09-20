[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/Naereen/StrapDown.js/graphs/commit-activity)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)

[<img alt="modrinth" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/modrinth_vector.svg">](https://modrinth.com/plugin/sqlib)

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/G2G4DZF4D)

<img src="https://raw.githubusercontent.com/MrNavaStar/SQLib/master/src/main/resources/assets/sqlib/icon.png" width="300" height="300">

# SQLib
SQLib is the easiest way to store data for all your minecraft needs! A simple sql wrapper made with a focus on minecraft use cases.

# Important Note:
This library is not a full-fledged sql wrapper, and does not provide full access to many sql features. 
The main focus of this library is to provide an easy and simple way to store data in your mods.
If you are looking for a more advanced database I recommend taking a look at something like [Nitrite](https://github.com/nitrite/nitrite-java).

# Config
The mod generates a config on first time start that allows you to configure the database used by all mods relying on sqlib. 
The default database is a sqlite database running in the sqlib directory.

# Datatypes
The datatypes can be accessed via `JavaTpes`, `MinecraftTypes` or the `AdventureTypes` classes. I tend to add support for new types as I run into them in my projects. If you would like one added, pleade make an issue!
| Standard | Minecraft   | Adventure |
|----------|-------------|-----------|
| Byte     | Vec3i       | Key       |
| Byte[]   | BlockPos    | Component |
| Bool     | ChunkPos    |           |
| Short    | Text        |           |
| Int      | Identifier  |           |
| Float    | Sound       |           |
| Double   | Json        |           |
| Long     | NbtElement  |           |
| String   |             |           |
| Char     |             |           |
| Date     |             |           |
| Color    |             |           |
| UUID     |             |           |
| URI      |             |           |
| URL      |             |           |

You can also add your own custom types as seen here:

# Setup
In your build.gradle include:
``` gradle
repositories {
    maven { url "https://api.modrinth.com/maven" }
}

dependencies {
  modImplementation("maven.modrinth:sqlib:3.2.2")
}
```

# Developer Usage
This example uses the built-in database managed by sqlib. for 99% of mods, using the built-in database is good, however 
further down are examples for custom database management.
```java
// Do not call SQLib.getDatabase() in a early mod initializer. Doing so will likely crash your mod.
// Calling in or after the regular mod initializer is ok.
Database db = SQLib.getDatabase();

DataStore store = db.dataStore("myModId", "userdata");
        
DataContainer playerData = store.createDataContainer();
playerData.put(JavaTypes.STRING, "username", "CoolGuy123");
playerData.put(MinecraftTypes.BLOCKPOS, "home", new BlockPos(304, 62, 37));
playerData.put(MinecraftTypes.NBT, "nbt", new NbtCompound());

System.out.println(playerdata.get(JavaTypes.STRING, "username"));
System.out.println(playerdata.get(MinecraftTypes.BLOCKPOS, "home"));
System.out.println(playerdata.get(MinecraftTypes.NBT, "nbt"));
```

# Custom Database Management
```java
Postgres db = new Postgres("name", "192.168.1.69", "3306", "cooluser", "radman");
// OR
MySQL db = new MySQL( "name", "192.168.1.69", "3306", "cooluser", "radman");
// OR
SQLite db = new SQLite("name", "some/dir");
```

# Transaction Support
This approach will bach sql commands into one command for faster read/writes of large amounts of data.
```java
DataStore store = db.dataStore("modId", "userdata");

DataContainer playerData = table.createDataContainer();
playerData.transaction().put("username", "CoolGuy123").put("home", new BlockPos(304, 62, 37).commit();
```

# Custom Types
You can add a custom type by following the implentations in `JavaTypes`, `MinecraftTypes` and `AdventureTypes`. Then you can use it like any other native SQLib type.
```java
// The SQLPrimitive is the base type to serialize to, and the two function lambdas are to serialize and deserialize from it
public static final SQLibType<JsonElement> JSON = new SQLibType<>(SQLPrimitive.STRING, JsonElement::toString, JsonParser::parseString);

// You can also extend a type like this:
public static final SQLibType<Identifier> IDENTIFIER = new SQLibType<>(SQLPrimitive.STRING, Identifier::toString, Identifier::tryParse);
public static final SQLibType<SoundEvent> SOUND = new SQLibType<>(IDENTIFIER, SoundEvent::getId, SoundEvent::of);
```
