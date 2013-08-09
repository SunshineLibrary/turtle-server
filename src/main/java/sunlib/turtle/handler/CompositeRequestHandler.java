package sunlib.turtle.handler;

import com.google.inject.Inject;
import fi.iki.elonen.NanoHTTPD;
import sunlib.turtle.models.ApiRequest;
import sunlib.turtle.models.ApiResponse;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class CompositeRequestHandler implements RequestHandler {

    @Inject
    GetRequestHandler mGetRequestHandler;
    @Inject
    PostRequestHandler mPostRequestHandler;
    @Inject
    ProxyRequestHandler mProxyRequestHandler;
    @Inject
    ManifestRequestHandler mManifestRequestHandler;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        switch (determineRequestType(request)) {
            case Get:
                return mGetRequestHandler.handleRequest(request);
            case Post:
                return mPostRequestHandler.handleRequest(request);
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

    @Override
    public void stop() {

    }

    private RequestType determineRequestType(ApiRequest request) {
        if (NanoHTTPD.Method.GET.equals(request.method)) {
            return RequestType.Get;
        } else {
            return RequestType.Post;
        }
    }

    private static enum RequestType {
        Get, Post, Proxy, Manifest
    }

}
