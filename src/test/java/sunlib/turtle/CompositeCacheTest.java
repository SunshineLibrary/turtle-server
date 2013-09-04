package sunlib.turtle;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.cache.CompositeCache;
import sunlib.turtle.cache.file.FileCache;
import sunlib.turtle.cache.file.TempFileCache;
import sunlib.turtle.models.CachedFile;
import sunlib.turtle.models.CachedText;

import java.io.File;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * User: fxp
 * Date: 13-9-4
 * Time: PM2:29
 */
public class CompositeCacheTest {

    Cache cache = null;

    @Before
    public void before() throws SQLException {
        System.out.println("before");
        Injector mInjector = Guice.createInjector(new TestModule());
        cache = mInjector.getInstance(Cache.class);
    }

    @Test
    public void testCacheText() {
        String key1 = "key1";
        String value1 = "value1";
        cache.put(new CachedText(key1, value1));
        CachedText cached = (CachedText) cache.get(key1);
        assertEquals((String) cached.getContent(), value1);
    }

    @Test
    public void testCacheFile() {
        String key1 = "key2";
        File file = new File("pom.xml");
        cache.put(new CachedFile(key1, file));
        CachedFile cached = (CachedFile) cache.get(key1);
        assertEquals(cached.getCacheId(), key1);
        assertNotNull(cached.file);
        assertTrue(cached.file.exists());
    }

    public class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(Cache.class).to(CompositeCache.class);
            bind(FileCache.class).to(TempFileCache.class);
        }
    }

}
