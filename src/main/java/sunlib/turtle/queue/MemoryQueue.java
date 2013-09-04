package sunlib.turtle.queue;

import com.google.inject.Inject;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.SerializedConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.cache.Cache;
import sunlib.turtle.handler.RequestHandler;
import sunlib.turtle.models.CachedManifest;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;

import javax.inject.Singleton;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class MemoryQueue implements RequestQueue, Runnable {

    static Logger logger = LogManager.getLogger(MemoryQueue.class.getName());
    @Inject
    RequestHandler mRequestHandler;
    @Inject
    Cache cache;
    private ObjectQueue<ApiRequest> mRequests;
    private Thread mThread;

    public MemoryQueue() {
        File file = new File("task.db");
        try {
            mRequests = new FileObjectQueue<ApiRequest>(file, new SerializedConverter<ApiRequest>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (mRequests) {
                if (mRequests.size() == 0) {
                    try {
                        mRequests.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            ApiRequest request = mRequests.peek();
            logger.info("processing a request,{}", request);
            while (true) {
                try {
//                        mRequestHandler.handleRequest(request);
                    logger.info("do something with this request");
                    if (ApiRequest.Type.GET.equals(request.type)
                            || ApiRequest.Type.GET_CACHE.equals(request.type)) {
                        ApiResponse resp = request.get();
                        cache.put(resp.getData());
                        if (!StringUtils.isEmpty(request.manifestId)) {
                            CachedManifest manifest = (CachedManifest) cache.get(request.manifestId);
                            //TODO: Do something
                            manifest.left--;
                            if (manifest.left == 0) {
                                manifest.is_cached = true;
                            }
                            cache.put(manifest);
                        }
                    } else if (ApiRequest.Type.POST_CACHE.equals(request.type)) {
                        ApiResponse resp = request.post();
                        if (resp.success) {
                            cache.put(resp.getData());
                        }
                    }
                    mRequests.remove();
                    logger.info("task completed,{}", request);
                    Thread.sleep(5000);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public void enqueueRequest(ApiRequest request) {
        synchronized (mRequests) {
            mRequests.add(request);
            mRequests.notify();
        }
    }

    @Override
    public void stop() {
        try {
            //TODO safe close mThead
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
