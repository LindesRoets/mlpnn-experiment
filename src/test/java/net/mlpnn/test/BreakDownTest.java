package net.mlpnn.test;

import org.junit.AfterClass;
import org.junit.Test;

/**
 *
 * @author lroets
 */
public class BreakDownTest {

    public BreakDownTest() {
    }

    @Test
    public void endSuite() {
        System.out.println(TestContext.printContextInfo());
    }

    @AfterClass
    public static void breakDownSuite() {
        TestContext.quitDriver();
    }
}
