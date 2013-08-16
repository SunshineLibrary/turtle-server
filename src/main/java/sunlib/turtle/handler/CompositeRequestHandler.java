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
    ManifestRequestHandler mManifestRequestHandler;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        switch (determineRequestType(request)) {
            case Get:
                /* Always fetch updates and cache result. Use cached result only when offline. Can block. */
                // GET  http://127.0.0.1/exercise/v1
                // GET  http://127.0.0.1/pack/v1

                /* Use cached result when available. Fetch update and cache on cache miss. Can block. */
                // GET  http://127.0.0.1/exercise/v1/subjects/1?ts=123456789
                // GET  http://127.0.0.1/exercise/v1/chapters/1?ts=123456789
                // GET  http://127.0.0.1/exercise/v1/lessons/1?ts=123456789
                // GET  http://127.0.0.1/exercise/v1/lessons/1/%2E%2C%2D.jpg?ts=123456789
                // GET  http://127.0.0.1/pack/v1/subjects/1?ts=123456789
                // GET  http://127.0.0.1/pack/v1/folders/1?ts=123456789
                // GET  http://127.0.0.1/pack/v1/pieces/1?ts=123456789
                // GET  http://127.0.0.1/pack/v1/pieces/1/2e9201af2920ed0192c2.mp4?ts=123456789
                // GET  http://127.0.0.1/pack/v1/images/1/2e9201af2920ed0192c2.jpg?ts=123456789
                return mGetRequestHandler.handleRequest(request);
            case UserData:
                /* Use cached data when available. Fetch server data on cache miss. Can block. */
                // GET  http://127.0.0.1/exercise/v1/user_data/subjects/1
                // GET  http://127.0.0.1/exercise/v1/user_data/achievements/1
                // GET  http://127.0.0.1/pack/v1/user_data/subjects/1
                // GET  http://127.0.0.1/pack/v1/user_data/achievements/1

                /* Update cached result and push update to server. Cannot block; queue up post requests. */
                // POST http://127.0.0.1/exercise/v1/user_data/subjects/1
                // POST http://127.0.0.1/exercise/v1/user_data/achievements/1
                // POST http://127.0.0.1/pack/v1/user_data/subjects/1
                // POST http://127.0.0.1/pack/v1/user_data/achievements/1
                return mPostRequestHandler.handleRequest(request);
            case BatchCache:
                /* Prefetch all required resources base on the manifest. Cannot block; returns cache status. */
                // GET  http://127.0.0.1/exercise/v1/chapters/1?action=cache
                // GET  http://127.0.0.1/exercise/v1/chapters/1?action=status
                // GET  http://127.0.0.1/exercise/v1/folders/1?action=cache
                // GET  http://127.0.0.1/exercise/v1/folders/1?action=status
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
            case BatchCache:
                return mManifestRequestHandler.fetchResponse(request);
        }
        return null;
    }

    @Override
    public void stop() {}

    private RequestType determineRequestType(ApiRequest request) {
        if (NanoHTTPD.Method.POST == request.method) {
            return RequestType.UserData;
        }
        if (NanoHTTPD.Method.GET != request.method) {
            return null;
        }
        return null;
    }

    private static enum RequestType {
        Get, UserData, BatchCache
    }

}
