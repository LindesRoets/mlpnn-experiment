package net.mlpnn.service;

import java.util.ArrayList;
import java.util.List;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.dto.Edge;
import net.mlpnn.dto.NetworkStatus;
import net.mlpnn.dto.Node;
import net.mlpnn.dto.SigmaGraphDTO;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.enums.LearningStatus;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.data.norm.MaxMinNormalizer;

/**
 *
 * @author Lindes Roets
 */
public class MultiLayerPerceptronRunner implements LearningEventListener, Runnable {
    
    private ApplicationConfiguration config;
    
    private MultilayerPercetpronParametersForm form;
    
    public MultiLayerPerceptronRunner(ApplicationConfiguration config, MultilayerPercetpronParametersForm form) {
        this.config = config;
        this.form = form;
    }
    
    private static MultiLayerPerceptron perceptron;
    
    private static ArrayList<Double> totalNetworkErrors = new ArrayList();
    
    private static LearningStatus learningStatus;

    /**
     * Initialize the dataset, multilayer perceptron, learning parameters and
     * starts training
     *
     * @param form
     */
    public LearningStatus start() {
        setTotalNetworkErrors(new ArrayList<>());
        DataSet dataSet = initializeDataSet(getForm());
        perceptron = initializeMultilayerPerceptron(getForm(), dataSet);
        initializeLearningParameters(getForm(), (MomentumBackpropagation) perceptron.getLearningRule());
        perceptron.learn(dataSet);
        return calculateAndSetLearningStatus(perceptron.getLearningRule());
    }
    
    public LearningStatus stop() {
        perceptron.getLearningRule().stopLearning();
        return calculateAndSetLearningStatus(perceptron.getLearningRule());
    }
    
    public LearningStatus pause() {
        perceptron.getLearningRule().pause();
        return calculateAndSetLearningStatus(perceptron.getLearningRule());
    }
    
    public LearningStatus resume() {
        perceptron.getLearningRule().resume();
        return calculateAndSetLearningStatus(perceptron.getLearningRule());
    }
    
    public NetworkStatus status() {
        NetworkStatus status = new NetworkStatus();
        status.setCurrentIteration(perceptron.getLearningRule().getCurrentIteration());
        status.setLearningStatus(calculateAndSetLearningStatus(perceptron.getLearningRule()));
        status.setTotalNetworkErrors(getTotalNetworkErrors());
        return status;
    }
    
    private void initializeLearningParameters(MultilayerPercetpronParametersForm form, MomentumBackpropagation learningRule) {
        learningRule.addListener(this);
        learningRule.setMomentum(form.getMomentum());
        learningRule.setMaxIterations(10000);
    }
    
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
        getTotalNetworkErrors().add(bp.getTotalNetworkError());
    }
    
    private MultiLayerPerceptron initializeMultilayerPerceptron(MultilayerPercetpronParametersForm form, DataSet dataSet) {
        MultiLayerPerceptron mp = new MultiLayerPerceptron(dataSet.getInputSize(), form.getNeuronCount(), dataSet.getOutputSize());
        return mp;
        
    }
    
    private DataSet initializeDataSet(MultilayerPercetpronParametersForm form) {
        DataSetInfo dataSetInfo = DataSetInfo.valueOf(form.getDataSetName().toUpperCase());
        String filePath = config.getDatasetFilePath() + "/" + dataSetInfo.localFileName;
        DataSet dataSet = DataSet.createFromFile(filePath, dataSetInfo.numberOfInputs, dataSetInfo.numberOfOutputs, dataSetInfo.delimiter, true);
        MaxMinNormalizer normalizer = new MaxMinNormalizer();
        normalizer.normalize(dataSet);
        return dataSet;
    }
    
    private LearningStatus calculateAndSetLearningStatus(BackPropagation learningRule) {
        
        if (learningRule == null) {
            learningStatus = LearningStatus.STOPPED;
        } else if (learningRule.isStopped()) {
            learningStatus = LearningStatus.STOPPED;
        } else if (learningRule.isPausedLearning()) {
            learningStatus = LearningStatus.PAUSED;
        } else {
            learningStatus = LearningStatus.RUNNING;
        }
        return learningStatus;
    }
    
    public MultiLayerPerceptron getPerceptron() {
        return perceptron;
    }
    
    public LearningStatus calculateLearningStatus() {
        return calculateAndSetLearningStatus(perceptron.getLearningRule());
    }
    
    @Override
    public void run() {
        start();
    }
    
    public MultilayerPercetpronParametersForm getForm() {
        return form;
    }
    
    public void setForm(MultilayerPercetpronParametersForm form) {
        this.form = form;
    }
    
    public static ArrayList<Double> getTotalNetworkErrors() {
        return totalNetworkErrors;
    }
    
    public static void setTotalNetworkErrors(ArrayList<Double> aTotalNetworkErrors) {
        totalNetworkErrors = aTotalNetworkErrors;
    }
    
    public SigmaGraphDTO getNetworkTopology() {
        
        SigmaGraphDTO dto = new SigmaGraphDTO();
        
        ArrayList<Node> nodes = getNodes();
        ArrayList<Edge> edges = new ArrayList<>();
        
        Layer[] layers = perceptron.getLayers();
        for (int i = 0; i < layers.length; i++) {
            
            if (i == layers.length - 1) {//last layer
                break;
            }
            Layer layer = layers[i];
            Layer nextLayer = layers[i + 1];
            
            for (int n = 0; n < layer.getNeuronsCount(); n++) {
                for (int m = 0; m < nextLayer.getNeuronsCount(); m++) {
                    edges.add(new Edge("l" + i + "s" + n + "t" + m, "l" + i + "n" + n, "l" + (i + 1) + "n" + m));
                }
            }
        }
        
        dto.setEdges(edges);
        dto.setNodes(nodes);
        
        return dto;
    }
    
    private ArrayList<Node> getNodes() {
        ArrayList<Node> nodes = new ArrayList<>();
        Layer[] layers = perceptron.getLayers();
        for (int i = 0; i < layers.length; i++) {
            Layer layer = layers[i];
            //Neuron[] neurons = layer.getNeurons();
            for (int n = 0; n < layer.getNeurons().length; n++) {
                //Neuron neuron = neurons[n];
                Node node = new Node();
                node.setId("l" + i + "n" + n);
                node.setLabel("l" + i + "n" + n);
                node.setSize(3);
                node.setY((i+1)*600);
                if(layer.getNeuronsCount() == 1){
                    node.setX(1000); //position node in middle of x line
                }else{
                   node.setX((n+1)*(2000/layer.getNeuronsCount())); 
                }
                nodes.add(node);
            }
        }
        return nodes;
    }
    
}
