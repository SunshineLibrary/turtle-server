package sunlib.turtle.services;

import org.apache.commons.lang3.StringUtils;
import sunlib.turtle.storage.KeyValues;
import sunlib.turtle.utils.WS;

/**
 * User: fxp
 * Date: 13-8-26
 * Time: AM11:26
 */
public class HeartbeatClient implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    String resp = (String) WS.url("/ping").get().getData().getContent();
                    if (!StringUtils.isEmpty(resp)) {
                        KeyValues.GLOBAL.put("/state/online", "{'online':true}");
                    }
                } catch (Exception e) {
                    KeyValues.GLOBAL.put("/state/online", "{'online':false}");
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            KeyValues.GLOBAL.put("/state/online", "{'online':false}");
        }
    }

}
