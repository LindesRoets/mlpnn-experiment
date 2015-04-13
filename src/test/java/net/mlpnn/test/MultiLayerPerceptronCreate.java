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

        for (int i = 1; i < 100; i++) {
            MultiLayerPerceptronCreatePage page = new MultiLayerPerceptronCreatePage(TestContext.DRIVER);
            page.completeForm("Iris - " + i, i + "", "0.75", "0.75", "Iris");
            page.submitForm();
            Thread.sleep(100l);
        }
//
//        for (int i = 200; i < 400; i++) {
//            MultiLayerPerceptronCreatePage page = new MultiLayerPerceptronCreatePage(TestContext.DRIVER);
//            page.completeForm("Seeds - " + i, i + "", "0.75", "0.75", "Seeds");
//            page.submitForm();
//            Thread.sleep(100l);
//        }

//        for (int i = 970; i < 1001; i++) {
//            MultiLayerPerceptronCreatePage page = new MultiLayerPerceptronCreatePage(TestContext.DRIVER);
//            page.completeForm("Red Wine Quality - " + i, i + "", "0.75", "0.75", "Red Wine Quality");
//            page.submitForm();
//            Thread.sleep(100l);
//        }

//        for (int i = 200; i < 400; i++) {
//            MultiLayerPerceptronCreatePage page = new MultiLayerPerceptronCreatePage(TestContext.DRIVER);
//            page.completeForm("White Wine Quality - " + i, i + "", "0.75", "0.75", "White Wine Quality");
//            page.submitForm();
//            Thread.sleep(100l);
//        }
//
//        for (int i = 200; i < 400; i++) {
//            MultiLayerPerceptronCreatePage page = new MultiLayerPerceptronCreatePage(TestContext.DRIVER);
//            page.completeForm("Glass - " + i, i + "", "0.75", "0.75", "Glass");
//            page.submitForm();
//            Thread.sleep(100l);
//        }
//
//        for (int i = 200; i < 400; i++) {
//            MultiLayerPerceptronCreatePage page = new MultiLayerPerceptronCreatePage(TestContext.DRIVER);
//            page.completeForm("Skin Segmentation - " + i, i + "", "0.75", "0.75", "Skin Segmentation");
//            page.submitForm();
//            Thread.sleep(100l);
//        }
//
//        for (int i = 200; i < 400; i++) {
//            MultiLayerPerceptronCreatePage page = new MultiLayerPerceptronCreatePage(TestContext.DRIVER);
//            page.completeForm("Dermatology - " + i, i + "", "0.75", "0.75", "Dermatology");
//            page.submitForm();
//            Thread.sleep(100l);
//        }
//
//        for (int i = 200; i < 400; i++) {
//            MultiLayerPerceptronCreatePage page = new MultiLayerPerceptronCreatePage(TestContext.DRIVER);
//            page.completeForm("Ionosphere - " + i, i + "", "0.75", "0.75", "Ionosphere");
//            page.submitForm();
//            Thread.sleep(100l);
//        }

    }

}
