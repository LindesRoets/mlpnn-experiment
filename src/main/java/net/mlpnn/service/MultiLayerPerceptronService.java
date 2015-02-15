package net.mlpnn.service;

import java.util.HashMap;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.dto.NetworkStatus;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lindes Roets
 */
@Service
public class MultiLayerPerceptronService {

    @Autowired
    private ApplicationConfiguration config;

    private HashMap<Long, MultiLayerPerceptronRunner> multiLayerPerceptronRunners = new HashMap<>();

    public MultiLayerPerceptronRunner getStatus(long multiLayerPerceptronRunnerId) {
        MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
        if (runner == null) {
            return null;
        }
        return runner;
    }

    public long startLearning(MultilayerPercetpronParametersForm form) {
        MultiLayerPerceptronRunner runner = new MultiLayerPerceptronRunner(config, form);
        Thread thread = new Thread(runner);
        thread.start();
        getMultiLayerPerceptronRunners().put(thread.getId(), runner);
        return thread.getId();
    }

    public HashMap<Long, MultiLayerPerceptronRunner> getMultiLayerPerceptronRunners() {
        return multiLayerPerceptronRunners;
    }

    protected void setMultiLayerPerceptronRunners(HashMap<Long, MultiLayerPerceptronRunner> multiLayerPerceptronRunners) {
        this.multiLayerPerceptronRunners = multiLayerPerceptronRunners;
    }

    public MultiLayerPerceptron removeTest(long multiLayerPerceptronRunnerId) {
        MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().remove(multiLayerPerceptronRunnerId);
        if (runner == null) {
            return null;
        }
        return runner.getPerceptron();
    }

    public MultiLayerPerceptronRunner pauseLearning(long multiLayerPerceptronRunnerId) {
        MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
        if (runner == null) {
            return null;
        }
        runner.getPerceptron().getLearningRule().pause();
        return runner;
    }

    public MultiLayerPerceptronRunner stopLearning(long multiLayerPerceptronRunnerId) {
        MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
        if (runner == null) {
            return null;
        }
        runner.getPerceptron().getLearningRule().stopLearning();
        return runner;
    }
}
