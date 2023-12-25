package valorless.valorlessutils.file;

import java.io.File;
import java.io.IOException;

public abstract class FileStorage {

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
}
