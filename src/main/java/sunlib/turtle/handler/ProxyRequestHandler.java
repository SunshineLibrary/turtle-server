package sunlib.turtle.handler;

import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import org.apache.commons.io.FileUtils;
import sunlib.turtle.models.*;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
//        CachedFile file = null;
        //TODO: change to sync http request
        //TODO for testing /exercise/1/lesson/123 is the json, others redirect to FileCache
//        try {
//            InputStream in = FileUtils.openInputStream(new File("mockData/lesson.json"));
//            file = new CachedFile(request.getCacheId(), in);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Cacheable ret = null;
        BasicHttpClient httpClient = new BasicHttpClient("http://192.168.3.100:3000");
        HttpResponse httpResponse = httpClient.get(request.uri, null);
        List<String> contentTypes = httpResponse.getHeaders().get("content-type");
        if (contentTypes != null) {
            String type = contentTypes.get(0);
            if (type.contains("image") ||
                    type.contains("audio") ||
                    type.contains("video")) {
                // A file need cache
                System.out.println("A cache file:" + type);
                try {
                    InputStream in = FileUtils.openInputStream(new File("mockData/lesson.json"));
                    ret = new CachedFile(request.getCacheId(), in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Some text need cache
                String text = httpResponse.getBodyAsString();
                System.out.println("A cache text with contenttype:" + text);
                ret = new CachedText(request.getCacheId(), text);
            }
        } else {
            // Some text need cache
            String text = httpResponse.getBodyAsString();
            System.out.println("A cache text:" + text);
            ret = new CachedText(request.getCacheId(), text);
        }

        return new ApiResponse(true, ret);
    }

    @Override
    public void stop() {

    }
}
