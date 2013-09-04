package sunlib.turtle.handler;

import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.models.CachedManifest;
import sunlib.turtle.models.CachedText;
import sunlib.turtle.models.ManifestItem;
import sunlib.turtle.queue.RequestQueue;
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
public class ManifestRequestHandler implements RequestHandler {

    static Logger logger = LogManager.getLogger(ManifestRequestHandler.class.getName());
    @Inject
    RequestQueue queue;
    //    static Map<String, CachedManifest> cache = new HashMap<String, CachedManifest>();
    @Inject
    Cache mCache;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        logger.trace("urls request,{}", request.toString());

        ManifestResponse ret = null;

        if ("cache".equals(request.params.get("act"))) {
            CachedManifest cached = (CachedManifest) mCache.get(request.getCacheId());
            if (cached == null) {
                //TODO add tasks to queue and download one by one
                // Get urls
                CachedManifest manifest = null;
                try {
                    manifest = new Gson().fromJson((String) SunWS.url(
                            request.uri)
                            .param("act", "status")
                            .param("ts", request.params.get("ts"))
                            .get().getData().getContent()
                            , CachedManifest.class);
                    manifest.key = request.getCacheId();
                    manifest.total = manifest.manifest.size();
                    manifest.left = manifest.total;
                    mCache.put(manifest);

                    CachedManifest startManifest = (CachedManifest) mCache.get(request.getCacheId());

                    for (ManifestItem item : manifest.manifest) {
                        ApiRequest req = SunWS.url(item.url).param("ts", item.ts);
                        req.manifestId = request.getCacheId();
                        req.type = ApiRequest.Type.GET_CACHE;
                        queue.enqueueRequest(req);
                        logger.info("enqueue manifest task,{}", req);
                    }

                    ret = new ManifestResponse(startManifest.is_cached, startManifest.progress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ret = new ManifestResponse(cached.is_cached, cached.progress);
            }
        } else if ("status".equals(request.params.get("act"))) {
            if (mCache.get(request.getCacheId()) != null) {
                CachedManifest cached = (CachedManifest) mCache.get(request.getCacheId());
                ret = new ManifestResponse();
                ret.is_cached = cached.is_cached;
                ret.progress = cached.progress;
            } else {
                ret = new ManifestResponse();
            }
        }
        return new ApiResponse(true,
                new CachedText(request.getCacheId(), new Gson().toJson(ret))
        );
    }

    @Override
    public void stop() {

    }

    public static class ManifestResponse {
        public Boolean is_cached;
        public Integer progress;

        public ManifestResponse() {
        }

        public ManifestResponse(Boolean is_cached, Integer progress) {
            this.is_cached = is_cached;
            this.progress = progress;
        }
    }
}
