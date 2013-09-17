package sunlib.turtle.cache;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.cache.file.FileCache;
import sunlib.turtle.models.*;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class CompositeCache implements Cache {

    static Logger logger = LogManager.getLogger(CompositeCache.class.getName());
    @Inject
    FileCache mFileCache;
    ConnectionSource connectionSource;
    Dao<CachedItem, String> cacheDAO;

    public CompositeCache() throws SQLException {
        String databaseUrl = "jdbc:h2:/sdcard/sunturtle_1/cached_item.v1";
//        String databaseUrl = "jdbc:h2:cached_item.v1";
        connectionSource = new JdbcConnectionSource(databaseUrl);
        TableUtils.createTableIfNotExists(connectionSource, CachedItem.class);
        cacheDAO = DaoManager.createDao(connectionSource, CachedItem.class);
    }

    @Override
    public Cacheable get(String key) {
        logger.info("get an item,{}", key);
        Cacheable ret = null;
        CachedItem item = null;
        try {
            item = cacheDAO.queryForId(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (item != null) {
            logger.info("got a item,{},{}", item.content_type, key);
            if ("text".equals(item.content_type)) {
                ret = new CachedText(key, item.content);
            } else if ("file".equals(item.content_type)) {
                ret = mFileCache.get(key);
            } else if ("manifest".equals(item.content_type)) {
                ret = new Gson().fromJson(item.content, CachedManifest.class);
            }
        }
        return ret;
    }

    @Override
    public void put(Cacheable cacheable) {
        logger.info("put an item,{}", cacheable.getCacheId());
        if (cacheable instanceof CachedText) {
//            mDataCache.put(cacheable);
            try {
                CachedItem item = cacheDAO.queryForId(cacheable.getCacheId());
                if (item != null) {
                    cacheDAO.update(item);
                } else {
                    item = new CachedItem((CachedText) cacheable);
                    cacheDAO.create(item);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (cacheable instanceof CachedFile) {
            try {
                CachedItem item = cacheDAO.queryForId(cacheable.getCacheId());
                if (item != null) {
                    cacheDAO.update(item);
                } else {
                    mFileCache.put(cacheable);
                    CachedFile cached = mFileCache.get(cacheable.getCacheId());
                    item = new CachedItem(cached);
                    cacheDAO.create(item);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (cacheable instanceof CachedManifest) {
            try {
                CachedItem item = cacheDAO.queryForId(cacheable.getCacheId());
                if (item != null) {
                    cacheDAO.update(item);
                } else {
                    item = new CachedItem((CachedManifest) cacheable);
                    cacheDAO.create(item);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Should not be here");
        }
    }

    @Override
    public Set<String> keySet() {
        Set<String> keys = new HashSet<String>();
        try {
            List<CachedItem> all = cacheDAO.queryForAll();
            for (CachedItem i : all) {
                keys.add(i.cachedId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keys;
    }

    @Override
    public void close() {
        try {
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
