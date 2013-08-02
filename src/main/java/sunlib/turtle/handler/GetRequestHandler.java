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
public class GetRequestHandler implements RequestHandler {

    @Inject Cache mCache;
    @Inject ProxyRequestHandler mProxyRequestHandler;
    @Inject RequestQueue mRequestQueue;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        Object result = mCache.get(request.target);
        if (result == null) {
            ApiResponse response = mProxyRequestHandler.handleRequest(request);
            mCache.put(request.target, response.getData(), 0);
            return response;
        }

        if (requireUpdate(request)) {
            mRequestQueue.enqueueRequest(request);

        }
        //TODO: 创建ApiResponse
        return null;
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        Object response = null;
        mCache.put(request.target, response, 0);
        return response;
    }

    private boolean requireUpdate(ApiRequest request) {
        return true;
    }
}
