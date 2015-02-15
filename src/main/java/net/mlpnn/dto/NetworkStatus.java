package net.mlpnn.dto;

import java.util.ArrayList;
import net.mlpnn.enums.LearningStatus;

/**
 *
 * @author Lindes Roets
 */
public class NetworkStatus {

    private LearningStatus learningStatus;
    private int currentIteration;
    private ArrayList<Double> totalNetworkErrors;

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

}
