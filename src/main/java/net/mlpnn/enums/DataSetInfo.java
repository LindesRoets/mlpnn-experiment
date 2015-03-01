package net.mlpnn.enums;

/**
 *
 * @author Lindes Roets
 */
public enum DataSetInfo {

    MUSHROOMS("http://archive.ics.uci.edu/ml/machine-learning-databases/mushroom/agaricus-lepiota.data", "mushrooms.csv", "mushrooms-training.csv", "mushrooms-validation.csv", 11, 1, ";"),
    SEEDS("http://archive.ics.uci.edu/ml/machine-learning-databases/00236/seeds_dataset.txt", "seeds.csv", "seeds-training.csv", "seeds-validation.csv", 11, 1, ";"),
    WINE_QUALITY_RED("http://archive.ics.uci.edu/ml/machine-learning-databases/wine-quality/winequality-red.csv", "winequality-red.csv", "winequality-red-training.csv", "winequality-red-validation.csv", 11, 1, ";"),
    GLASS("http://archive.ics.uci.edu/ml/machine-learning-databases/glass/glass.data", "glass.csv", "glass-training.csv", "glass-validation.csv", 11, 1, ";"),
    SKIN_SEGMENTATION("http://archive.ics.uci.edu/ml/machine-learning-databases/00229/Skin_NonSkin.txt", "skin-non-skin.csv", "skin-non-skin-training.csv", "skin-non-skin-validation.csv", 11, 1, ";"),
    CONCRETE_STRENGTH("http://archive.ics.uci.edu/ml/machine-learning-databases/concrete/compressive/Concrete_Data.xls", "concrete-data.xls", "concrete-data-training.xls", "concrete-data-validation.xls", 11, 1, ";"),
    ACCUTE_INFLAMMATION("http://archive.ics.uci.edu/ml/machine-learning-databases/acute/diagnosis.data", "bladder-diagnosis.csv", "bladder-diagnosis-training.csv", "bladder-diagnosis-validation.csv", 11, 1, ";"),
    DERMATOLOGY("http://archive.ics.uci.edu/ml/machine-learning-databases/dermatology/dermatology.data", "dermatology.csv", "dermatology-training.csv", "dermatology-validation.csv", 11, 1, ";"),
    IONOSPHERE("http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data", "ionosphere.csv", "ionosphere-training.csv", "ionosphere-validation.csv", 11, 1, ";"),
    LEAF("http://archive.ics.uci.edu/ml/machine-learning-databases/00288/leaf.zip", "leaf.zip", "leaf-training.zip", "leaf-validation.zip", 11, 1, ";");

    public String url;
    public String localFileName;
    public String trainingFileName;
    public String validationFileName;
    public int numberOfInputs;
    public int numberOfOutputs;
    public String delimiter;

    private DataSetInfo(String url, String localFileName, String trainingFileName, String validationFileName, int numberOfInputs, int numberOfOutputs, String delimiter) {
        this.url = url;
        this.localFileName = localFileName;
        this.trainingFileName = trainingFileName;
        this.validationFileName = validationFileName;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.delimiter = delimiter;
    }

}
