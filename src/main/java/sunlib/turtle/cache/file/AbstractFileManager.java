package sunlib.turtle.cache.file;

import java.io.File;

public interface AbstractFileManager {

    public abstract void setUp() throws Exception;

    public abstract File get(String key);

    public abstract File put(String key, File file);

    public abstract void close() throws Exception;
}
