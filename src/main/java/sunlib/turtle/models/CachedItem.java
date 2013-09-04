package sunlib.turtle.models;

import com.google.gson.Gson;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;

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
    @DatabaseField
    public String mime;
    // 2M for content size
    @DatabaseField(width = 1024 * 1000)
    public String content;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public HashMap<String, String> meta = new HashMap<String, String>();

    public CachedItem(CachedText text) {
        this.cachedId = text.getCacheId();
        this.content_type = "text";
        this.content = text.content;
        this.mime = "application/json";
    }

    public CachedItem(CachedFile file) {
        this.cachedId = file.getCacheId();
        this.content_type = "file";
        this.content = file.file.getAbsolutePath();
        this.mime = "";
    }

    public CachedItem(CachedManifest manifest) {
        this.cachedId = manifest.getCacheId();
        this.content_type = "manifest";
        this.content = new Gson().toJson(manifest);
        this.mime = "";
    }

    public CachedItem() {
    }
}
