package net.mlpnn;

import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author Lindes Roets
 */
@ConfigurationProperties(prefix = "config", ignoreUnknownFields = false)
@Component
/**
 * This class gets instantiated with the properties in the application.properties file. It only
 * tries to bind the properties that starts with the prefix config.
 *
 */
public class ApplicationConfiguration implements Serializable {

	private static final long serialVersionUID = -5989809321233681697L;

	/**
	 * The path to the training and validation data sets 
	 */
	private String datasetFilePath;

	public String getDatasetFilePath() {
		return datasetFilePath;
	}

	public void setDatasetFilePath(String datasetFilePath) {
		this.datasetFilePath = datasetFilePath;
	}

}
