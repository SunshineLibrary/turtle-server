package sunlib.turtle;

/**
 * User: fxp
 * Date: 13-8-30
 * Time: PM3:46
 */
public class NewCacheTest {
/*
    Cache cache = null;

    @Before
    public void before() throws SQLException {
        System.out.println("before");
        Injector mInjector = Guice.createInjector(new TestModule());
        cache = mInjector.getInstance(Cache.class);
    }

    @Test
    public void testCacheItem() throws Exception {
        String key1 = "key1";
        String content1 = "content1";
        String content_type1 = "content-type-1";

        cache.putItem(new CachedItem(key1, content1, "content-type-1"));
        CachedItem cached = cache.getItem("key1");
        assertEquals(key1, cached.key);
        assertEquals(content1, cached.content);
        assertEquals(content_type1, cached.content_type);
    }

    @Test
    public void testCacheManifest() throws Exception {
        String key1 = "key1";
        String key2 = "key2";
        String content1 = "content1";
        String content_type1 = "content-type-1";
        CachedManifest manifest = new CachedManifest(key2)
                .addItem(new CachedItem(key1, content1, content_type1));
        cache.putManifest(manifest);
        CachedManifest cached = cache.getManifest(key2);
        assertEquals(key2, cached.key);
        assertEquals(1, cached.manifestKeys.size());
        assertEquals(key1, cached.manifestKeys.get(0));
    }

    public class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(Cache.class).to(GlobalCache.class);
        }
    }
    */
}
