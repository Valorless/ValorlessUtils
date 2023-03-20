package valorless.valorlessutils.file;

import java.io.File;
import java.io.IOException;

public abstract class FileStorage {

    private final File file;

    public FileStorage(File file) {
        this.file = file;
    }

    public boolean createFile() {
        try {
            return this.file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    public boolean fileExists() {
        return this.file.exists();
    }

    public boolean deleteFile() {
        return this.file.delete();
    }

    public File getFile() {
        return file;
    }
}
