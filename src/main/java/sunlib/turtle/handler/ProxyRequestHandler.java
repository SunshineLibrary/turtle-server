package sunlib.turtle.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;
import sunlib.turtle.utils.WS;

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
        return (ApiResponse) fetchResponse(request);
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        ApiResponse ret = null;
        try {
            logger.info("proxy fetch,{}", request);
            ApiRequest req = WS.url(request.uri).params(request.params);
            req.params.remove("callback");
            ret = req.get();
            logger.info("proxy fetched,{}", request);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return ret;
    }

    public boolean isBinaryStream(String type) {
        return (type.contains("image") ||
                type.contains("audio") ||
                type.contains("video"));
    }

    @Override
    public void stop() {

    }
}
