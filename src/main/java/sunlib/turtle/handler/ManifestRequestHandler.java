package sunlib.turtle.handler;

import com.google.inject.Inject;
import sunlib.turtle.ApiRequest;
import sunlib.turtle.ApiResponse;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.queue.RequestQueue;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class ManifestRequestHandler implements RequestHandler {

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        return null;
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        return null;
    }
}
