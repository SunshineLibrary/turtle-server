package sunlib.turtle.models;

import java.util.Map;

/**
 * User: fxp
 * Date: 13-8-5
 * Time: PM2:58
 */
public class Material {

    public String type;
    public String id;
    public Map<String, String> params;

    public Material() {
    }

    public Material(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getCacheId() {
        return type + id;
    }
}
