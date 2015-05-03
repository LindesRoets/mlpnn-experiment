package net.mlpnn.test.config;

import net.mlpnn.Application;
import net.mlpnn.ApplicationConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Lindes Roets
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ConfigurationTest {
	
    @Autowired
    private ApplicationConfiguration config;
	
    @Autowired
    private Test1 test1;

    @Autowired
    private Test2 test2;

    @Autowired
    private Test3 test3;

    @Autowired
    private Test4 test4;

    @Test
    public void testConfigInjectionSingleton() {
		System.out.println("Config path for data file: "+config.getDatasetFilePath());

        Assert.assertEquals("data", test1.getPath());
        Assert.assertEquals("data", test2.getPath());

        test1.setPath("/tmp2");

        Assert.assertEquals("/tmp2", test2.getPath());

    }

    @Test
    public void testConfigInjectionPrototype() {

        Assert.assertEquals("data", test3.getPath());
        Assert.assertEquals("data", test4.getPath());

        test3.setPath("/tmp2");

		//Testing that setting test3 path did not change test4 path since it is not singletons
        Assert.assertEquals("data", test4.getPath());

    }

}
