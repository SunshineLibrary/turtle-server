package sunlib.turtle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;
import sunlib.turtle.handler.RequestHandler;
import sunlib.turtle.models.ApiRequest;
import sunlib.turtle.models.ApiResponse;
import sunlib.turtle.module.JavaModule;
import sunlib.turtle.queue.RequestQueue;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class TurtleServer extends NanoHTTPD {

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
            resp = mRequestHandler.handleRequest(req);
            if (resp.getData() == null) {
                ret = new Response(
                        Response.Status.INTERNAL_ERROR,
                        MIME_PLAINTEXT,
                        "internal error");
            } else if (resp.getData() instanceof String) {
                ret = new Response(
                        Response.Status.OK,
                        MIME_PLAINTEXT,
                        (String) resp.getData());
            } else if (resp.getData() instanceof File) {
                ret = new Response(
                        Response.Status.OK,
                        NanoHTTPD.MIME_DEFAULT_BINARY,
                        new FileInputStream((File) resp.getData())
                );
            } else {
                ret = new Response(
                        Response.Status.BAD_REQUEST,
                        MIME_PLAINTEXT,
                        "bad request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = new Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "internal error");
        }
        return ret;
    }
}
