package sunlib.turtle.handler;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.models.Cacheable;
import sunlib.turtle.queue.RequestQueue;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class GetRequestHandler implements RequestHandler {

    static Logger logger = LogManager.getLogger("GetRequestHandler");
    @Inject
    Cache mCache;
    @Inject
    ProxyRequestHandler mProxyRequestHandler;
    @Inject
    RequestQueue mRequestQueue;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        ApiResponse ret = null;
        String cacheId = request.getCacheId();
        Cacheable cached = mCache.get(cacheId);
        if (cached == null) {
            logger.info("cache miss");
            try {
                ret = mProxyRequestHandler.handleRequest(request);
                if (ret.success) {
                    mCache.put(ret.getData());
                    ret = new ApiResponse(ret, mCache.get(cacheId));
                } else {
                    logger.error("get response failed.{}", request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("cache hit");
            ret = new ApiResponse(true, cached);
        }

        if (requireUpdate(request)) {
            mRequestQueue.enqueueRequest(request);
        }
        //TODO: 创建ApiResponse
        return ret;
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        return null;
    }

    private boolean requireUpdate(ApiRequest request) {
        //TODO: judge by timestamp
        return false;
    }

    @Override
    public void stop() {

    }
}
