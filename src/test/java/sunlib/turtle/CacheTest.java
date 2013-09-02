package sunlib.turtle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.cache.CompositeCache;
import sunlib.turtle.models.CachedFile;
import sunlib.turtle.models.CachedText;
import sunlib.turtle.module.JavaModule;

import java.io.File;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * User: fxp
 * Date: 13-8-30
 * Time: PM3:46
 */
public class CacheTest {

    Cache cache;
    CachedText cachedText = new CachedText("testkey1", "testcontent");

    @Before
    public void before() throws SQLException {
        Injector mInjector = Guice.createInjector(new JavaModule());
        cache = mInjector.getInstance(CompositeCache.class);
    }

    @Test
    public void testFileCache() throws Exception {
        File target = new File("/Users/fxp/Downloads/data.csv");
        CachedFile cachedFile = new CachedFile("testkey2", target);
        cache.put(cachedFile);
        CachedFile cached = (CachedFile) cache.get("testkey2");
        String content = FileUtils.readFileToString(cached.file);

        assertEquals(cached.getCacheId(), "testkey2");
        assertEquals(FileUtils.readFileToString(target), content);
    }

    @Test
    public void testTextCache() throws Exception {
        cache.put(cachedText);
        CachedText cached = (CachedText) cache.get(cachedText.getCacheId());
        assertEquals(cached.getCacheId(), cachedText.getCacheId());
        assertEquals((String) cached.getContent(), cachedText.content);
    }

    @After
    public void after() throws SQLException {
//        connectionSource.close();
        cache.close();
    }
}
