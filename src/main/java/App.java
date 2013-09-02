import fi.iki.elonen.ServerRunner;
import sunlib.turtle.TurtleServer;

/**
 * User: fxp
 * Date: 13-8-28
 * Time: PM9:24
 */
public class App {

    public static void main(String[] args) {
        ServerRunner.run(TurtleServer.class);
//        new Thread(new HeartbeatClient()).start();

    }
}

