package sunlib.turtle.api;

/**
 * Created with IntelliJ IDEA.
 * User: Bowen
 * Date: 13-8-15
 * Time: PM3:50
 * To change this template use File | Settings | File Templates.
 */
public interface ApiProvider {

    public ApiRequestTypeMatcher getRequestMatcher();
    
}
