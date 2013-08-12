package sunlib.turtle.models;

/**
 * User: fxp
 * Date: 13-8-10
 * Time: PM5:07
 */
public interface Cacheable {

    public String getCacheId();

    public Long getTimeStamp();

    public Object getContent();

}
