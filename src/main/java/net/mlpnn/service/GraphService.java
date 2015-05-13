package net.mlpnn.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
import net.mlpnn.dto.FlotChartDTO;
import net.mlpnn.rep.Coordinate;
import net.mlpnn.util.Sorting;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 *
 * @author Lindes Roets
 */
@Service
public class GraphService {

	private final String PATH_TO_RUNTIME_REPORTS = "/public/report/runtime/";
	private final String CONVERGENCE_REPORT_SUFFIX = "-convergenceReport.csv";
	private final String PATH_TO_RUNTIME_REPORTS_FROM_SRC = "src/main/resources" + PATH_TO_RUNTIME_REPORTS;

	private final Logger LOGGER = LoggerFactory.getLogger(GraphService.class);

	public FlotChartDTO getFlotChart(MultiLayerPerceptronRunner runner) {
		Assert.notNull(runner);
		FlotChartDTO flotChartDTO = new FlotChartDTO();
		ArrayList<Coordinate> coordinates = new ArrayList<>();
		if (runner.getTotalNetworkErrors() == null) {
			return null;
		}
		for (int i = 0; i < runner.getTotalNetworkErrors().size(); i++) {
			Coordinate coordinate = new Coordinate(new Double(i + 1), runner.getTotalNetworkErrors().get(i));
			coordinates.add(coordinate);
		}
		flotChartDTO.setCoordinates(coordinates.toArray(new Coordinate[coordinates.size()]));
		return flotChartDTO;
	}

	public double[][] getFlotChartDoubleArray(MultiLayerPerceptronRunner runner) {
		Assert.notNull(runner);

		if (runner.getTotalNetworkErrors() == null) {
			return null;
		}
		double[][] coordinates = new double[runner.getTotalNetworkErrors().size()][2];
		for (int i = 0; i < runner.getTotalNetworkErrors().size(); i++) {
			Coordinate coordinate = new Coordinate(new Double(i + 1), runner.getTotalNetworkErrors().get(i));
			coordinates[i] = coordinate.getArrayFormat();
		}

		return coordinates;
	}

	/**
	 *
	 * @param runners The runners from which the array of convergence errors per epoch will be extracted.
	 * @return The array sorted by size of hidden layer neurons ascending.
	 */
	public double[][] getAllConvergenceErrors(List<MultiLayerPerceptronRunner> runners) {
		Assert.notNull(runners);

		Sorting.sortRunnersByNeuronCount(runners);

		double[][] coordinates = new double[runners.size()][2];
		int count = 0;
		for (MultiLayerPerceptronRunner runner : runners) {
			Coordinate coordinate = new Coordinate(new Double(runner.getForm().getNeuronCount()), new Double(runner.getPerceptron().getLearningRule().getCurrentIteration()));
			coordinates[count] = coordinate.getArrayFormat();
			count++;

		}
		return coordinates;
	}

	/**
	 *
	 * @param runners The runners from which the csv file will be compiled. The first column is the hidden layer neuron count. The
	 * second column is the epoch in which learning converged or were stopped for any other reason. The csv file gets written to
	 * src/main/resources/public/report/runtime with a file name <{DataSetInfo.name()}>-convergenceReport.csv
	 */
	public void writeConvergenceCSV(List<MultiLayerPerceptronRunner> runners) throws IOException {
		if (runners == null || runners.isEmpty()) {
			return;
		}

		File csvFile = FileUtils.toFile(this.getClass().getResource(PATH_TO_RUNTIME_REPORTS + runners.get(0).getForm().getDataSetName() + CONVERGENCE_REPORT_SUFFIX));
		FileOutputStream fos = null;
		if (csvFile == null) {
			csvFile = new File(PATH_TO_RUNTIME_REPORTS_FROM_SRC + runners.get(0).getForm().getDataSetName() + CONVERGENCE_REPORT_SUFFIX);
			LOGGER.info("File was not found.");
		}
		fos = new FileOutputStream(csvFile, false);

		StringBuilder bf = new StringBuilder("Neuron Count,Epoch\n");

		Sorting.sortRunnersByNeuronCount(runners);
		for (MultiLayerPerceptronRunner runner : runners) {
			bf.append(runner.getForm().getNeuronCount()).append(",").append(runner.getPerceptron().getLearningRule().getCurrentIteration()).append("\n");
			IOUtils.write(bf.toString(), fos, "UTF-8");
			bf = new StringBuilder();
		}

		fos.close();

	}

	@PreDestroy
	public void deleteAllRuntimeReports() {
		try {
			FileUtils.cleanDirectory(new File(PATH_TO_RUNTIME_REPORTS_FROM_SRC));
			LOGGER.info("Successfully removed all the files in :" + PATH_TO_RUNTIME_REPORTS_FROM_SRC);
		} catch (IOException ioe) {
			LOGGER.warn("Error while trying to delete runtime reports.", ioe);
		}

	}

}
