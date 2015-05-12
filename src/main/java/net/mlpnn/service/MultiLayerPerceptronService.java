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
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.dto.DashBoardDTO;
import net.mlpnn.dto.NetworkStatusDTO;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import net.mlpnn.util.Counting;
import net.mlpnn.util.Sorting;
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
 * Exposes core service for managing the HashMap of MultiLayerPerceptronRunners
 *
 *
 * @author Lindes Roets
 */
@Service
public class MultiLayerPerceptronService {

	private final Logger LOGGER = LoggerFactory.getLogger(MultiLayerPerceptronService.class);

	@Autowired
	private ApplicationConfiguration config;

	/**
	 * The HashMap of MultiLayerPerceptronRunners that is in memory.
	 */
	private HashMap<String, MultiLayerPerceptronRunner> multiLayerPerceptronRunners = new HashMap<>();

	/**
	 *
	 * @param multiLayerPerceptronRunnerId - the id the uniquely identifies the runner in memory
	 * @return {@link MultiLayerPerceptronRunner}
	 */
	public MultiLayerPerceptronRunner getStatus(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
		if (runner == null) {
			return null;
		}
		return runner;
	}

	/**
	 *
	 * @param form - {@link MultilayerPercetpronParametersForm}. The form that encapsulates all the parameters necessary to start a new learning
	 * thread.
	 * @return The {@link UUID} in string format
	 */
	public String startLearning(MultilayerPercetpronParametersForm form) {
		MultiLayerPerceptronRunner runner = new MultiLayerPerceptronRunner(config, form);
		Thread thread = new Thread(runner);
		thread.start();
		String id = UUID.randomUUID().toString();
		runner.setId(id);
		getMultiLayerPerceptronRunners().put(id, runner);
		return id;
	}

	/**
	 *
	 * @param multiLayerPerceptronRunnerId - Removes the runner with the matching id from memory. If this dataset has not been persisted this will be
	 * like a delete.
	 * @return {@link MultiLayerPerceptronRunner} that was removed from the HashMap.
	 */
	public MultiLayerPerceptronRunner removeTest(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().remove(multiLayerPerceptronRunnerId);
		if (runner == null) {
			return null;
		}
		return runner;
	}

	/**
	 * Removes all runners from the HashMap. This will basically clear the memory. If you did not write your runners to disk with the save options you
	 * will loose all your data.
	 *
	 * @return true if the the HashMap is empty.
	 */
	public boolean removeAllRunners() {

		getMultiLayerPerceptronRunners().clear();

		return getMultiLayerPerceptronRunners().isEmpty();
	}

	/**
	 *
	 * @param multiLayerPerceptronRunnerId - Pause learning for the runner identified by the multiLayerPerceptronRunnerId.
	 * @return {@link MultiLayerPerceptronRunner}. null if no runner was found with id multiLayerPerceptronRunnerId in memory.
	 */
	public MultiLayerPerceptronRunner pauseLearning(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
		if (runner == null) {
			return null;
		}
		runner.getPerceptron().getLearningRule().pause();
		return runner;
	}

	/**
	 *
	 * @param multiLayerPerceptronRunnerId - Stop learning for the runner identified by the multiLayerPerceptronRunnerId.
	 * @return {@link MultiLayerPerceptronRunner}. null if no runner was found with id multiLayerPerceptronRunnerId in memory.
	 */
	public MultiLayerPerceptronRunner stopLearning(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
		if (runner == null) {
			return null;
		}
		runner.getPerceptron().getLearningRule().stopLearning();
		return runner;
	}

	/**
	 *
	 * @param multiLayerPerceptronRunnerId - Resume learning for the runner identified by the multiLayerPerceptronRunnerId.
	 * @return {@link MultiLayerPerceptronRunner}. null if no runner was found with id multiLayerPerceptronRunnerId in memory.
	 */
	public MultiLayerPerceptronRunner resumeLearning(String multiLayerPerceptronRunnerId) {
		MultiLayerPerceptronRunner runner = getMultiLayerPerceptronRunners().get(multiLayerPerceptronRunnerId);
		if (runner == null) {
			return null;
		}
		runner.getPerceptron().getLearningRule().resume();
		return runner;
	}

	/**
	 * 
	 * @return list of {@link NetworkStatusDTO} - Returns a list of all statuses of the learning threads
	 * @throws InterruptedException - 
	 */
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

	/**
	 * Groups all the runners by their {@link DataSetInfo}
	 * @return HashMap with {@link DataSetInfo} as key
	 */
	public HashMap<DataSetInfo, List<MultiLayerPerceptronRunner>> getRunnersByGroup() {

		HashMap<DataSetInfo, List<MultiLayerPerceptronRunner>> groups = new HashMap<>();
		for (DataSetInfo dataSet : DataSetInfo.values()) {
			groups.put(dataSet, new ArrayList<>());
		}

		for (MultiLayerPerceptronRunner runner : multiLayerPerceptronRunners.values()) {
			groups.get(DataSetInfo.valueOf(runner.getForm().getDataSetName())).add(runner);
		}
		return groups;
	}

