package sunlib.turtle.queue;

import com.google.inject.Inject;
import sunlib.turtle.ApiRequest;
import sunlib.turtle.handler.RequestHandler;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

public class MemoryQueue implements RequestQueue {

    private Queue<ApiRequest> mRequests;
    @Inject RequestHandler mRequestHandler;

    public MemoryQueue() {
        mRequests = new LinkedBlockingQueue<ApiRequest>();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(!mRequests.isEmpty()) {
                            ApiRequest request = mRequests.remove();
                            mRequestHandler.fetchResponse(request);
                        }
                        try {
                            mRequests.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
        ).start();
    }

    @Override
    public void enqueueRequest(ApiRequest request) {
        mRequests.add(request);
        mRequests.notify();
    }
}
