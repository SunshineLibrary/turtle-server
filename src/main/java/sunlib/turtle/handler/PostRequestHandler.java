package sunlib.turtle.handler;

import com.google.inject.Inject;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.models.ApiRequest;
import sunlib.turtle.models.ApiResponse;
import sunlib.turtle.queue.RequestQueue;

import java.util.Date;

/**
 * User: fxp
 * Date: 13-8-6
 * Time: PM4:58
 */
public class PostRequestHandler implements RequestHandler {

    @Inject
    Cache mCache;
    @Inject
    ProxyRequestHandler mProxyRequestHandler;
    @Inject
    RequestQueue mRequestQueue;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        Object resp = fetchResponse(request);
        return (ApiResponse) resp;
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
//        mCache.put(
//                request.target.getCacheId(),
//                request.params.get("data"),
//                (new Date()).getTime()
//        );
        return new ApiResponse(true, null);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stop() {

    }
}
