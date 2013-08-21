package sunlib.turtle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.handler.RequestHandler;
import sunlib.turtle.models.ApiRequest;
import sunlib.turtle.models.ApiResponse;
import sunlib.turtle.models.CachedFile;
import sunlib.turtle.models.CachedText;
import sunlib.turtle.module.JavaModule;
import sunlib.turtle.queue.RequestQueue;

import java.io.InputStream;
import java.util.Map;

public class TurtleServer extends NanoHTTPD {

    static Logger logger = LogManager.getLogger(TurtleServer.class.getName());
    private Injector mInjector;
    private RequestHandler mRequestHandler;
    private RequestQueue mRequestQueue;

    public TurtleServer() {
        super(8080);
        try {
            mInjector = Guice.createInjector(new JavaModule());
            mRequestHandler = mInjector.getInstance(RequestHandler.class);
            mRequestQueue = mInjector.getInstance(RequestQueue.class);
        } catch (Exception e) {
            e.printStackTrace();
            this.stop();
        }
    }

    public static void main(String[] args) {
        ServerRunner.run(TurtleServer.class);

    }

    @Override
    public void stop() {
        super.stop();
        mRequestHandler.stop();
        mRequestQueue.stop();
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        ApiResponse resp = null;
        Response ret = null;
        try {
            ApiRequest req = new ApiRequest(uri, method, header, parms, files);
            if (req.type == null) {
                ret = new Response(Response.Status.BAD_REQUEST,
                        MIME_PLAINTEXT,
                        "bad request");
            } else {
                resp = mRequestHandler.handleRequest(req);
                if (resp.getData() == null) {
                    ret = new Response(
                            Response.Status.INTERNAL_ERROR,
                            MIME_PLAINTEXT,
                            "internal error");
                } else if (resp.getData() instanceof CachedText) {
                    ret = new Response(
                            Response.Status.OK,
                            MIME_PLAINTEXT,
                            (String) resp.getData().getContent());
                } else if (resp.getData() instanceof CachedFile) {
                    ret = new Response(
                            Response.Status.OK,
                            NanoHTTPD.MIME_DEFAULT_BINARY,
                            (InputStream) resp.getData().getContent()
                    );
                } else {
                    ret = new Response(
                            Response.Status.BAD_REQUEST,
                            MIME_PLAINTEXT,
                            "bad request");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = new Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "internal error");
        }
        return ret;
    }
}
