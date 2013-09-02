package sunlib.turtle.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * User: fxp
 * Date: 13-8-30
 * Time: PM4:12
 */
@DatabaseTable(tableName = "cached_items")
public class CachedItem {

    @DatabaseField(id = true)
    public String cachedId;
    @DatabaseField
    public String content_type;
    @DatabaseField(width = 1024 * 1000)
    public String content;

    public CachedItem(CachedText text) {
        this.cachedId = text.getCacheId();
        this.content_type = "text";
        this.content = text.content;
    }

    public CachedItem(CachedFile file) {
        this.cachedId = file.getCacheId();
        this.content_type = "file";
        this.content = file.file.getAbsolutePath();
    }

    public CachedItem() {
    }

}
