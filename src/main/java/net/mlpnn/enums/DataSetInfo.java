package net.mlpnn.enums;

/**
 *
 * @author Lindes Roets
 */
public enum DataSetInfo {

    IRIS("http://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data", "iris.csv", "iris-refactored.csv", "iris-training.csv", "iris-validation.csv", 4, 1, ",", false),
    SEEDS("http://archive.ics.uci.edu/ml/machine-learning-databases/00236/seeds_dataset.txt", "seeds.csv", "seeds-refactored.csv", "seeds-training.csv", "seeds-validation.csv", 7, 1, "\t", false),
    WINE_QUALITY_RED("http://archive.ics.uci.edu/ml/machine-learning-databases/wine-quality/winequality-red.csv", "winequality-red.csv", "winequality-red.csv", "winequality-red-training.csv", "winequality-red-validation.csv", 11, 1, ";", true),
    WINE_QUALITY_WHITE("http://archive.ics.uci.edu/ml/machine-learning-databases/wine-quality/winequality-white.csv", "winequality-white.csv", "winequality-white.csv", "winequality-white-training.csv", "winequality-white-validation.csv", 11, 1, ";", true),
    GLASS("http://archive.ics.uci.edu/ml/machine-learning-databases/glass/glass.data", "glass.csv", "glass-refactored.csv", "glass-training.csv", "glass-validation.csv", 9, 1, ",", false),
    SKIN_SEGMENTATION("http://archive.ics.uci.edu/ml/machine-learning-databases/00229/Skin_NonSkin.txt", "skin-non-skin.csv", "skin-non-skin.csv", "skin-non-skin-training.csv", "skin-non-skin-validation.csv", 3, 1, "\t", false),
    DERMATOLOGY("http://archive.ics.uci.edu/ml/machine-learning-databases/dermatology/dermatology.data", "dermatology.csv", "dermatology-refactored.csv", "dermatology-training.csv", "dermatology-validation.csv", 34, 1, ",", false),
    IONOSPHERE("http://archive.ics.uci.edu/ml/machine-learning-databases/ionosphere/ionosphere.data", "ionosphere.csv", "ionosphere-refactored.csv", "ionosphere-training.csv", "ionosphere-validation.csv", 34, 1, ",", false);

    public String url;
    public String originalFileName;
    public String refactoredFileName;
    public String trainingFileName;
    public String validationFileName;
    public int numberOfInputs;
    public int numberOfOutputs;
    public String delimiter;
    public boolean containsHeader;

    private DataSetInfo(String url, String originalFileName, String refactoredFileName, String trainingFileName, String validationFileName, int numberOfInputs, int numberOfOutputs, String delimiter, boolean containsHeader) {
        this.url = url;
        this.originalFileName = originalFileName;
        this.refactoredFileName = refactoredFileName;
        this.trainingFileName = trainingFileName;
        this.validationFileName = validationFileName;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.delimiter = delimiter;
        this.containsHeader = containsHeader;
    }

}
