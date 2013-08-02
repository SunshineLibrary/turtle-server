package sunlib.turtle.queue;

import sunlib.turtle.ApiRequest;

import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public interface RequestQueue {

    public void enqueueRequest(ApiRequest request);

}
