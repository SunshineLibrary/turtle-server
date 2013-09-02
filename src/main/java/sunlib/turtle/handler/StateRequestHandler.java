package sunlib.turtle.handler;

import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.commons.lang3.ObjectUtils;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.models.CachedText;
import sunlib.turtle.storage.KeyValues;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;

/**
 * User: fxp
 * Date: 13-8-26
 * Time: AM11:29
 */
public class StateRequestHandler implements RequestHandler {

    @Inject
    Cache mCache;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        if ("/state/online".equals(request.uri)) {
            return new ApiResponse(
                    true,
                    new CachedText(request.getCacheId(), ObjectUtils.toString(KeyValues.GLOBAL.get(request.uri)))
            );
        } else if ("/state/cache".equals(request.uri)) {
            return new ApiResponse(true,
                    new CachedText(request.uri, new Gson().toJson(mCache.keySet()))
            );
        }
        return null;
    }

    @Override
    public void stop() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
