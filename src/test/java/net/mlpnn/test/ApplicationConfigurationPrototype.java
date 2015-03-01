package net.mlpnn.test;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Lindes Roets
 */
@ConfigurationProperties(prefix = "config", ignoreUnknownFields = false)
@Component
@Scope(value = "prototype")
public class ApplicationConfigurationPrototype {

    private String datasetFilePath;

    public String getDatasetFilePath() {
        return datasetFilePath;
    }

    public void setDatasetFilePath(String datasetFilePath) {
        this.datasetFilePath = datasetFilePath;
    }

}
