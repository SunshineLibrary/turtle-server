package sunlib.turtle.cache;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import sunlib.turtle.cache.file.TempFileManager;
import sunlib.turtle.models.Cacheable;
import sunlib.turtle.models.CachedFile;
import sunlib.turtle.models.CachedItem;
import sunlib.turtle.models.CachedText;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
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

    //    @Inject
//    DataCache mDataCache;
//    @Inject
//    FileCache mFileCache;
    ConnectionSource connectionSource;
    Dao<CachedItem, String> cacheDAO;

    public CompositeCache() throws SQLException {
        String databaseUrl = "jdbc:h2:mem:cached_item";
        connectionSource = new JdbcConnectionSource(databaseUrl);
        cacheDAO = DaoManager.createDao(connectionSource, CachedItem.class);
        TableUtils.createTable(connectionSource, CachedItem.class);
    }

    @Override
    public Cacheable get(String key) {
        Cacheable ret = null;
        CachedItem item = null;
        try {
            item = cacheDAO.queryForId(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (item != null) {
            if ("text".equals(item.content_type)) {
                ret = new CachedText(key, item.content);
            } else if ("file".equals(item.content_type)) {
                try {
                    ret = new CachedFile(key, TempFileManager.getInstance().get(key));
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return ret;
    }

    @Override
    public void put(Cacheable cacheable) {
        if (cacheable instanceof CachedText) {
//            mDataCache.put(cacheable);
            try {
                CachedItem item = cacheDAO.queryForId(cacheable.getCacheId());
                if (item != null) {
                    System.err.println("SHOULD NOT BE HERE");
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
                    System.err.println("SHOULD NOT BE HERE");
                } else {
                    File newFile = TempFileManager.getInstance().put(
                            cacheable.getCacheId(),
                            ((CachedFile) cacheable).file);
                    ((CachedFile) cacheable).file = newFile;
                    item = new CachedItem((CachedFile) cacheable);
                    cacheDAO.create(item);
                    System.out.println("created a tmp file," + newFile.getAbsolutePath());
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
