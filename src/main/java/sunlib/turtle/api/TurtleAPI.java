package sunlib.turtle.api;

/**
 * User: fxp
 * Date: 13-8-26
 * Time: PM12:36
 */
public class TurtleAPI {

    public static ApiCategorizer newCategorizer(String type) {
        if ("sunlib-exercise".equals(type)) {
            return new SunExerciseCategorizer();
        }
        return null;
    }

}
