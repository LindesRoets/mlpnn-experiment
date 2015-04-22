package net.mlpnn.test.data;

import java.io.File;
import java.io.IOException;
import net.mlpnn.Application;
import net.mlpnn.ApplicationConfiguration;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.service.DataSetService;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author lindes.roets
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DownloadDataSetTest {

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private ApplicationConfiguration config;

    @Test
    public void testDownloadGlassDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.GLASS);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.GLASS.originalFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testDownloadWineQualityWhiteDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.WINE_QUALITY_WHITE);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.WINE_QUALITY_WHITE.originalFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testDownloadWineQualityRedDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.WINE_QUALITY_RED);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.WINE_QUALITY_RED.originalFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testDownloadSeedsDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.SEEDS);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.SEEDS.originalFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testDownloadIonosphereDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.IONOSPHERE);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.IONOSPHERE.originalFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testDownloadSkinDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.SKIN_SEGMENTATION);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.SKIN_SEGMENTATION.originalFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testDownloadDermatologyDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.DERMATOLOGY);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.DERMATOLOGY.originalFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testDownloadIrisDataSet() throws Exception {
        dataSetService.downloadDataSet(DataSetInfo.IRIS);

        File file = FileUtils.getFile(config.getDatasetFilePath() + "/" + DataSetInfo.IRIS.originalFileName);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.length() > 0);
    }

    @Test
    public void testValidationAndTrainingSetCreationForWineQualityRed() throws IOException {

        dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.WINE_QUALITY_RED);

    }

    @Test
    public void testValidationAndTrainingSetCreationForGlass() throws IOException {

        dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.GLASS);

    }

    @Test
    public void testValidationAndTrainingSetCreation() throws IOException {

        dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.WINE_QUALITY_WHITE);

    }

    @Test
    public void testValidationAndTrainingSetCreationForSeed() throws IOException {

        dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.SEEDS);

    }

    @Test
    public void testValidationAndTrainingSetCreationForIonosphere() throws IOException {

        dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.IONOSPHERE);

    }

    @Test
    public void testValidationAndTrainingSetCreationForSkin() throws IOException {

        dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.SKIN_SEGMENTATION);

    }

    @Test
    public void testValidationAndTrainingSetCreationForDermatology() throws IOException {

        dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.DERMATOLOGY);

    }

    @Test
    public void testValidationAndTrainingSetCreationForIris() throws IOException {

        dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.IRIS);

    }
}
