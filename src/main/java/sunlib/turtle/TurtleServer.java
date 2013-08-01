package sunlib.turtle;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;

import java.util.Map;

/**
 * An example of subclassing NanoHTTPD to make a custom HTTP server.
 */
public class TurtleServer extends NanoHTTPD {

    public static Proxy proxy = Proxy.MockProxy;

    public TurtleServer() {
        super(8080);
    }

    public static void main(String[] args) {
        ServerRunner.run(TurtleServer.class);
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        System.out.println("METHOD:" + method);
        System.out.println("URL:" + uri);
        System.out.println("PARAMS:" + parms);

        ApiResponse resp = proxy.doRequest(new ApiRequest(uri)
                .param("id", parms.get("id")));

        return new Response(resp.getData());
//        return new Response("turtle is born for web app running offline");
    }
}
