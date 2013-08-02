package sunlib.turtle.handler;

import sunlib.turtle.ApiRequest;
import sunlib.turtle.ApiResponse;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

public interface RequestHandler {
    
    public ApiResponse handleRequest(ApiRequest request);

    public Object fetchResponse(ApiRequest request);
}
