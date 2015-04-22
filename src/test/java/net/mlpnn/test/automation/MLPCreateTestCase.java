package net.mlpnn.test.automation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author lroets
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    initTest.class,
    MultiLayerPerceptronCreate.class,
    BreakDownTest.class,})
public class MLPCreateTestCase {
}
