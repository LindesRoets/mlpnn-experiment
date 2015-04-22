package net.mlpnn.test.automation;

import java.util.concurrent.TimeUnit;
import org.junit.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author lroets
 */
public class initTest {

    public initTest() {
    }

    @Test
    public void startSuite() {
        Assert.assertNotNull("We need an intantiated driver.", TestContext.DRIVER);
    }

    @BeforeClass
    public static void setUpSuite() {
        TestContext.DRIVER = new FirefoxDriver();
        TestContext.DRIVER.manage().timeouts().implicitlyWait(4000, TimeUnit.MILLISECONDS);
        TestContext.DRIVER.manage().timeouts().pageLoadTimeout(120000, TimeUnit.MILLISECONDS);

        String baseURL = System.getenv("BASE_URL");
        if (baseURL != null) {
            TestContext.BASE_URL = baseURL;
        }
    }
}
