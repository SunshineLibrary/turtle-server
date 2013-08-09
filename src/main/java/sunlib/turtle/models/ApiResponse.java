package sunlib.turtle.models;

import java.io.Serializable;

/**
 * User: fxp
 * Date: 13-8-1
 * Time: PM3:12
 */
public class ApiResponse implements Serializable {

    public boolean success;
    public Object result;

    public ApiResponse(boolean success, Object result) {
        this.success = success;
        this.result = result;
    }

    public Object getData() {
        return result;
    }

}
