package sunlib.turtle.module;

import com.google.inject.AbstractModule;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.cache.CompositeCache;
import sunlib.turtle.cache.data.AndroidDataCache;
import sunlib.turtle.cache.data.DataCache;
import sunlib.turtle.cache.file.AndroidFileCache;
import sunlib.turtle.cache.file.FileCache;
import sunlib.turtle.handler.CompositeRequestHandler;
import sunlib.turtle.handler.RequestHandler;
import sunlib.turtle.queue.AndroidQueue;
import sunlib.turtle.queue.RequestQueue;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */
public class AndroidModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Cache.class).to(CompositeCache.class);
        bind(DataCache.class).to(AndroidDataCache.class);
        bind(FileCache.class).to(AndroidFileCache.class);

        bind(RequestHandler.class).to(CompositeRequestHandler.class);
        bind(RequestQueue.class).to(AndroidQueue.class);
    }
}
