package net.mlpnn.test.config;

import net.mlpnn.Application;
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
    private Test1 test1;

    @Autowired
    private Test2 test2;

    @Autowired
    private Test3 test3;

    @Autowired
    private Test4 test4;

    @Test
    public void testConfigInjectionSingleton() {

        Assert.assertEquals("/tmp", test1.getPath());
        Assert.assertEquals("/tmp", test2.getPath());

        test1.setPath("/tmp2");

        Assert.assertEquals("/tmp2", test2.getPath());

    }

    @Test
    public void testConfigInjectionPrototype() {

        Assert.assertEquals("/tmp", test3.getPath());
        Assert.assertEquals("/tmp", test4.getPath());

        test3.setPath("/tmp2");

        Assert.assertEquals("/tmp", test4.getPath());

    }

}
