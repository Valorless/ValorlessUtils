# ValorlessUtils
<img src="https://img.shields.io/badge/Versions-1.18%20--%201.19%2B-brightgreen?style=flat" alt="Versions" style="max-width: 100%;"/><br>

ValorlessUtils is a library of various utilities, created to make the creation of my other addons easier.

My primary programing language is C#, so the functions will resemble those.

## Features

### - Utils.
Various utilities.

| Function | Parameters | Returns | Description |
| --- | --- | --- | --- |
| `IsStringNullOrEmpty()` | `String string` | Boolean | Check whether the string in question is null or empty. |

### - Log.
Easy console logging.

| Function | Parameters | Description | 
| --- | --- | --- |
| `Info()` | `JavaPlugin caller, String msg` | Logs desired message in the console as the type INFO. |
| `Warning()` | `JavaPlugin caller, String msg` | Logs desired message in the console as the type WARNING. |
| `Error()` | `JavaPlugin caller, String msg` | Logs desired message in the console as the type SEVERE. |

### - Tags.
Custom NBT tags.

| Function | Parameters  | Description | Returns |
| --- | --- | --- | --- |
| `Set()` | `JavaPlugin caller, PersistentDataContainer container, String key, Object value, PersistentDataType type` |	Sets a custom tag onto chosen container. | |
| `Get()` | `JavaPlugin caller, PersistentDataContainer container, String key, PersistentDataType type` |	Gets the value of a chosen tag in the chosen container. | Object |
| `Has()` | `JavaPlugin caller, PersistentDataContainer container, String key, PersistentDataType type` |	Checks if the container contains the tag. | Boolean |

### - Config.
Easy usage of Config.yml.

| Function | Parameters  | Description | Returns |
| --- | --- | --- | --- |
| `Load()` | `JavaPlugin caller` |	Will attempt to load config.yml of the chosen plugin.<br>Creating new if it doesn't exist. | |
| `Reload()` | `JavaPlugin caller` |	Reload config.yml. |  |
| `HasKey()` | `JavaPlugin caller, String key` |	Check config.yml if the key exists. | Boolean |
| `Get()` | `JavaPlugin caller, String key` |	Returns value found on key. | Object |
| `GetString()` | `JavaPlugin caller, String key` |	Returns value found on key. | String |
| `GetBool()` | `JavaPlugin caller, String key` |	Returns value found on key. | Boolean |
| `GetInt()` | `JavaPlugin caller, String key` |	Returns value found on key. | Integer |
| `GetFloat()` | `JavaPlugin caller, String key` |	Returns value found on key. | Double |
| `GetColor()` | `JavaPlugin caller, String key` |	Returns value found on key.<br>Value must be hex color. | Color |
| `Set()` | `JavaPlugin caller, String key, Object value` |	Set value of key. | |
| `SetString()` | `JavaPlugin caller, String key, Object value` |	Set String value of key. | |
| `SetBool()` | `JavaPlugin caller, String key, Object value` |	Set Boolean value of key. | |
| `SetInt()` | `JavaPlugin caller, String key, Object value` |	Set Integer value of key. | |
| `SetFloat()` | `JavaPlugin caller, String key, Object value` |	Set Double value of key. | |
| `SetColor()` | `JavaPlugin caller, String key, Object value` |	Set Color value of key. | |
| `AddValidationEntry()` | `JavaPlugin caller, String key, Object defaultValue` |	Add config.yml key you want to validate exists or not.<br>*Primarily used for when updating a plugin, and existing config.yml do not add new keys.* | |
| `Validate()` | `JavaPlugin caller` |	Validate config.yml using the entries defined using AddValidationEntry().<br>*Primarily used for when updating a plugin, and existing config.yml do not add new keys.* | |

## Integration

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

<dependencies>
  <dependency>
    <groupId>valorless.valorlessutils</groupId>
    <artifactId>ValorlessUtils</artifactId>
    <version>1.0.0.69-SNAPSHOT</version> <!-- Or which ever version you choose to use. -->
  </dependency>
</dependencies>
```
</details>
<details>
  <summary>Dependency:</summary>

```xml
<dependency>
  <groupId>valorless.valorlessutils</groupId>
  <artifactId>ValorlessUtils</artifactId>
  <version>1.0.0.69-SNAPSHOT</version> <!-- Or which ever version you choose to use. -->
</dependency>
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
name: SakuraTweaks
main: valorless.sakuratweaks.SakuraTweaks
version: 1.0.0
author: Valorless
api-version: 1.18
depend: [ValorlessUtils]
commands:
  sakuratweaks:
    description: Main command.
    usage:
  st:
    description: Main command.
    usage:
  sakuratweaks reload:
    description: Reloads the configuration file.
    usage: /sakuratweaks reload
    permission: sakuratweaks.reload
    permission-message: You don't have sakuratweaks.reload.
  st reload:
    description: Reloads the configuration file.
    usage: /sakuratweaks reload
    permission: sakuratweaks.reload
    permission-message: You don't have sakuratweaks.reload.
permissions:
  sakuratweaks.*:
    description: Gives access to all SakuraTweaks commands.
    children:
      sakuratweaks.reload: true
  sakuratweaks.reload:
    description: Allows you to reload the configuration.
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
