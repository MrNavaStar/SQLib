[![](https://jitpack.io/v/MrNavaStar/SQLib.svg)](https://jitpack.io/#MrNavaStar/SQLib)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/Naereen/StrapDown.js/graphs/commit-activity)\
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](https://lbesson.mit-license.org/)

<img src="https://github.com/MrNavaStar/SQLib/blob/master/SQLibIcon.png" width="300" height="300">

# SQLib
SQLib is the easiest way to store data for all your minecraft needs! Its simple fabric based sql wrapper made with a focus on minecraft use cases.

# Important Note:
This library is not a full fledged sql wrapper, and does not provide full access to many sql features. 
The main focus of this library is to provide an easy and simple way to store data in your mods.
If you are looking for a more advanced database I recommend taking a look at something like [Nitrite](https://github.com/nitrite/nitrite-java).

# Setup
The library first needs to be included into your project. The simplest way to do this is through [Jitpack](https://jitpack.io/).
In your build.gradle make sure to include:

``` gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
  modImplementation 'com.github.MrNavaStar:SQLib:v1.2.0'
  
  //Or if you wish to include with the mod:
  include(modImplementation('com.github.MrNavaStar:SQLib:v1.2.0'))
}
```

# Usage
The database must first be setup to use either sqlite or mysql like so:

- SQLITE
``` java
Database database = new SQLiteDatabase("name", "/awesome/dir");
```
- MYSQL
``` java
Database database = new MySQLDatabase("name", "127.0.0.1", "3306", "username", "password");
```

Now a table can be added to store data. This table will automatically be added to the database (Note that it is not required to include your mod id in the 
table name, however doing so will result in less conflict with other mods that name their tables the same):
``` java
Table playerData = database.createTable(MODID + " name");
```
In order to store data in the table you need a DataContainer object. This will simply provide a way to handle your data in an object orientied manner.
``` java
DataContainer player = playerData.createDataContainer(id);
```
Finally, you can use the DataContainer to put, get, and drop data:

``` java
player.put("NickName", "Bob Ross");
player.put("Health", 100);

String name = player.getString("NickName");
String health = player.getInt("Health");

player.dropString("NickName");
player.dropInt("Health");
```
