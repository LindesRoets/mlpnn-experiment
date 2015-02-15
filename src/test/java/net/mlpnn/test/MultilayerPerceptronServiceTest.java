package net.mlpnn.test;

import net.mlpnn.Application;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.enums.LearningStatus;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import net.mlpnn.service.MultiLayerPerceptronRunner;
import net.mlpnn.service.MultiLayerPerceptronService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author lindes.roets
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class MultilayerPerceptronServiceTest {

    @Autowired
    private ApplicationConfiguration config;

    int test = 0;

    private final Logger LOGGER = LoggerFactory.getLogger(MultilayerPerceptronServiceTest.class);

    @Autowired
    private MultiLayerPerceptronService multiLayerPerceptronService;

    @Test
    public void testLearningStatus() throws Exception {

        MultilayerPercetpronParametersForm form = new MultilayerPercetpronParametersForm();
        form.setDataSetName(DataSetInfo.WINE_QUALITY_RED.name());
        form.setLearningRate(0.5);
        form.setMomentum(0.5);
        form.setNeuronCount(110);

        Long id = multiLayerPerceptronService.startLearning(form);

        Thread.sleep(500);

        MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getStatus(id);

        while (runner.calculateLearningStatus().equals(LearningStatus.RUNNING)) {
            runner = multiLayerPerceptronService.getStatus(id);
        }

        Assert.assertEquals("We are expecting learning to be stopped.", LearningStatus.STOPPED, runner.calculateLearningStatus().STOPPED);

        MultiLayerPerceptron perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNotNull("We are expecting to get the perceptron back from the removal action.", perceptron);

        perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNull("We are expecting the perceptron to be null since it has already been removed.", perceptron);

    }

    @Test
    public void testLearningTwoPerceptrons() throws Exception {

        MultilayerPercetpronParametersForm form = new MultilayerPercetpronParametersForm();
        form.setDataSetName(DataSetInfo.WINE_QUALITY_RED.name());
        form.setLearningRate(0.5);
        form.setMomentum(0.5);
        form.setNeuronCount(110);

        Long id = multiLayerPerceptronService.startLearning(form);
        Long id2 = multiLayerPerceptronService.startLearning(form);

        Thread.sleep(500);

        MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getStatus(id);
        MultiLayerPerceptronRunner runner2 = multiLayerPerceptronService.getStatus(id2);

        //make sure first perceptron finish learning
        while (runner.calculateLearningStatus().equals(LearningStatus.RUNNING)) {
            runner = multiLayerPerceptronService.getStatus(id);
        }

        //make sure second perceptron finish learning
        while (runner2.calculateLearningStatus().equals(LearningStatus.RUNNING)) {
            runner2 = multiLayerPerceptronService.getStatus(id2);
        }

        runner = multiLayerPerceptronService.getStatus(id);
        runner2 = multiLayerPerceptronService.getStatus(id2);
        Assert.assertEquals("We are expecting learning to be stopped.", LearningStatus.STOPPED, runner.calculateLearningStatus().STOPPED);
        Assert.assertEquals("We are expecting learning to be stopped.", LearningStatus.STOPPED, runner2.calculateLearningStatus().STOPPED);

        Assert.assertEquals("Expecting 2 perceptrons", 2, multiLayerPerceptronService.getMultiLayerPerceptronRunners().size());

        MultiLayerPerceptron perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNotNull("We are expecting to get the perceptron back from the removal action.", perceptron);

        perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNull("We are expecting the perceptron to be null since it has already been removed.", perceptron);

    }

    @Test
    public void testRemovingPerceptron() throws InterruptedException {

        MultilayerPercetpronParametersForm form = new MultilayerPercetpronParametersForm();
        form.setDataSetName(DataSetInfo.WINE_QUALITY_RED.name());
        form.setLearningRate(0.5);
        form.setMomentum(0.5);
        form.setNeuronCount(110);

        Long id = multiLayerPerceptronService.startLearning(form);

        Thread.sleep(500);

        MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getStatus(id);

        while (runner.calculateLearningStatus().equals(LearningStatus.RUNNING)) {
            runner = multiLayerPerceptronService.getStatus(id);
        }

        Assert.assertEquals("We are expecting learning to be stopped.", LearningStatus.STOPPED, runner.calculateLearningStatus().STOPPED);

        MultiLayerPerceptron perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNotNull("We are expecting to get the perceptron back from the removal action.", perceptron);

        perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNull("We are expecting the perceptron to be null since it has already been removed.", perceptron);

    }

    @Test
    public void testPausingLearning() throws InterruptedException {

        MultilayerPercetpronParametersForm form = new MultilayerPercetpronParametersForm();
        form.setDataSetName(DataSetInfo.WINE_QUALITY_RED.name());
        form.setLearningRate(0.5);
        form.setMomentum(0.5);
        form.setNeuronCount(110);

        Long id = multiLayerPerceptronService.startLearning(form);

        Thread.sleep(500);

        MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getStatus(id);

        if (runner.calculateLearningStatus().equals(LearningStatus.RUNNING)) {
            runner = multiLayerPerceptronService.pauseLearning(id);
            Assert.assertEquals("We are expecting the learning to be in a paused state", LearningStatus.PAUSED, runner.calculateLearningStatus());

        }

        MultiLayerPerceptron perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNotNull("We are expecting to get the perceptron back from the removal action.", perceptron);

        perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNull("We are expecting the perceptron to be null since it has already been removed.", perceptron);

    }

    @Test
    public void testStopLearning() throws InterruptedException {

        MultilayerPercetpronParametersForm form = new MultilayerPercetpronParametersForm();
        form.setDataSetName(DataSetInfo.WINE_QUALITY_RED.name());
        form.setLearningRate(0.5);
        form.setMomentum(0.5);
        form.setNeuronCount(110);

        Long id = multiLayerPerceptronService.startLearning(form);

        Thread.sleep(500);

        MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getStatus(id);

        while (runner.calculateLearningStatus().equals(LearningStatus.RUNNING)) {
            runner = multiLayerPerceptronService.stopLearning(id);
            Assert.assertEquals("We are expecting the learning to be in a paused state", LearningStatus.STOPPED, runner.calculateLearningStatus());
        }

        Assert.assertEquals("We are expecting learning to be stopped.", LearningStatus.STOPPED, runner.calculateLearningStatus().STOPPED);

        MultiLayerPerceptron perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNotNull("We are expecting to get the perceptron back from the removal action.", perceptron);

        perceptron = multiLayerPerceptronService.removeTest(id);

        Assert.assertNull("We are expecting the perceptron to be null since it has already been removed.", perceptron);

    }

}
