package sunlib.turtle.handler;

import sunlib.turtle.models.ApiRequest;
import sunlib.turtle.models.ApiResponse;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class ManifestRequestHandler implements RequestHandler {

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        return null;
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        return null;
    }

    @Override
    public void stop() {

    }
}
