package net.mlpnn.dto;

import java.util.ArrayList;
import net.mlpnn.enums.LearningStatus;

/**
 *
 * @author Lindes Roets
 */
public class NetworkStatusDTO {

    private LearningStatus learningStatus;
    private int currentIteration;
    private String runnerId;
    private String networkName;
    private int hiddenLayerNeuronCount;

    public int getCurrentIteration() {
        return currentIteration;
    }

    public void setCurrentIteration(int currentIteration) {
        this.currentIteration = currentIteration;
    }

    public LearningStatus getLearningStatus() {
        return learningStatus;
    }

    public void setLearningStatus(LearningStatus learningStatus) {
        this.learningStatus = learningStatus;
    }

    public String getRunnerId() {
        return runnerId;
    }

    public void setRunnerId(String runnerId) {
        this.runnerId = runnerId;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public int getHiddenLayerNeuronCount() {
        return hiddenLayerNeuronCount;
    }

    public void setHiddenLayerNeuronCount(int hiddenLayerNeuronCount) {
        this.hiddenLayerNeuronCount = hiddenLayerNeuronCount;
    }

}
