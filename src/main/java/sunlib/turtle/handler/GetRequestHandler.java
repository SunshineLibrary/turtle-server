package sunlib.turtle.handler;

import com.google.inject.Inject;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.models.ApiRequest;
import sunlib.turtle.models.ApiResponse;
import sunlib.turtle.queue.RequestQueue;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class GetRequestHandler implements RequestHandler {

    @Inject
    Cache mCache;
    @Inject
    ProxyRequestHandler mProxyRequestHandler;
    @Inject
    RequestQueue mRequestQueue;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        Object result = mCache.get(request.target.getCacheId());
        if (result == null) {
            ApiResponse response = mProxyRequestHandler.handleRequest(request);
            mCache.put(request.target.getCacheId(), response.getData(), 0);
            return response;
        }

        if (requireUpdate(request)) {
            mRequestQueue.enqueueRequest(request);

        }
        //TODO: 创建ApiResponse
        return new ApiResponse(true, result);
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        Object response = null;
        mCache.put(request.target.getCacheId(), response, 0);
        return response;
    }

    private boolean requireUpdate(ApiRequest request) {
        //TODO: judge by timestamp
        return false;
    }

    @Override
    public void stop() {

    }
}
