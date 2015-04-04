package net.mlpnn.test;

import org.junit.Test;

import java.awt.*;
import java.io.IOException;

/**
 *
 * @author lroets
 */
public class MultiLayerPerceptronCreate {

    @Test
    public void createMLP() throws AWTException, IOException, InterruptedException {

        for (int i = 50; i < 70; i++) {
            MultiLayerPerceptronCreatePage page = new MultiLayerPerceptronCreatePage(TestContext.DRIVER);
            page.completeForm("Test - "+i, i+"", "0.75", "0.75", "Red Wine Quality");
            page.submitForm();
            Thread.sleep(1000l);
        }

    }

}
