package sunlib.turtle.handler;

import org.apache.commons.io.FileUtils;
import sunlib.turtle.models.ApiRequest;
import sunlib.turtle.models.ApiResponse;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-2
 */

@Singleton
public class ProxyRequestHandler implements RequestHandler {

    @Override
    public ApiResponse handleRequest(ApiRequest request) {
        return (ApiResponse) fetchResponse(request);
    }

    @Override
    public Object fetchResponse(ApiRequest request) {
        Object content = null;
        //TODO: change to sync http request
        //TODO for testing /exercise/1/lesson/123 is the json, others redirect to FileCache
        if ("123".equals(request.target.id)) {
            try {
                content = FileUtils.readFileToString(new File("mockData/lesson.json"));
//                FileUtils.
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ApiResponse(true, content);
        } else {
            return null;
        }
    }

    @Override
    public void stop() {

    }
}
