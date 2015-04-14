package net.mlpnn.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.dto.NetworkStatusDTO;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 *
 * @author Lindes Roets
 */
@Service
public class MultiLayerPerceptronService {

	private final Logger LOGGER = LoggerFactory.getLogger(MultiLayerPerceptronService.class);

	@Autowired
	private ApplicationConfiguration config;

	private HashMap<String, MultiLayerPerceptronRunner> multiLayerPerceptronRunners = new HashMap<>();

	public MultiLayerPerceptronRunner getStatus(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
		if (runner == null) {
			return null;
		}
		return runner;
	}

	public String startLearning(MultilayerPercetpronParametersForm form) {
		MultiLayerPerceptronRunner runner = new MultiLayerPerceptronRunner(config, form);
		Thread thread = new Thread(runner);
		thread.start();
		String id = UUID.randomUUID().toString();
		getMultiLayerPerceptronRunners().put(id, runner);
		return id;
	}

	public HashMap<String, MultiLayerPerceptronRunner> getMultiLayerPerceptronRunners() {
		return multiLayerPerceptronRunners;
	}

	protected void setMultiLayerPerceptronRunners(HashMap<String, MultiLayerPerceptronRunner> multiLayerPerceptronRunners) {
		this.multiLayerPerceptronRunners = multiLayerPerceptronRunners;
	}

	public MultiLayerPerceptron removeTest(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().remove(multiLayerPerceptronRunnerId);
		if (runner == null) {
			return null;
		}
		return runner.getPerceptron();
	}

	public MultiLayerPerceptronRunner pauseLearning(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
		if (runner == null) {
			return null;
		}
		runner.getPerceptron().getLearningRule().pause();
		return runner;
	}

	public MultiLayerPerceptronRunner stopLearning(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
		if (runner == null) {
			return null;
		}
		runner.getPerceptron().getLearningRule().stopLearning();
		return runner;
	}

	public MultiLayerPerceptronRunner resumeLearning(String multiLayerPerceptronRunnerId) {
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
			HashMap<String, MultiLayerPerceptronRunner> runners = this.getMultiLayerPerceptronRunners();
			Set<String> ids = runners.keySet();
			String[] idss = ids.toArray(new String[ids.size()]);
			List<String> mlpIds = Arrays.asList(idss);
			Collections.sort(mlpIds);
			for (String mlpId : mlpIds) {
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

				if (runner.getPerceptron() == null) {
					dto.setCurrentIteration(-1);
				} else {
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

	public HashMap<DataSetInfo, List<MultiLayerPerceptronRunner>> getRunnersByGroup() {

		HashMap<DataSetInfo, List<MultiLayerPerceptronRunner>> groups = new HashMap<>();
		for (DataSetInfo dataSet : DataSetInfo.values()) {
			groups.put(dataSet, new ArrayList<MultiLayerPerceptronRunner>());
		}

		for (MultiLayerPerceptronRunner runner : multiLayerPerceptronRunners.values()) {
			groups.get(DataSetInfo.valueOf(runner.getForm().getDataSetName())).add(runner);
		}
		return groups;
	}

	public List<MultiLayerPerceptronRunner> getRunners(DataSetInfo dataSetInfo) {
		List<MultiLayerPerceptronRunner> runners = new ArrayList<>();

		for (MultiLayerPerceptronRunner runner : multiLayerPerceptronRunners.values()) {
			if (runner.getForm().getDataSetName().equals(dataSetInfo.name())) {
				runners.add(runner);
			}
		}
		return runners;
	}

	public void testPerceptron(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);

		Assert.notNull(runner, "Need a runner to be able to test the perceptron!");
		DataSetInfo dataSetInfo = DataSetInfo.valueOf(runner.getForm().getDataSetName());
		String filePath = config.getDatasetFilePath() + "/" + dataSetInfo.validationFileName;

		//non of the validation data sets contains headers. All validation sets have "," as delimiter
		DataSet dataSet = DataSet.createFromFile(filePath, dataSetInfo.numberOfInputs, dataSetInfo.numberOfOutputs, ",", false);
		MaxMinNormalizer normalizer = new MaxMinNormalizer();
		normalizer.normalize(dataSet);

		MultiLayerPerceptron perceptron = runner.getPerceptron();
		for (DataSetRow row : dataSet.getRows()) {
			perceptron.setInput(row.getInput());
			perceptron.calculate();
			double[] networkOutput = perceptron.getOutput();

			LOGGER.info("Inputs: " + Arrays.toString(row.getInput()));
			LOGGER.info("Desired output: " + Arrays.toString(row.getDesiredOutput()));
			LOGGER.info("Actual output:" + Arrays.toString(networkOutput));
			LOGGER.info("/n");
		}
	}

	public void saveRunners() throws IOException {
		String id = UUID.randomUUID().toString();
		LOGGER.info("Id: " + id);
		FileOutputStream fout = new FileOutputStream(config.getDatasetFilePath() + "/perceptrons.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(this.multiLayerPerceptronRunners);
	}

	public void retrieveRunners() throws IOException, ClassNotFoundException {
		//Read all the saved perceptron runners from disk
		FileInputStream fis = new FileInputStream(config.getDatasetFilePath() + "/perceptrons.ser");
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();
		ois.close();
		
		//Re-instantiate the data sets - this was not serialized
		HashMap<String, MultiLayerPerceptronRunner> runners = (HashMap<String, MultiLayerPerceptronRunner>)obj;
		Set<String> ids = runners.keySet();
		String[] idss = ids.toArray(new String[ids.size()]);
		List<String> mlpIds = Arrays.asList(idss);
		Collections.sort(mlpIds);
		for (String mlpId : mlpIds) {
			MultiLayerPerceptronRunner runner = runners.get(mlpId);
			DataSet dataSet = runner.initializeDataSet(runner.getForm());
			runner.getPerceptron().getLearningRule().stopLearning();// we need to explicitly set this to stopped when deserializing a runner 
			runner.getPerceptron().getLearningRule().setTrainingSet(dataSet);
			this.multiLayerPerceptronRunners.put(mlpId, runner);
		}
		
	}

}
