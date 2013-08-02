package sunlib.turtle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;
import sunlib.turtle.handler.RequestHandler;
import sunlib.turtle.module.JavaModule;

import java.util.Map;

/**
 * An example of subclassing NanoHTTPD to make a custom HTTP server.
 */
public class TurtleServer extends NanoHTTPD {

    private Injector mInjector;
    private RequestHandler mRequestHandler;

    public TurtleServer() {
        super(8080);

        mInjector = Guice.createInjector(new JavaModule());
        mRequestHandler = mInjector.getInstance(RequestHandler.class);
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        System.out.println("METHOD:" + method);
        System.out.println("URL:" + uri);
        System.out.println("PARAMS:" + parms);

        ApiResponse resp = mRequestHandler.handleRequest(new ApiRequest(uri).param("id", parms.get("id")));

        return new Response(resp.getData());
    }

    public static void main(String[] args) {
        ServerRunner.run(TurtleServer.class);
    }
}
