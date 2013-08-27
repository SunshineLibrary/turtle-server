package sunlib.turtle.handler;

import org.apache.commons.lang3.ObjectUtils;
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

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
//        return new ApiResponse()
        return new ApiResponse(
                true,
                new CachedText(request.getCacheId(), ObjectUtils.toString(KeyValues.GLOBAL.get(request.uri)))
        );
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stop() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
