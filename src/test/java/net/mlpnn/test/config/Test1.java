package net.mlpnn.test.config;

import net.mlpnn.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Lindes Roets
 */
@Component
public class Test1 {

    @Autowired
    private ApplicationConfiguration config;

    public void setPath(String path) {
        config.setDatasetFilePath(path);
    }

    public String getPath() {
        return config.getDatasetFilePath();
    }
}
