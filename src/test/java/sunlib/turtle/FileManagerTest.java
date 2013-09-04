package sunlib.turtle;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * User: fxp
 * Date: 13-9-3
 * Time: PM9:51
 */
public class FileManagerTest extends TestCase {

    public FileManagerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }
}
