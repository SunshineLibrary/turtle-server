package sunlib.turtle.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;
import sunlib.turtle.utils.SunWS;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class ProxyRequestHandler implements RequestHandler {

    static Logger logger = LogManager.getLogger(ProxyRequestHandler.class.getName());

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        ApiResponse ret = null;
        try {
            logger.info("proxy fetch,{}", request);
            ApiRequest req = SunWS.url(request.uri).params(request.params);
            // for not caching jsonp part, like "callback_1({...})"
            req.params.remove("callback");
            ret = req.get();
            logger.info("proxy fetched,{}", request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public void stop() {

    }
}
