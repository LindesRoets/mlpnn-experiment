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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lindes Roets
 */
@Service
public class DataSetService {

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
        String filePath = config.getDatasetFilePath() + "/" + dataSetInfo.refactoredFileName;
        DataSet dataSet = DataSet.createFromFile(filePath, dataSetInfo.numberOfInputs, dataSetInfo.numberOfOutputs, dataSetInfo.delimiter, dataSetInfo.containsHeader);

        //Shuffle the data set
        Random r = new Random(1);
        Collections.shuffle(dataSet.getRows(), r);

        //Group all the classes into groups
        List<List<DataSetRow>> dataSetGroups = new ArrayList<>();

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

        //Find the smallest group size
        int smallestGroupSize = Integer.MAX_VALUE;
        for (List<DataSetRow> groups : dataSetGroups) {
            if (groups.size() < smallestGroupSize) {
                smallestGroupSize = groups.size();
            }
            System.out.println("Size: " + groups.size() + "\t" + Arrays.toString(groups.get(0).getDesiredOutput()));
        }

        System.out.println("Smallest group size: " + smallestGroupSize);

        //devide the datasets into 2 files - a training set and a validation set
        File trainingFile = new File(config.getDatasetFilePath() + "/" + dataSetInfo.trainingFileName);
        File validationFile = new File(config.getDatasetFilePath() + "/" + dataSetInfo.validationFileName);

        List<String> trainingFileLines = new ArrayList<>();
        List<String> validationFileLines = new ArrayList<>();

        for (int i = 0; i < Math.round(smallestGroupSize / 2.0); i++) {
            for (List<DataSetRow> rows : dataSetGroups) {
                trainingFileLines.add(rows.get(i).toCSV());
            }

        }

        for (long i = Math.round(smallestGroupSize / 2.0); i != smallestGroupSize; i++) {
            for (List<DataSetRow> rows : dataSetGroups) {
                validationFileLines.add(rows.get((int) i).toCSV());
            }
        }

        FileUtils.writeLines(trainingFile, trainingFileLines);
        FileUtils.writeLines(validationFile, validationFileLines);
    }

    public void preprocessDataSet(DataSetInfo dataSetInfo) throws IOException {
        switch (dataSetInfo) {
            case GLASS:

                List<String> lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
                List<String> adaptedLines = new ArrayList<>();
                for (String line : lines) {
                    //For the glass dataSet we have to remove the first column - this is an id column
                    String tmp = line.substring(line.indexOf(dataSetInfo.delimiter) + 1);
                    adaptedLines.add(tmp);
                }

                FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.refactoredFileName), adaptedLines);
                break;
            case SEEDS:

                lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
                adaptedLines = new ArrayList<>();
                for (String line : lines) {
                    //There are some lines that contains 2 tabs next to each other instead of one
                    String tmp = line.replaceAll("\t\t", "\t");
                    adaptedLines.add(tmp);
                }

                FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.refactoredFileName), adaptedLines);
                break;
            case IONOSPHERE:
                lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
                adaptedLines = new ArrayList<>();
                for (String line : lines) {
                    //Change the classification g and b to 1 and 2 respectively
                    String tmp = line.substring(line.lastIndexOf(",") + 1);
                    if (tmp.equals("g")) {
                        tmp = line.substring(0, line.lastIndexOf(",") + 1) + 1;
                    } else {
                        tmp = line.substring(0, line.lastIndexOf(",") + 1) + 2;
                    }
                    adaptedLines.add(tmp);
                }

                FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.refactoredFileName), adaptedLines);
                break;

            case DERMATOLOGY:
                //Remove all lines with missing values
                lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
                adaptedLines = new ArrayList<>();

                for (String line : lines) {
                    if (!line.contains("?")) {
                        adaptedLines.add(line);
                    }
                }

                FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.refactoredFileName), adaptedLines);
                break;

            case IRIS:
                lines = FileUtils.readLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.originalFileName));
                adaptedLines = new ArrayList<>();
                for (String line : lines) {
                    //Change the classification g and b to 1 and 2 respectively
                    String tmp = line.substring(line.lastIndexOf(",") + 1);
                    if (tmp.equals("Iris-setosa")) {
                        tmp = line.substring(0, line.lastIndexOf(",") + 1) + 1;
                    } else if (tmp.equals("Iris-versicolor")) {
                        tmp = line.substring(0, line.lastIndexOf(",") + 1) + 2;
                    } else if (tmp.equals("Iris-virginica")) {
                        tmp = line.substring(0, line.lastIndexOf(",") + 1) + 3;
                    }
                    adaptedLines.add(tmp);
                }

                FileUtils.writeLines(new File(config.getDatasetFilePath() + "/" + dataSetInfo.refactoredFileName), adaptedLines);
                break;
        }

    }
}
