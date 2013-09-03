package sunlib.turtle.queue;

import com.google.inject.Inject;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.SerializedConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.api.ApiCategorizer;
import sunlib.turtle.api.SunExerciseCategorizer;
import sunlib.turtle.api.TurtleAPI;
import sunlib.turtle.cache.file.FileCache;
import sunlib.turtle.handler.RequestHandler;
import sunlib.turtle.utils.ApiRequest;

import javax.inject.Singleton;
import java.io.File;

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
    @Inject
    FileCache mFileCache;


    private ObjectQueue<ApiRequest> mRequests;
    private Thread mThread;

    public MemoryQueue() {
        File file = new File("CAO");
        try {
            mRequests = new FileObjectQueue<ApiRequest>(file, new SerializedConverter<ApiRequest>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mThread = new Thread(new Looper());
        mThread.setDaemon(true);
        mThread.start();
    }

    @Override
    public void enqueueRequest(ApiRequest request) {
        synchronized (mRequests){
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

    private class Looper implements Runnable {
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
                        mRequestHandler.handleRequest(request);
                        mRequests.remove();
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
