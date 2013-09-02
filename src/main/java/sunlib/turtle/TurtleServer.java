package sunlib.turtle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.api.SunExerciseCategorizer;
import sunlib.turtle.api.TurtleAPI;
import sunlib.turtle.handler.RequestHandler;
import sunlib.turtle.models.CachedFile;
import sunlib.turtle.models.CachedText;
import sunlib.turtle.module.JavaModule;
import sunlib.turtle.queue.RequestQueue;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;

import java.io.FileInputStream;
import java.util.Map;

public class TurtleServer extends NanoHTTPD {

    public static SunExerciseCategorizer categorizer = (SunExerciseCategorizer) TurtleAPI.newCategorizer("sunlib-exercise");
    static Logger logger = LogManager.getLogger(TurtleServer.class.getName());
    private RequestHandler mRequestHandler;
    private RequestQueue mRequestQueue;

    public TurtleServer() {
        super(3001);
        try {
            Injector mInjector = Guice.createInjector(new JavaModule());
            mRequestHandler = mInjector.getInstance(RequestHandler.class);
            mRequestQueue = mInjector.getInstance(RequestQueue.class);
        } catch (Exception e) {
            e.printStackTrace();
            this.stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        mRequestHandler.stop();
        mRequestQueue.stop();
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
        ApiResponse resp = null;
        Response ret = null;
        try {
            ApiRequest req = new ApiRequest("127.0.0.1", -1, uri)
                    .method(method)
                    .params(parms)
                    .headers(headers);
            req.type = categorizer.getType(method, uri, parms);
            logger.trace("request,{}", req);
            if (req.type == null) {
                ret = new Response(Response.Status.BAD_REQUEST,
                        MIME_PLAINTEXT,
                        "{err:'request type not recognized'}");
            } else {
                resp = mRequestHandler.handleRequest(req);
                if (resp == null || !resp.success || resp.getData() == null) {
                    ret = new Response(
                            Response.Status.INTERNAL_ERROR,
                            MIME_JSON,
                            "{err:'internal error'}");
                } else if (resp.getData() instanceof CachedText) {
                    String content = (String) resp.getData().getContent();
                    content = (StringUtils.isEmpty(content)) ? "" : content;
                    if (parms.get("callback") != null) {
                        content = parms.get("callback") + "(" + content + ")";
                    }
                    ret = new Response(
                            Response.Status.OK,
                            MIME_JSON,
                            content);
                } else if (resp.getData() instanceof CachedFile) {
                    ret = new Response(
                            Response.Status.OK,
                            resp.getContentType(),
                            new FileInputStream(((CachedFile) resp.getData()).file)
                    );
                } else {
                    ret = new Response(
                            Response.Status.BAD_REQUEST,
                            MIME_JSON,
                            "{err:'bad request'}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = new Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "internal error");
        }

        return ret;
//        return null;
    }
}