	/**
	 * 
	 * @param dataSetInfo - The {@link DataSetInfo} by which to filter the runners in memory. The runners are 
	 * sorted by neuron count
	 * @return The list of {@link MultiLayerPerceptronRunner} of filtered runners.
	 */
	public List<MultiLayerPerceptronRunner> getRunners(DataSetInfo dataSetInfo) {
		List<MultiLayerPerceptronRunner> runners = new ArrayList<>();

		for (MultiLayerPerceptronRunner runner : multiLayerPerceptronRunners.values()) {
			if (runner.getForm().getDataSetName().equals(dataSetInfo.name())) {
				runners.add(runner);
			}
		}
		Sorting.sortRunnersByNeuronCount(runners);
		return runners;
	}

	/**
	 * Test the trained perceptron. Prints out the data to screen for each sample tested.
	 * @param multiLayerPerceptronRunnerId - The runner that contains the trained perceptron to test.
	 */
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

	/**
	 * Group all runners by dataset and wrtie to file
	 *
	 * @throws java.io.IOException - 
	 */
	public void saveRunners() throws IOException {

		for (DataSetInfo dataSetInfo : DataSetInfo.values()) {
			saveRunners(dataSetInfo);
		}
	}

	/**
	 * Saves all the runners for which the data set used matches the parameter.
	 *
	 * @param dataSetInfo {@link DataSetInfo}
	 * @throws IOException - 
	 */
	public void saveRunners(DataSetInfo dataSetInfo) throws IOException {
		FileOutputStream fout = new FileOutputStream(config.getDatasetFilePath() + "/" + dataSetInfo.name() + "-perceptrons.ser.gz");
		GZIPOutputStream gz = new GZIPOutputStream(fout);
		ObjectOutputStream oos = new ObjectOutputStream(gz);

		//create new array to contain all the runners to be saved
		HashMap<String, MultiLayerPerceptronRunner> dataSetRunners = new HashMap<>();
		for (Map.Entry<String, MultiLayerPerceptronRunner> entry : this.multiLayerPerceptronRunners.entrySet()) {
			if (entry.getValue().getForm().getDataSetName().equalsIgnoreCase(dataSetInfo.name())) {
				dataSetRunners.put(entry.getKey(), entry.getValue());
			}
		}

		oos.writeObject(dataSetRunners);
	}

	public void retrieveRunners() throws IOException, ClassNotFoundException {
		//Read all the saved perceptron runners from disk
		FileInputStream fis = new FileInputStream(config.getDatasetFilePath() + "/perceptrons.ser");
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();
		ois.close();

		//Re-instantiate the data sets - this was not serialized
		HashMap<String, MultiLayerPerceptronRunner> runners = (HashMap<String, MultiLayerPerceptronRunner>) obj;
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

	public void retrieveRunners(DataSetInfo dataSetInfo) throws IOException, ClassNotFoundException {
		//Read all the saved perceptron runners from disk
		FileInputStream fis = new FileInputStream(config.getDatasetFilePath() + "/" + dataSetInfo.name() + "-perceptrons.ser");
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();
		ois.close();

		//Re-instantiate the data sets - this was not serialized
		HashMap<String, MultiLayerPerceptronRunner> runners = (HashMap<String, MultiLayerPerceptronRunner>) obj;
		for (Map.Entry<String, MultiLayerPerceptronRunner> entry : runners.entrySet()) {
			MultiLayerPerceptronRunner runner = entry.getValue();
			
			//refresh the config - when the application starts the config is initialized, so we must refresh the stale config from the repo
			runner.setConfig(config); 
			DataSet dataSet = runner.initializeDataSet(runner.getForm());
			runner.getPerceptron().getLearningRule().stopLearning();// we need to explicitly set this to stopped when deserializing a runner 
			runner.getPerceptron().getLearningRule().setTrainingSet(dataSet);
			this.multiLayerPerceptronRunners.put(entry.getKey(), runner);
		}

	}

	public DashBoardDTO getDashBoard() {
		DashBoardDTO dashboard = new DashBoardDTO();
		dashboard.setPausedThreadCount(Counting.getPausedThreadCount(this.multiLayerPerceptronRunners.values()));
		dashboard.setRunningThreadCount(Counting.getLearningThreadCount(this.multiLayerPerceptronRunners.values()));
		dashboard.setStoppedThreadCount(Counting.getStoppedThreadCount(this.multiLayerPerceptronRunners.values()));
		return dashboard;
	}

	public HashMap<String, MultiLayerPerceptronRunner> getMultiLayerPerceptronRunners() {
		return multiLayerPerceptronRunners;
	}

	protected void setMultiLayerPerceptronRunners(HashMap<String, MultiLayerPerceptronRunner> multiLayerPerceptronRunners) {
		this.multiLayerPerceptronRunners = multiLayerPerceptronRunners;
	}

}
