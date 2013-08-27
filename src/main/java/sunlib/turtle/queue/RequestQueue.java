package sunlib.turtle.queue;

import sunlib.turtle.utils.ApiRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

public interface RequestQueue {

    public void enqueueRequest(ApiRequest request);

    public void stop();
}
