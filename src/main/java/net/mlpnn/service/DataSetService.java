package net.mlpnn.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.enums.DataSetInfo;
import org.apache.commons.io.FileUtils;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lindes Roets
 */
@Service
public class DataSetService {

	private final Logger LOGGER = LoggerFactory.getLogger(DataSetService.class);

	@Autowired
	private ApplicationConfiguration config;

	public void downloadDataSets() throws IOException {
		for (DataSetInfo info : DataSetInfo.values()) {
			this.downloadDataSet(info);
		}

	}

	public void downloadDataSet(DataSetInfo info) throws MalformedURLException, IOException {
		File file = new File(config.getDatasetFilePath() + "/" + info.originalFileName);
		FileUtils.copyURLToFile(new URL(info.url), file);
	}

	public void createTestAndValidationDataForRegressor(DataSetInfo dataSetInfo) throws IOException {

		//Do preprossessing on the files - some data sets need to be changed to fit the network input and output data types
		preprocessDataSet(dataSetInfo);
		String filePath = config.getDatasetFilePath() + "/" + dataSetInfo.transformed;
		LOGGER.debug("File path: " + filePath);
		DataSet dataSet = DataSet.createFromFile(filePath, dataSetInfo.numberOfInputs, dataSetInfo.numberOfOutputs, ",", dataSetInfo.containsHeader);

		//Shuffle the data set
		Random r = new Random(1);
		Collections.shuffle(dataSet.getRows(), r);

		//Group all the classes into groups
		List<List<DataSetRow>> dataSetGroups = new ArrayList<>();

		LOGGER.info("\nData Set Size: " + dataSet.getRows().size());
		for (DataSetRow dataSetRow : dataSet.getRows()) {

			boolean elementAdded = false;
			for (List<DataSetRow> rows : dataSetGroups) {
				double[] expected = dataSetRow.getDesiredOutput();

				if (Arrays.equals(expected, rows.get(0).getDesiredOutput())) {
					elementAdded = rows.add(dataSetRow);
					break;
				}
			}
			//add new group
			if (!elementAdded) {
				List<DataSetRow> rows = new ArrayList<>();
				rows.add(dataSetRow);
				dataSetGroups.add(rows);
			}

		}

		LOGGER.info("\n\nData set: " + dataSetInfo.name());

		//Find the smallest group size
		int smallestGroupSize = Integer.MAX_VALUE;
		for (List<DataSetRow> groups : dataSetGroups) {
			if (groups.size() < smallestGroupSize) {
				smallestGroupSize = groups.size();
			}
			LOGGER.info("Size: " + groups.size() + "\t" + Arrays.toString(groups.get(0).getDesiredOutput()));
		}

		LOGGER.info("Smallest group size: " + smallestGroupSize);

		//devide the datasets into 2 files - a training set and a validation set
		File trainingFile = new File(config.getDatasetFilePath() + "/" + dataSetInfo.trainingFileName);
		File validationFile = new File(config.getDatasetFilePath() + "/" + dataSetInfo.validationFileName);

		List<String> trainingFileLines = new ArrayList<>();
		List<String> validationFileLines = new ArrayList<>();

		for (List<DataSetRow> rows : dataSetGroups) {
			for (int i = 0; i < Math.round((rows.size() * 2.0) / 3.0); i++) {
				trainingFileLines.add(rows.get(i).toCSV());
			}
			for (long i = Math.round((rows.size() * 2.0) / 3.0); i != rows.size(); i++) {
				validationFileLines.add(rows.get((int) i).toCSV());
			}

		}
		LOGGER.info("Training set size: " + trainingFileLines.size());
		LOGGER.info("Validation set size: " + validationFileLines.size());

		FileUtils.writeLines(trainingFile, trainingFileLines);
		FileUtils.writeLines(validationFile, validationFileLines);
	}

	public void preprocessDataSet(DataSetInfo dataSetInfo) throws IOException {
		switch (dataSetInfo) {
			case WINE_QUALITY_RED:
			case WINE_QUALITY_WHITE:
				List<String> lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
				List<String> transformed = new ArrayList<>();

				for (String line : lines) {
					String tmp = line.replaceAll(";", ",");

					String category = tmp.substring(tmp.lastIndexOf(",") + 1);
					switch (category) {
						case "1":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,0,0,0,0,0,0,1";
							break;
						case "2":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,0,0,0,0,0,1,0";
							break;
						case "3":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,0,0,0,0,1,0,0";
							break;
						case "4":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,0,0,0,1,0,0,0";
							break;
						case "5":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,0,0,1,0,0,0,0";
							break;
						case "6":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,0,1,0,0,0,0,0";
							break;
						case "7":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,1,0,0,0,0,0,0";
							break;
						case "8":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,1,0,0,0,0,0,0,0";
							break;
						case "9":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,1,0,0,0,0,0,0,0,0";
							break;
						case "10":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "1,0,0,0,0,0,0,0,0,0";
							break;
					}
					transformed.add(tmp);
				}

				FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.transformed), transformed);
				break;

