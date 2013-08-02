package sunlib.turtle.handler;

import sunlib.turtle.ApiRequest;
import sunlib.turtle.ApiResponse;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class ProxyRequestHandler implements RequestHandler {

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        Object response = fetchResponse(request);
        //TODO: 从结果创建ApiResponse
        return null;
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        //TODO: 连接
        return null;
    }
}
