package sunlib.turtle.module;

import sunlib.turtle.models.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: fxp
 * Date: 13-8-5
 * Time: PM5:37
 */
public class MaterialFactory {

    public static final List<UriExtractor> extractors = new ArrayList<UriExtractor>() {{
        this.add(new ExerciseMaterialExtractor());
        this.add(new ExerciseUserdataExtractor());
    }};

    public static Material get(String uri, Map<String, String> params) {
        Material ret = null;
        for (UriExtractor extractor : extractors) {
            Material m = extractor.extract(uri);
            if (m != null) {
                ret = m;
                break;
            }
        }
        ret.params= params;
        return ret;
    }

    public static abstract class UriExtractor {

        public abstract String getPattern();

        public Material extract(String uri) {
            Pattern p = Pattern.compile(getPattern());
            Matcher m = p.matcher(uri);
            if (m.find()) {
                return extractParts(m);
            } else {
                return null;
            }
        }

        protected abstract Material extractParts(Matcher m);

    }

    public static class ExerciseMaterialExtractor extends UriExtractor {

        @Override
        public String getPattern() {
            return "/exercise/1/(lesson|chapter|)/([^/]+)";
        }

        @Override
        protected Material extractParts(Matcher m) {
            return new Material(m.group(1), m.group(2));
        }
    }

    public static class ExerciseUserdataExtractor extends UriExtractor {

        @Override
        public String getPattern() {
            return "/exercise/1/userdata//([^/]+)";
        }

        @Override
        protected Material extractParts(Matcher m) {
            return new Material("userdata", m.group(1));
        }
    }

}
