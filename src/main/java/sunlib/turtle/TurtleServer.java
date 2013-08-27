package sunlib.turtle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunlib.turtle.handler.RequestHandler;
import sunlib.turtle.models.CachedFile;
import sunlib.turtle.models.CachedText;
import sunlib.turtle.module.JavaModule;
import sunlib.turtle.queue.RequestQueue;
import sunlib.turtle.services.HeartbeatClient;
import sunlib.turtle.utils.ApiRequest;
import sunlib.turtle.utils.ApiResponse;

import java.io.InputStream;
import java.util.Map;

public class TurtleServer extends NanoHTTPD {

    static Logger logger = LogManager.getLogger(TurtleServer.class.getName());
    private Injector mInjector;
    private RequestHandler mRequestHandler;
    private RequestQueue mRequestQueue;

    public TurtleServer() {
        super(30000);
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
        new Thread(new HeartbeatClient()).start();

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
            logger.trace("request,{}", req);
            if (req.type == null) {
                ret = new Response(Response.Status.BAD_REQUEST,
                        MIME_PLAINTEXT,
                        "request type not determined");
            } else {
                resp = mRequestHandler.handleRequest(req);
                if (resp.getData() == null) {
                    ret = new Response(
                            Response.Status.INTERNAL_ERROR,
                            MIME_PLAINTEXT,
                            "internal error");
                } else if (resp.getData() instanceof CachedText) {
                    String content = (String) resp.getData().getContent();
                    content = (StringUtils.isEmpty(content)) ? "" : content;
                    if (parms.get("callback") != null) {
                        content = parms.get("callback") + "(" + content + ")";
                    }
                    ret = new Response(
                            Response.Status.OK,
                            MIME_PLAINTEXT,
                            content);
                } else if (resp.getData() instanceof CachedFile) {
                    String mime = NanoHTTPD.MIME_DEFAULT_BINARY;
                    if (req.uri.contains("pdf")) {
                        mime = "application/pdf";
                    }
                    ret = new Response(
                            Response.Status.OK,
                            mime,
                            (InputStream) resp.getData().getContent()
                    );
//                    ret = new Response(
//                            Response.Status.OK,
//                            NanoHTTPD.MIME_DEFAULT_BINARY,
//                            new FileInputStream("/Users/fxp/Downloads/Skype_6.7.59.373.pdf")
//                    );
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
