package net.mlpnn.test.util;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lindes Roets
 */
public class MathTest {

    @Test
    public void testMathRound() {

        int a = 7;

        long b = Math.round(a / 2.0);

        int c = Math.round(a/2);
        
        Assert.assertEquals(4, b);
        
        Assert.assertEquals(a, b + c);
    }

}
