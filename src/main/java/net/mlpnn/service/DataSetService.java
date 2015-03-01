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
        File file = new File(config.getDatasetFilePath() + "/" + info.localFileName);
        FileUtils.copyURLToFile(new URL(info.url), file);
    }

    public void createTestAndValidationDataForRegressor(DataSetInfo dataSetInfo) throws IOException {
        String filePath = config.getDatasetFilePath() + "/" + dataSetInfo.localFileName;
        DataSet dataSet = DataSet.createFromFile(filePath, dataSetInfo.numberOfInputs, dataSetInfo.numberOfOutputs, dataSetInfo.delimiter, true);

        Random r = new Random(1);
        Collections.shuffle(dataSet.getRows(), r);

        List<List<DataSetRow>> dataSetGroups = new ArrayList<>();

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            if (dataSetGroups.isEmpty()) {
                List<DataSetRow> rows = new ArrayList<>();
                rows.add(dataSetRow);
                dataSetGroups.add(rows);
            } else {
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
        }

        int smallestGroupSize = Integer.MAX_VALUE;
        for (List<DataSetRow> groups : dataSetGroups) {
            if (groups.size() < smallestGroupSize) {
                smallestGroupSize = groups.size();
            }
            System.out.println("Size: " + groups.size() + "\t" + Arrays.toString(groups.get(0).getDesiredOutput()));
        }

        System.out.println("Smallest group size: " + smallestGroupSize);

        File trainingFile = new File(config.getDatasetFilePath() + "/" + dataSetInfo.trainingFileName);
        File validationFile = new File(config.getDatasetFilePath() + "/" + dataSetInfo.validationFileName);

        List<String> trainingFileLines = new ArrayList<>();
        List<String> validationFileLines = new ArrayList<>();
        
        for (int i = 0; i < Math.round(smallestGroupSize / 2.0); i++) {
            for(List<DataSetRow> rows : dataSetGroups){
                trainingFileLines.add(rows.get(i).toCSV());
            }
        
        }
        for (long i = Math.round(smallestGroupSize / 2.0); i != smallestGroupSize; i++) {
            for(List<DataSetRow> rows : dataSetGroups){
                validationFileLines.add(rows.get((int)i).toCSV());
            }
        }
        
        FileUtils.writeLines(trainingFile, trainingFileLines);
        FileUtils.writeLines(validationFile, validationFileLines);
    }
}
