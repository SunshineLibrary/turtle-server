package sunlib.turtle.models;

import java.io.Serializable;

/**
 * User: fxp
 * Date: 13-8-23
 * Time: PM4:29
 */
public class ManifestItem implements Serializable {
    public String url;
    public String ts;
    public String hash;
    public Boolean is_cached;

    @Override
    public String toString() {
        return url + ":" + ts + ":" + hash;
    }
}
