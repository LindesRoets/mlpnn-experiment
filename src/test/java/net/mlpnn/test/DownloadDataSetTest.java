package net.mlpnn.test;

import java.io.File;
import java.io.IOException;
import net.mlpnn.Application;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.service.DataSetService;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neuroph.core.data.DataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author lindes.roets
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DownloadDataSetTest {

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private ApplicationConfiguration config;

    @Test
    public void testDownloadMushroomDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.MUSHROOMS);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.MUSHROOMS.localFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testDownloadWineQualityRedDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.WINE_QUALITY_RED);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.WINE_QUALITY_RED.localFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testDataSetShuffle() throws IOException {

        dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.WINE_QUALITY_RED);      
    
    }

}
