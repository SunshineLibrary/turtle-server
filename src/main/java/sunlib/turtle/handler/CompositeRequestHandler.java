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
                // GET  http://127.0.0.1/exercise/subjects/1?ts=123456789
                // GET  http://127.0.0.1/exercise/chapters/1?ts=123456789
                // GET  http://127.0.0.1/exercise/lessons/1?ts=123456789
                // GET  http://127.0.0.1/exercise/lessons/1/%2E%2C%2D.jpg?ts=123456789

                // GET  http://127.0.0.1/pack/subjects/1?ts=123456789
                // GET  http://127.0.0.1/pack/folders/1?ts=123456789
                // GET  http://127.0.0.1/pack/pieces/1?ts=123456789
                // GET  http://127.0.0.1/pack/pieces/1/2e9201af2920ed0192c2.jpg?ts=123456789
                return mGetRequestHandler.handleRequest(request);
            case Post:
                // POST http://127.0.0.1/exercise/user_records/subjects/1
                return mPostRequestHandler.handleRequest(request);
            case Proxy:
                // GET  http://127.0.0.1/exercise/subjects
                // GET  http://127.0.0.1/pack/subjects
                return mProxyRequestHandler.handleRequest(request);
            case Manifest:
                // GET  http://127.0.0.1/exercise/chapters/1?action=cache
                // GET  http://127.0.0.1/exercise/chapters/1?action=status
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
        if (NanoHTTPD.Method.GET == request.method) {
            return RequestType.Get;
        } else {
            return RequestType.Post;
        }
    }

    private static enum RequestType {
        Get, Post, Proxy, Manifest
    }

}
