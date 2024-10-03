package valorless.valorlessutils.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

import org.bukkit.plugin.java.JavaPlugin;

public class JsonFile extends FileStorage {

    // Gson instance with pretty printing
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Constructor for JsonFile class.
     * @param file The File object representing the JSON file.
     */
    public JsonFile(JavaPlugin plugin, File file) {
        super(file);
    }

    /**
     * Sets a custom Gson instance.
     * @param gson The Gson instance to set.
     */
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    /**
     * Writes an object to the JSON file.
     * @param object The object to write.
     */
    public void writeObject(Object object) {
        if (!fileExists())
            createFile();
        String json = gson.toJson(object);
        try (FileWriter writer = new FileWriter(getFile())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads an object from the JSON file.
     * @param clazz The class of the object to read.
     * @param <T> The type of the object.
     * @return The read object or null if the file doesn't exist.
     */
    public <T> T readObject(Class<T> clazz) {
        if (!fileExists())
            return null;
        try (FileReader reader = new FileReader(getFile())) {
            return this.gson.fromJson(reader, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
