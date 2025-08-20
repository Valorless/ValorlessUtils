
# <a href="https://www.spigotmc.org/resources/109586" rel="nofollow"><img src="https://www.spigotmc.org/data/resource_icons/109/109586.jpg" width="70" /></a> ValorlessUtils
<a href="https://github.com/Valorless/ValorlessUtils" rel="nofollow"><img src="https://img.shields.io/badge/Versions-1.18%20--%201.21-brightgreen?style=flat" alt="Versions" style="max-width: 100%;"/></a><br>

ValorlessUtils is a library of various utilities, created to make the creation of my other addons easier.

My primary programing language is C#, so the functions will resemble those.

```xml
<repository>
 <id>jitpack.io</id>
 <url>https://jitpack.io</url>
</repository>

<dependency>
 <groupId>com.github.Valorless</groupId>
 <artifactId>ValorlessUtils</artifactId>
 <version>1.16.0</version>
 <scope>provided</scope>
</dependency>
```

API Docs: [https://valorless.github.io/ValorlessUtils](https://valorless.github.io/ValorlessUtils)

## Features

### - Variable Types.
*"T" refers to type, such as Integer or Double.*

| Type | Parameters | Description |
| --- | --- | --- |
| `Vector2` | `T x, T y` | A generic class representing a 2D vector with components of a number type. |
| `Vector3` | `T x, T y, T z` | A generic class representing a 3D vector with components of a number type. |

#### - Vector2 & Vector3
Both Vector2 & Vector3 have the following functions:<br>
*(Using Vector3 as example)*
| Function | Parameters | Returns | Description |
| --- | --- | --- | --- |
| `Set()` | `Vector component, T value` |  | Sets the value of a specific component in the vector. |
| `Set()` | `T x, T y, T z` |  | Sets the values of all components in the vector. |
| `Equals()` | `Vector3<T> other` | Boolean | Checks if the current Vector3 instance is equal to another Vector3 instance. |

### - Utils.
Various utilities.

| Function | Parameters | Returns | Description |
| --- | --- | --- | --- |
| `IsStringNullOrEmpty()` | `String string` | Boolean | Checks if a given string is null or empty. |
| `Percent()` | `Number current, Number max` | Double/Float | Calculates the percentage of a given value relative to a maximum value. |
| `Chance()` | `Double percent` | Boolean | Determines if an event occurs based on a given percentage chance. |
| `RandomRange()` | `Integer min, Integer max` | Integer | Generates a random integer within the specified range (inclusive). |
| `RandomRange()` | `Double min, Double max` | Double | Generates a random double within the specified range (inclusive). |
| `Clamp()` | `T value, T min, T max` | Comparable | Clamps a value between a minimum and maximum value. |
| `Clamp01()` | `Number value` | Integer/Double/Long/Float | Clamps a Number value between 0 and 1. |
| `Bool.FromValue()` | `Integer value` | Boolean | Converts an Integer value to a Boolean. |
| `Bool.ToValue()` | `Boolean bool` | Integer | Converts a Boolean value to an Integer. |

### - Log.
Easy console logging.

| Function | Parameters | Description | 
| --- | --- | --- |
| `Info()` | `JavaPlugin caller, String msg` | Logs desired message in the console as the type INFO. |
| `Warning()` | `JavaPlugin caller, String msg` | Logs desired message in the console as the type WARNING. |
| `Error()` | `JavaPlugin caller, String msg` | Logs desired message in the console as the type SEVERE. |
| `Debug()` | `JavaPlugin caller, String msg` | Logs desired message in the console as the custom type DEBUG.<br>This checks for the boolean variable 'debug' in the caller's config.yml. |

### - NBT.
Easy NBT tagging.

| Function | Parameters  | Description | Returns |
| --- | --- | --- | --- |
| `Set<type>()` | `ItemStack item, String key, <type> value` |	Sets a custom tag onto chosen ItemStack.<br>Run after setting or before getting ItemMeta. | |
| `Get<type>()` | `ItemStack item, String key` |	Gets the value of a chosen tag in the chosen container. | \<type\> |
| `Has()` | `ItemStack item, String key` |	Checks if the ItemStack has the tag. | Boolean |
  
Supported types: String, Integer, Integer[], Float, Double, UUID, Bool

### - Config.
Easy usage of Config.yml.<br>
<br>
Instatiating new config.
```java
import valorless.valorlessutils.config.Config;
public final Config config = new Config(JavaPlugin, "FileName.Extension");
```

| Function | Parameters  | Description | Returns |
| --- | --- | --- | --- |
| `Reload()` |  |	Reloads file. |  |
| `HasKey()` | `String key` |	Checks file if the key exists. | Boolean |
| `SaveConfig()` | | Saves file. |  |
| `Get()` | `String key` |	Returns value found on key. | Object |
| `GetString()` | `String key` |	Returns value found on key. | String |
| `GetBool()` | `String key` |	Returns value found on key. | Boolean |
| `GetInt()` | `String key` |	Returns value found on key. | Integer |
| `GetFloat()` | `String key` |	Returns value found on key. | Double |
| `GetStringList()` | `String key` |	Returns value found on key. | List\<String> |
| `GetIntList()` | `String key` |	Returns value found on key. | List\<Integer> |
| `GetDoubleList()` | `String key` |	Returns value found on key. | List\<Double> |
| `GetList()` | `String key` |	Returns value found on key. | List\<?> |
| `Set()` | `String key, Object value` |	Set value of key. | |
| `AddValidationEntry()` | `String key, Object defaultValue` |	Add config.yml key you want to validate exists or not.<br>*Primarily used for when updating a plugin, and existing config.yml do not add new keys.* | |
| `Validate()` |  |	Validate config.yml using the entries defined using AddValidationEntry().<br>*Primarily used for when updating a plugin, and existing config.yml do not add new keys.* | |


### - Translator.
Easy usage of item/block translations.<br>
*(Uses translation keys: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Translatable.html#getTranslationKey())*<br>
<br>
*Languages Supported: English, Danish, German, Spanish, French, Turkish, Dutch, Japanese, Korean, Chinese (Simplified), Russian, Polish*<br>
(Additonal languages can be requested, but they must be on this list: https://minecraft.wiki/w/Language#Languages)<br>
<br>
Instatiating new translator.
```java
import valorless.valorlessutils.translate.Translator;

public final Translator translator = new Translator("en-us");
```

| Function | Parameters  | Description | Returns |
| --- | --- | --- | --- |
| `Translate()` | `String translationKey` |	Translate item/block name. **([Example](https://github.com/Valorless/HavenBags/blob/1.19.0/src/main/java/valorless/havenbags/HavenBags.java#L317))** | String |
| `GetLanguageKey()` |  |	Returns current language. (i.e. "en-us") | String |
| `SetLanguage()` | `String key` | Change langauge of the translator. |  |
| `GetLanguageFileContent()` | `String key` |	Returns all content of the current language file. | String |

### - SFX.

| Function | Parameters | Description | Returns |
| --- | --- | --- | --- |
| `Play()` | `String string, Float volume, Float pitch, Player player` | Play a sound at the location of the player with the given volume and pitch. |  |

### - Encoder.

| Function | Parameters | Returns | Description |
| --- | --- | --- | --- |
| `Encode()` | `String message, int shift` | String | Encodes a message using the Caesar Cipher algorithm with a specified shift. |
| `Decode()` | `String encodedMessage, int shift` | String | Decodes an encoded message using the Caesar Cipher algorithm with a specified shift. |

### - Crafting Recipes.
Create custom vanilla-like crafting recipes easily.<br>
Each recipe will check if the player has the permission to craft it.<br>
<br>
Instatiating new CraftRecipe.
```java
import valorless.valorlessutils.crafting.CraftRecipe;
import valorless.valorlessutils.crafting.Ingredient;

ItemStack result = new ItemStack(Material.GRASS_BLOCK);
List<String> shape = new ArrayList<String>();
  shape.add("XXX");
  shape.add("XDX");
  shape.add("XXX");
List<Ingredient> ingredients = new ArrayList<Ingredient>();
  ingredients.add(new Ingredient("D", Material.DIRT));	
CraftRecipe recipe = new CraftRecipe(
  PLUGIN, 
  "TestRecipe1", 
  RecipeType.Shaped, 
  ingredients, 
  result, 
  shape
);
recipe.SetPermission("Test.Permission");
recipe.Add();
```
| Function | Parameters  | Description | Returns |
| --- | --- | --- | --- |
| `Add()` |  | Adds the crafting recipe to the server. |  |
| `Remove()` |  |	Removes the crafting recipe from the server. |  |
| `SetShape()` | `List<String> shape` | Sets the shape of the recipe (for shaped recipes) | |
| `GetShape()` |  |	Gets the shape of the recipe. | List\<String> |
| `SetIngredients()` | `List<Ingredient> ingredients` | Sets the ingredients required for the recipe. |  |
| `GetIngredients()` |  |	Gets the list of ingredients required for the recipe. | List\<Ingredient> |
| `SetType()` | `RecipeType type` | Sets the type of the crafting recipe. |  |
| `GetType()` |  | Gets the type of the crafting recipe. | RecipeType |
| `SetPermission()` | `String permission` | Sets the permission required to craft the recipe.<br> If the permission is set to null, anyone can craft the item. |  |
| `SetPermission()` | `Permission permission` | Sets the permission required to craft the recipe.<br> If the permission is set to null, anyone can craft the item. |  |
| `GetPermission()` |  | Gets the permission required to craft the recipe. | Permission |
| `SetResult()` | `ItemStack result` | Sets the result of the crafting recipe. |  |
| `GetResult()` |  | Gets the result of the crafting recipe. |  |
| `toString()` |  | Returns a string representation of this CraftRecipe object. |  |

# Integration (Very old)

As I've just recently started making plugins, I'm going to asume you're using Eclipse & Maven, as that's what I am.<br>
The way I'm about to show if the say I've found out how to do this.<br>
There are other ways, and if you know those, feel free to do it that way.

### - Step 1: Dependency
Add ValorlessUtils.jar to any folder in your project.<br>
*(I decided to make a new folder called 'dependencies', and placed it there.)*

### - Step 2: pom.xml
Using Maven you have to define your dependencies manually.<br>
So we're gonna add these a plugin and a dependency it into our file 'pom.xml'.

<details>
  <summary>Plugin:</summary>

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-install-plugin</artifactId>
  <version>2.5.2</version>
  <executions>
    <execution>
      <id>install-external</id>
      <phase>clean</phase>
      <configuration>
        <file>${basedir}/dependencies/ValorlessUtils-1.0.0.69-SNAPSHOT.jar</file> <!-- Location of the ValorlessUtils.jar file. -->
        <repositoryLayout>default</repositoryLayout>
        <groupId>valorless.valorlessutils</groupId>
        <artifactId>ValorlessUtils</artifactId>
        <version>1.0.0.69-SNAPSHOT</version> <!-- Or which ever version you choose to use. -->
        <packaging>jar</packaging>
        <generatePom>true</generatePom>
      </configuration>
      <goals>
          <goal>install-file</goal>
      </goals>
    </execution>
  </executions>
</plugin>

```
</details>
<details>
  <summary>Dependency:</summary>

```xml
<dependencies>
  <dependency>
    <groupId>valorless.valorlessutils</groupId>
    <artifactId>ValorlessUtils</artifactId>
    <version>1.0.0.69-SNAPSHOT</version> <!-- Or which ever version you choose to use. -->
  </dependency>
</dependencies>
```
</details>

Your pom.xml file should look something like this now:

<details>
  <summary>pom.xml</summary>

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>valorless.valorlessutils</groupId>
  <artifactId>ValorlessUtils</artifactId>
  <version>1.0.0</version>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.7</source>
          <target>1.7</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-install-plugin</artifactId>
        <version>2.5.2</version>
        <executions>
          <execution>
            <id>install-external</id>
            <phase>clean</phase>
            <configuration>
              <file>${basedir}/dependencies/ValorlessUtils-1.0.0.69-SNAPSHOT.jar</file> <!-- Location of the ValorlessUtils.jar file. -->
              <repositoryLayout>default</repositoryLayout>
              <groupId>valorless.valorlessutils</groupId>
              <artifactId>ValorlessUtils</artifactId>
              <version>1.0.0.69-SNAPSHOT</version> <!-- Or which ever version you choose to use. -->
              <packaging>jar</packaging>
              <generatePom>true</generatePom>
            </configuration>
            <goals>
                <goal>install-file</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
  <repositories>
     <repository>
       <id>spigot-repo</id>
       <url>https://hub.spigotmc.org/nexus/content/repositories/public/</url>
     </repository>
   </repositories>
  <dependencies>
    <dependency>
      <groupId>org.spigotmc</groupId>
      <artifactId>spigot-api</artifactId>
      <version>1.18.2-R0.1-SNAPSHOT</version><!--change this value depending on the version or use LATEST-->
      <type>jar</type>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>valorless.valorlessutils</groupId>
      <artifactId>ValorlessUtils</artifactId>
      <version>1.0.0.69-SNAPSHOT</version>
    </dependency>
  </dependencies>
</project>

```
</details>

Once this is added, save the file and run Maven Clean on your project to install the jar.
<img src="https://i.gyazo.com/97d140492be660a65b44b93bcf73aefb.png" alt="" style="max-width: 100%;"/><br>

### - Step 3: plugin.yml
Inside plugin.yml we have to add `depend: [ValorlessUtils]` for the plugin to require it. or change 'depend' to 'soft-depend' if it's only optional.<br>
In this case we'll be going with 'depend'.<br>
Your plugin.yml file should look something like this now:

<details>
  <summary>plugin.yml</summary>

```yaml
name: HavenBags
main: valorless.havenbags.Main
version: version-number
author: Valorless
api-version: 1.18
website: https://www.spigotmc.org/resources/110420/
description: Create shulker-like bags of varying sizes bound to a player, or accessible by anyone.
depend: [ValorlessUtils]
softdepend: [ChestSort]
commands:
  havenbags:
    description: Main command.
    usage:
    aliases: [bags, bag]
permissions:
  havenbags.*:
    description: Gives access to all HavenBags commands.
    children:
      havenbags.reload: true
      havenbags.use: true
      havenbags.rename: true
      havenbags.create: true
      havenbags.give: true
      havenbags.restore: true
      havenbags.bypass: true
      havenbags.preview: true
  havenbags.reload:
    description: Allows you to reload the configuration.
    default: op
  havenbags.use:
    description: Allows you to use bags.
    default: op
  havenbags.rename:
    description: Allows you to rename bags. Without havenbags.bypass, you can only rename your own bags.
    default: op
  havenbags.create:
    description: Allows you to create bags.
    default: op
  havenbags.give:
    description: Allows you to give bags.
    default: op
  havenbags.restore:
    description: Allows you to restore bags.
    default: op
  havenbags.bypass:
    description: Allows you to bypass ownership locks.
    default: op
  havenbags.preview:
    description: Allows you to bypass preview bags.
    default: op
```
</details>

### - Step 4: Add ValorlessUtils to the Eclipse project. 
Now we have to add the jar file into Java Build Path, so Eclipse can find the packages.<br>
Head into the properties of your project. (Right click your project, or go to 'Project > Properties' )
In there we head to 'Java Build Path > Libraries', and press the button saying 'Add External Jar'.<br>
Locate ValorlessUtils.jar and select it.<br>
Once done, it should look something like this:<br>
<img src="https://i.gyazo.com/4cea1796bf598ea26520976ef457770f.png" alt="" style="max-width: 100%;"/><br>
Apply and Close.

### - Done
With this, ValorlessUtils should now have been added to your project's dependencies, and you can proceed to use it.<br>
I went about it slightly differently in step 4, as I chose 'Add External Class Folder' instead, where all the code from ValorlessUtils is anyway.
