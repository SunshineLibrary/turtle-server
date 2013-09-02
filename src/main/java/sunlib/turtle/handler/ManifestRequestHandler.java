package sunlib.turtle.handler;

import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.models.CachedManifest;
import sunlib.turtle.models.CachedText;
import sunlib.turtle.models.ManifestItem;
import sunlib.turtle.models.ManifestList;
import sunlib.turtle.queue.RequestQueue;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;
import sunlib.turtle.utils.SunWS;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class ManifestRequestHandler implements RequestHandler {

    static Logger logger = LogManager.getLogger(ManifestRequestHandler.class.getName());
    static Map<String, CachedManifest> cache = new HashMap<String, CachedManifest>();
    @Inject
    Cache mCache;
    @Inject
    RequestQueue queue;

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        logger.trace("urls request,{}", request.toString());
        // get urls from act=status API

        ManifestResponse ret = null;

        if ("cache".equals(request.params.get("act"))) {
            CachedManifest cached = cache.get(request.getCacheId());
            if (cached == null) {
                //TODO add tasks to queue and download one by one
                // Get urls
                ManifestList manifest = new Gson().fromJson((String) SunWS.url(
                        request.uri)
                        .param("act", "status")
                        .param("ts", "1")
                        .get().getData().getContent()
                        , ManifestList.class);
                cache.put(request.getCacheId(), new CachedManifest(request.getCacheId(), manifest.manifest));
                new Thread(new ManifestDownloadThread(request.getCacheId())).start();
                CachedManifest startManifest = cache.get(request.getCacheId());
                ret = new ManifestResponse(startManifest.is_cached, startManifest.progress);
            } else {
                ret = new ManifestResponse(cached.is_cached, cached.progress);
            }
        } else if ("status".equals(request.params.get("act"))) {
            if (cache.get(request.getCacheId()) != null) {
                CachedManifest cached = cache.get(request.getCacheId());
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

    public static class ManifestDownloadThread implements Runnable {

        public String manifestId;
        @Inject
        Cache mCache;

        public ManifestDownloadThread(String manifestId) {
            this.manifestId = manifestId;
        }

        @Override
        public void run() {
            CachedManifest manifest = cache.get(manifestId);
            if (manifest != null) {
                int leftCount = manifest.manifest.size();
                for (ManifestItem item : manifest.manifest) {
                    try {
                        ApiRequest req = SunWS.url(item.url).param("ts", item.ts);
//                        ApiResponse resp = mRequestHandler.handleRequest(req);
                        logger.info("cached file,{}", item);
                        leftCount--;
                        if (leftCount == 0) {
                            manifest.is_cached = true;
                        }
                        manifest.progress = 100 - leftCount * (100 / manifest.manifest.size());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

//                    try {
//                        while (!manifest.is_cached) {
//                            manifest.progress += 20;
//                            Thread.sleep(1000);
//                            if (manifest.progress == 100) {
//                                manifest.is_cached = true;
//                            }
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
                }
            }
        }
    }

    public static class ManifestResponse {
        public Boolean is_cached;
        public Integer progress;
        public List<ManifestItem> manifest;

        public ManifestResponse() {
        }

        public ManifestResponse(Boolean is_cached, Integer progress) {
            this.is_cached = is_cached;
            this.progress = progress;
        }
    }
}
