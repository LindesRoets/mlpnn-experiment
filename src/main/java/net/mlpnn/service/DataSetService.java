package net.mlpnn.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.enums.DataSetInfo;
import org.apache.commons.io.FileUtils;
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
}
