package sunlib.turtle.handler;

import com.google.inject.Inject;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.models.CachedText;
import sunlib.turtle.queue.RequestQueue;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;

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

    public Object fetchResponse(ApiRequest request) {
        System.out.println("set user_data," + request.getCacheId() + ":" + request.params.get("data"));
        mCache.put(new CachedText(request.getCacheId(), request.params.get("data")));
        CachedText ret = (CachedText) mCache.get(request.getCacheId());
        System.out.println(ret.getContent());
        return new ApiResponse(true, ret);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stop() {

    }
}
