package sunlib.turtle.models;

/**
 * User: fxp
 * Date: 13-8-10
 * Time: PM5:09
 */
public class CachedText implements Cacheable {

    public String key;
    public String content;

    public CachedText(String key, String content) {
        this.key = key;
        this.content = content;
    }

    @Override
    public String getCacheId() {
        return key;
    }

    @Override
    public Long getTimeStamp() {
        return null;
    }

    @Override
    public Object getContent() {
        return content;
    }
}
