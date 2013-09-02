package sunlib.turtle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import sunlib.turtle.module.JavaModule;
import sunlib.turtle.queue.MemoryQueue;
import sunlib.turtle.utils.ApiRequest;

import java.sql.SQLException;

/**
 * User: fxp
 * Date: 13-8-30
 * Time: PM3:46
 */
public class QueueTest {

    MemoryQueue queue = null;

    @Before
    public void before() throws SQLException {
        Injector mInjector = Guice.createInjector(new JavaModule());
        queue = mInjector.getInstance(MemoryQueue.class);
    }

    @Test
    public void testEnqueue() throws Exception {
        ApiRequest request = new ApiRequest("127.0.0.1", 3000, "/exercise/v1/lessons/10111");
        queue.enqueueRequest(request);

    }
}
