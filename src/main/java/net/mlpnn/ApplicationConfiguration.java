package net.mlpnn;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author Lindes Roets
 */
@ConfigurationProperties(prefix = "config", ignoreUnknownFields = false)
@Component
public class ApplicationConfiguration {

    private String datasetFilePath;

    public String getDatasetFilePath() {
        return datasetFilePath;
    }

    public void setDatasetFilePath(String datasetFilePath) {
        this.datasetFilePath = datasetFilePath;
    }

}
