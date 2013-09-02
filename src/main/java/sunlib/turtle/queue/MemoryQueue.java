package sunlib.turtle.queue;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.handler.RequestHandler;
import sunlib.turtle.utils.ApiRequest;

import javax.inject.Singleton;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class MemoryQueue implements RequestQueue {

    static Logger logger = LogManager.getLogger(MemoryQueue.class.getName());
    @Inject
    RequestHandler mRequestHandler;
    private Queue<ApiRequest> mRequests;
    private Thread mThread;

    public MemoryQueue() {
        mRequests = new LinkedBlockingQueue<ApiRequest>();
        mThread = new Thread(new Looper());
        mThread.setDaemon(true);
        mThread.start();
    }

    @Override
    public void enqueueRequest(ApiRequest request) {
        mRequests.add(request);
        mRequests.notify();
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

    private class Looper implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (mRequests) {
                    if (mRequests.isEmpty()) {
                        try {
                            mRequests.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ApiRequest request = mRequests.remove();
                logger.info("processing a request,{}", request);

                while (true) {
                    try {
                        mRequestHandler.handleRequest(request);
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
    }
}
