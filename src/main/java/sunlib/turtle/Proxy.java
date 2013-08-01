package sunlib.turtle;

/**
 * User: fxp
 * Date: 13-8-1
 * Time: PM3:08
 */
public enum Proxy {
    MockProxy, LocalServer;

    public ApiResponse doRequest(ApiRequest request) {
        return new ApiResponse();
    }

}