			case SKIN_SEGMENTATION:

				lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
				transformed = new ArrayList<>();

				for (String line : lines) {
					String tmp = line.replaceAll("\t", ",");
					
					String category = tmp.substring(tmp.lastIndexOf(",") + 1);
					switch (category) {
						case "1":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,1";
							break;
						case "2":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "1,0";
							break;
					}
					
					transformed.add(tmp);
				}

				FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.transformed), transformed);
				break;

			case GLASS:

				lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
				transformed = new ArrayList<>();
				for (String line : lines) {
					//For the glass dataSet we have to remove the first column - this is an id column
					String tmp = line.substring(line.indexOf(dataSetInfo.delimiter) + 1);

					String category = tmp.substring(tmp.lastIndexOf(",") + 1);
					switch (category) {
						case "1":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,0,0,0,1";
							break;
						case "2":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,0,0,1,0";
							break;
						case "3":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,0,1,0,0";
							break;
						case "4":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,0,1,0,0,0";
							break;
						case "5":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,1,0,0,0,0";
							break;
						case "6":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,1,0,0,0,0,0";
							break;
						case "7":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "1,0,0,0,0,0,0";
							break;
					}
					transformed.add(tmp);
				}

				FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.transformed), transformed);
				break;

			case SEEDS:

				LOGGER.info("in seeds");
				lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
				transformed = new ArrayList<>();

				for (String line : lines) {
					//There are some lines that contains 2 tabs next to each other instead of one
					String tmp = line.replaceAll("\t\t", "\t");

					tmp = tmp.replaceAll("\t", ",");
					//Change the classification 1,2 and 3 to (0,0,1),(0,1,0) and (1,0,0) respectively
					String category = tmp.substring(tmp.lastIndexOf(",") + 1);
					switch (category) {
						case "1":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,0,1";
							break;
						case "2":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "0,1,0";
							break;
						case "3":
							tmp = tmp.substring(0, tmp.lastIndexOf(",") + 1) + "1,0,0";
							break;
					}
					transformed.add(tmp);
				}

				FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.transformed), transformed);
				break;

			case IONOSPHERE:
				lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
				transformed = new ArrayList<>();

				for (String line : lines) {

					//Change the classification g and b to 1 and 2 respectively
					String tmp = line.substring(line.lastIndexOf(",") + 1);
					switch (tmp) {
						case "g":
							tmp = line.substring(0, line.lastIndexOf(",") + 1) + "0,1";
							break;
						case "b":
							tmp = line.substring(0, line.lastIndexOf(",") + 1) + "1,0";
							break;
					}

					transformed.add(tmp);
				}

				FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.transformed), transformed);
				break;

			case DERMATOLOGY:
				//Remove all lines with missing values
				lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
				transformed = new ArrayList<>();

				for (String line : lines) {
					if (!line.contains("?")) {

						String category = line.substring(line.lastIndexOf(",") + 1);
						switch (category) {
							case "1":
								line = line.substring(0, line.lastIndexOf(",") + 1) + "0,0,0,0,0,1";
								break;
							case "2":
								line = line.substring(0, line.lastIndexOf(",") + 1) + "0,0,0,0,1,0";
								break;
							case "3":
								line = line.substring(0, line.lastIndexOf(",") + 1) + "0,0,0,1,0,0";
								break;
							case "4":
								line = line.substring(0, line.lastIndexOf(",") + 1) + "0,0,1,0,0,0";
								break;
							case "5":
								line = line.substring(0, line.lastIndexOf(",") + 1) + "0,1,0,0,0,0";
								break;
							case "6":
								line = line.substring(0, line.lastIndexOf(",") + 1) + "1,0,0,0,0,0";
								break;
						}

						transformed.add(line);
					}
				}

				FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.transformed), transformed);
				break;

			case IRIS:
				lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
				transformed = new ArrayList<>();

				for (String line : lines) {

					/**
					 * Change the classification Iris-setosa, Iris-versicolor and Iris-virginica to (1,0,0),(0,1,0) and (0,0,1)
					 * respectively
					 */
					String tmp = line.substring(line.lastIndexOf(",") + 1);
					switch (tmp) {
						case "Iris-setosa":
							tmp = line.substring(0, line.lastIndexOf(",") + 1) + "0,0,1";
							break;
						case "Iris-versicolor":
							tmp = line.substring(0, line.lastIndexOf(",") + 1) + "0,1,0";
							break;
						case "Iris-virginica":
							tmp = line.substring(0, line.lastIndexOf(",") + 1) + "1,0,0";
							break;
					}

					transformed.add(tmp);

				}

				FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.transformed), transformed);
				break;
		}

	}
}
