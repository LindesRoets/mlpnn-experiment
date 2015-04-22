package net.mlpnn.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.rep.Edge;
import net.mlpnn.dto.NetworkStatusDTO;
import net.mlpnn.rep.Node;
import net.mlpnn.dto.SigmaGraphDTO;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.enums.LearningStatus;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import org.json.JSONArray;
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
import org.neuroph.util.random.NguyenWidrowRandomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 *
 * @author Lindes Roets
 */
public class MultiLayerPerceptronRunner implements LearningEventListener, Runnable, Serializable {

	private transient final Logger LOGGER = LoggerFactory.getLogger(MultiLayerPerceptronRunner.class);

	private ApplicationConfiguration config;

	private MultilayerPercetpronParametersForm form;

	private static final long serialVersionUID = 3075382090934381294L;

	public MultiLayerPerceptronRunner(ApplicationConfiguration config, MultilayerPercetpronParametersForm form) {
		this.config = config; //TODO: implement a way to refresh the config - this will be needed after deserialization
		this.form = form;
	}

	private MultiLayerPerceptron perceptron;

	private ArrayList<Double> totalNetworkErrors = new ArrayList();

	private LearningStatus learningStatus;

	/**
	 * Initialize the dataset, multilayer perceptron, learning parameters and starts training
	 *
	 * @param form
	 */
	public LearningStatus start() {
		setTotalNetworkErrors(new ArrayList<>());
		DataSet dataSet = initializeDataSet(getForm());
		perceptron = initializeMultilayerPerceptron(getForm(), dataSet);
		initializeLearningParameters(getForm(), (MomentumBackpropagation) perceptron.getLearningRule());
		perceptron.learn(dataSet);
		return calculateAndSetLearningStatus(perceptron);
	}

	public LearningStatus stop() {
		perceptron.getLearningRule().stopLearning();
		return calculateAndSetLearningStatus(perceptron);
	}

	public LearningStatus pause() {
		perceptron.getLearningRule().pause();
		return calculateAndSetLearningStatus(perceptron);
	}

	public LearningStatus resume() {
		perceptron.getLearningRule().resume();
		return calculateAndSetLearningStatus(perceptron);
	}

	private void initializeLearningParameters(MultilayerPercetpronParametersForm form, MomentumBackpropagation learningRule) {
		learningRule.addListener(this);
		learningRule.setMomentum(form.getMomentum());
		learningRule.setMaxIterations(1000000);
		//learningRule.setMaxError(0.005);
	}

	public void handleLearningEvent(LearningEvent event) {
		BackPropagation bp = (BackPropagation) event.getSource();
		LOGGER.debug(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
		getTotalNetworkErrors().add(bp.getTotalNetworkError());
	}

	private MultiLayerPerceptron initializeMultilayerPerceptron(MultilayerPercetpronParametersForm form, DataSet dataSet) {
		MultiLayerPerceptron mp = new MultiLayerPerceptron(dataSet.getInputSize(), form.getNeuronCount(), dataSet.getOutputSize());
		for (Layer layer : mp.getLayers()) {
			for (Neuron neuron : layer.getNeurons()) {
				for (Connection connection : neuron.getInputConnections()) {
					connection.getWeight().setValue(0.4);
				}
			}
		}

		NguyenWidrowRandomizer randomizer = new NguyenWidrowRandomizer(-0.7, 0.7);
		randomizer.setRandomGenerator(new Random(1));
		mp.randomizeWeights(randomizer);
		return mp;

	}

	protected DataSet initializeDataSet(MultilayerPercetpronParametersForm form) {
		form.setDataSetName(form.getDataSetName().toUpperCase());
		DataSetInfo dataSetInfo = DataSetInfo.valueOf(form.getDataSetName());
		String filePath = config.getDatasetFilePath() + "/" + dataSetInfo.trainingFileName;
		DataSet dataSet = DataSet.createFromFile(filePath, dataSetInfo.numberOfInputs, dataSetInfo.numberOfOutputs, ",", false);
		MaxMinNormalizer normalizer = new MaxMinNormalizer();
		normalizer.normalize(dataSet);

		return dataSet;
	}

	private LearningStatus calculateAndSetLearningStatus(MultiLayerPerceptron perceptron) {
		if (perceptron == null || perceptron.getLearningRule() == null) {
			setLearningStatus(LearningStatus.STOPPED);
		} else if (perceptron.getLearningRule().isStopped()) {
			setLearningStatus(LearningStatus.STOPPED);
		} else if (perceptron.getLearningRule().isPausedLearning()) {
			setLearningStatus(LearningStatus.PAUSED);
		} else {
			setLearningStatus(LearningStatus.RUNNING);
		}
		return getLearningStatus();
	}

	public MultiLayerPerceptron getPerceptron() {
		return perceptron;
	}

	public LearningStatus calculateLearningStatus() {
		return calculateAndSetLearningStatus(perceptron);
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

	public ArrayList<Double> getTotalNetworkErrors() {
		return totalNetworkErrors;
	}

	public void setTotalNetworkErrors(ArrayList<Double> aTotalNetworkErrors) {
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
				node.setY((i + 1) * 600);
				if (layer.getNeuronsCount() == 1) {
					node.setX(1000); //position node in middle of x line
				} else {
					node.setX((n + 1) * (2000 / layer.getNeuronsCount()));
				}
				nodes.add(node);
			}
		}
		return nodes;
	}

	public JSONArray getFlotChartDoubleArray() {
		Assert.notNull(this);

		if (this.getTotalNetworkErrors() == null) {
			return null;
		}
		double[][] coordinates = new GraphService().getFlotChartDoubleArray(this);
		JSONArray ja = new JSONArray(coordinates);
		return ja;
	}

	public LearningStatus getLearningStatus() {
		return learningStatus;
	}

	public void setLearningStatus(LearningStatus learningStatus) {
		this.learningStatus = learningStatus;
	}

}
