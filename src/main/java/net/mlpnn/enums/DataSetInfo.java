package net.mlpnn.enums;

/**
 *
 * @author Lindes Roets
 */
public enum DataSetInfo {

    MUSHROOMS("http://archive.ics.uci.edu/ml/machine-learning-databases/mushroom/agaricus-lepiota.data", "mushrooms.csv", 11, 1, ";"),
    SEEDS("http://archive.ics.uci.edu/ml/machine-learning-databases/00236/seeds_dataset.txt", "seeds.csv", 11, 1, ";"),
    WINE_QUALITY_RED("http://archive.ics.uci.edu/ml/machine-learning-databases/wine-quality/winequality-red.csv", "winequality-red.csv", 11, 1, ";"),
    GLASS("http://archive.ics.uci.edu/ml/machine-learning-databases/glass/glass.data", "glass.csv", 11, 1, ";"),
    SKIN_SEGMENTATION("http://archive.ics.uci.edu/ml/machine-learning-databases/00229/Skin_NonSkin.txt", "skin-non-skin.csv", 11, 1, ";"),
    CONCRETE_STRENGTH("http://archive.ics.uci.edu/ml/machine-learning-databases/concrete/compressive/Concrete_Data.xls", "concrete-data.xls", 11, 1, ";"),
    ACCUTE_INFLAMMATION("http://archive.ics.uci.edu/ml/machine-learning-databases/acute/diagnosis.data", "bladder-diagnosis.csv", 11, 1, ";"),
    DERMATOLOGY("http://archive.ics.uci.edu/ml/machine-learning-databases/dermatology/dermatology.data", "dermatology.csv", 11, 1, ";"),
    IONOSPHERE("http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data", "ionosphere.csv", 11, 1, ";"),
    LEAF("http://archive.ics.uci.edu/ml/machine-learning-databases/00288/leaf.zip", "leaf.zip", 11, 1, ";");

    public String url;
    public String localFileName;
    public int numberOfInputs;
    public int numberOfOutputs;
    public String delimiter;

    DataSetInfo(String url, String localFileName, int numberOfInputs, int numberOfOutputs, String delimiter) {
        this.url = url;
        this.localFileName = localFileName;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.delimiter = delimiter;
    }
}
