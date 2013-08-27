package sunlib.turtle.api;

import sunlib.turtle.utils.ApiRequest;

/**
 * User: fxp
 * Date: 13-8-26
 * Time: PM12:36
 */
public class TurtleAPI {

    public static void main() {
        ApiCategorizer categorizer = TurtleAPI.newCategorizer("sunlib");
        ApiRequest.Type type = categorizer.getRequestCategory(new ApiRequest(""));
        System.out.println("Type:" + type);
    }

    public static ApiCategorizer newCategorizer(String type) {
        if ("sunlib-exercise".equals(type)) {
            return new SunExerciseCategorizer();
        }
        return null;
    }

    public static class PairMatcher {
        public String value;
        public String matcher;

        public PairMatcher(String value, String matcher) {
            this.value = value;
            this.matcher = matcher;
        }
    }

}
