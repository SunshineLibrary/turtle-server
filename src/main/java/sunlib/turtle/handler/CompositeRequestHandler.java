package sunlib.turtle.handler;

import com.google.inject.Inject;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;

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
    @Inject
    ProxyRequestHandler mProxyRequestHandler;
    @Inject
    StateRequestHandler mStateRequestHandler;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        switch (request.type) {
            case GET:
                /**
                 * Always fetch updates and cache content.
                 * Use cached content only when offline.
                 * Can block.
                 */
                // GET  http://127.0.0.1/exercise/v1
                // GET  http://127.0.0.1/pack/v1

                return mGetRequestHandler.handleRequest(request);
            case GET_CACHE:
                /**
                 * Use cached content when available.
                 * Fetch update and cache on cache miss.
                 * Can block.
                 */
                // GET  http://127.0.0.1/exercise/v1/subjects/1?ts=123456789
                // GET  http://127.0.0.1/exercise/v1/chapters/1?ts=123456789
                // GET  http://127.0.0.1/exercise/v1/lessons/1?ts=123456789
                // GET  http://127.0.0.1/exercise/v1/lessons/1/%2E%2C%2D.jpg?ts=123456789
                // GET  http://127.0.0.1/pack/v1/subjects/1?ts=123456789
                // GET  http://127.0.0.1/pack/v1/folders/1?ts=123456789
                // GET  http://127.0.0.1/pack/v1/pieces/1?ts=123456789
                // GET  http://127.0.0.1/pack/v1/pieces/1/2e9201af2920ed0192c2.mp4?ts=123456789
                // GET  http://127.0.0.1/pack/v1/images/1/2e9201af2920ed0192c2.jpg?ts=123456789
                /**
                 * Use cached data when available.
                 * Fetch server data on cache miss.
                 * Can block.
                 */
                // GET  http://127.0.0.1/exercise/v1/user_data/subjects/1
                // GET  http://127.0.0.1/exercise/v1/user_data/achievements/1
                // GET  http://127.0.0.1/pack/v1/user_data/subjects/1
                // GET  http://127.0.0.1/pack/v1/user_data/achievements/1
                return mGetRequestHandler.handleRequest(request);

            case POST_CACHE:
                /**
                 * Update cached content and push update to server.
                 * Cannot block. queue up post requests.
                 */
                // POST http://127.0.0.1/exercise/v1/user_data/subjects/1
                // POST http://127.0.0.1/exercise/v1/user_data/achievements/1
                // POST http://127.0.0.1/pack/v1/user_data/subjects/1
                // POST http://127.0.0.1/pack/v1/user_data/achievements/1
                return mPostRequestHandler.handleRequest(request);

            case BATCH_CACHE:
                /**
                 * Prefetch all required resources base on the urls.
                 * Cannot block;
                 * returns cache status.
                 */
                // GET  http://127.0.0.1/exercise/v1/chapters/1?action=cache
                // GET  http://127.0.0.1/exercise/v1/chapters/1?action=status
                // GET  http://127.0.0.1/exercise/v1/folders/1?action=cache
                // GET  http://127.0.0.1/exercise/v1/folders/1?action=status
                return mManifestRequestHandler.handleRequest(request);
            case STATE:
                return mStateRequestHandler.handleRequest(request);
            default:
                return null;
        }
    }

    @Override
    public void stop() {
    }


}
