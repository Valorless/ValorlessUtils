package valorless.valorlessutils.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class JsonFile extends FileStorage {

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public JsonFile(File file) {
        super(file);
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

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