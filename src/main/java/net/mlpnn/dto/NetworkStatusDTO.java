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
    private ArrayList<Double> totalNetworkErrors;
    private Long runnerId;
    private String networkName;

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

    public ArrayList<Double> getTotalNetworkErrors() {
        return totalNetworkErrors;
    }

    public void setTotalNetworkErrors(ArrayList<Double> totalNetworkErrors) {
        this.totalNetworkErrors = totalNetworkErrors;
    }

    public Long getRunnerId() {
        return runnerId;
    }

    public void setRunnerId(Long runnerId) {
        this.runnerId = runnerId;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

}
