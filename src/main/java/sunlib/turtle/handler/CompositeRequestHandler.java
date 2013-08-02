package sunlib.turtle.handler;

import com.google.inject.Inject;
import sunlib.turtle.ApiRequest;
import sunlib.turtle.ApiResponse;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class CompositeRequestHandler implements RequestHandler {

    private static enum RequestType { Get, Proxy, Manifest};

    @Inject GetRequestHandler mGetRequestHandler;
    @Inject ProxyRequestHandler mProxyRequestHandler;
    @Inject ManifestRequestHandler mManifestRequestHandler;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        switch (determineRequestType(request)) {
            case Get:
                return mGetRequestHandler.handleRequest(request);
            case Proxy:
                return mProxyRequestHandler.handleRequest(request);
            case Manifest:
                return mManifestRequestHandler.handleRequest(request);
            default:
                return null;
        }
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        switch (determineRequestType(request)) {
            case Get:
                return mGetRequestHandler.fetchResponse(request);
            case Manifest:
                return mManifestRequestHandler.fetchResponse(request);
        }
        return null;
    }

    private RequestType determineRequestType(ApiRequest request) {
        return RequestType.Get;
    }
}
