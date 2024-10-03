package valorless.valorlessutils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Preconditions;

import valorless.valorlessutils.ValorlessUtils;
import valorless.valorlessutils.ValorlessUtils.Log;

public abstract class FileStorage extends YamlConfiguration {

    // The File object associated with this FileStorage instance
    private final File file;

    /**
     * Constructor for FileStorage class.
     * @param file The File object representing the file to be managed.
     */
    public FileStorage(File file) {
        this.file = file;
    }

    /**
     * Creates a new file. Returns true if the file is created successfully, false otherwise.
     * @return true if the file is created successfully, false otherwise.
     */
    public boolean createFile() {
        try {
            return this.file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks if the file associated with this instance exists.
     * @return true if the file exists, false otherwise.
     */
    public boolean fileExists() {
        return this.file.exists();
    }

    /**
     * Deletes the file associated with this instance.
     * @return true if the file is deleted successfully, false otherwise.
     */
    public boolean deleteFile() {
        return this.file.delete();
    }

    /**
     * Gets the File object associated with this instance.
     * @return The File object.
     */
    public File getFile() {
        return file;
    }
    
    public static YamlConfiguration loadConfiguration(File file) {
        Preconditions.checkArgument(file != null, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException | InvalidConfigurationException ex) {
            //Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
            // If config is invalid, back it up before resetting
            Log.Error(ValorlessUtils.thisPlugin, "Invalid " + file + " detected. Creating a backup and resetting to default.");
            Log.Error(ValorlessUtils.thisPlugin, "" + ex);
         // Create a backup file with a timestamp in its name
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            File backupFile = new File(file.getParent(), file.getName() + "_" + timestamp + ".yml");

            try {
                // Copy the config file to the backup file
                Files.copy(new File(file.getParent(), file.getName()).toPath(), backupFile.toPath());
                Log.Info(ValorlessUtils.thisPlugin, "Backup created: " + backupFile.getName());
            } catch (IOException E) {
            	Log.Error(ValorlessUtils.thisPlugin, "Failed to create backup of the config file!");
                Log.Error(ValorlessUtils.thisPlugin, "" + E);
            }
        }

        return config;
    }
}
