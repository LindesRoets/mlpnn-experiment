package net.mlpnn.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Lindes Roets
 */
@Component
public class Test3 {

    @Autowired
    private ApplicationConfigurationPrototype config;

    public void setPath(String path) {
        config.setDatasetFilePath(path);
    }

    public String getPath() {
        return config.getDatasetFilePath();
    }
}
