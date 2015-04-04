package net.mlpnn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.dto.NetworkStatusDTO;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lindes Roets
 */
@Service
public class MultiLayerPerceptronService {

    private final Logger LOGGER = LoggerFactory.getLogger(MultiLayerPerceptronService.class);

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

    public MultiLayerPerceptronRunner resumeLearning(long multiLayerPerceptronRunnerId) {
        MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
        if (runner == null) {
            return null;
        }
        runner.getPerceptron().getLearningRule().resume();
        return runner;
    }

    public List<NetworkStatusDTO> getPerceptronStatuses() throws InterruptedException {
        List<NetworkStatusDTO> statuses = new ArrayList<>();
        if (this.getMultiLayerPerceptronRunners() != null && this.getMultiLayerPerceptronRunners().size() > 0) {
            HashMap<Long, MultiLayerPerceptronRunner> runners = this.getMultiLayerPerceptronRunners();
            Set<Long> ids = runners.keySet();
            Long[] idss = ids.toArray(new Long[ids.size()]);
            List<Long> mlpIds = Arrays.asList(idss);
            Collections.sort(mlpIds);
            for (Long mlpId : mlpIds) {
                MultiLayerPerceptronRunner runner = runners.get(mlpId);
                NetworkStatusDTO dto = new NetworkStatusDTO();
                int count = 0;
                while (runner.getPerceptron() == null) {
                    // each of these runners is handled in a different thread. 
                    // Therefore this Thread has to wait for a new training thread to intantiate its perceptron
                    Thread.sleep(500);
                    LOGGER.info("We're waiting ....");
                    count++;
                    if (count > 10) {
                        break;
                    }
                }

                if(runner.getPerceptron()==null){
                    dto.setCurrentIteration(-1);
                }else{
                   dto.setCurrentIteration(runner.getPerceptron().getLearningRule().getCurrentIteration()); 
                }
                
                dto.setLearningStatus(runner.calculateLearningStatus());
                dto.setNetworkName(runner.getForm().getNetworkName());
                dto.setRunnerId(mlpId);
                dto.setHiddenLayerNeuronCount(runner.getForm().getNeuronCount());
                statuses.add(dto);
            }
        }
        return statuses;
    }
}
