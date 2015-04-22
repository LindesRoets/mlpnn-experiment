package net.mlpnn.test.automation;

import net.mlpnn.constants.ResourcePath;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author lroets
 */
public class MultiLayerPerceptronCreatePage extends BasePage {

    public String createURL = TestContext.BASE_URL + ResourcePath.MLP_BASE + ResourcePath.MLP_CREATE;

    @FindBy(id = "networkName")
    public WebElement networkName;

    @FindBy(id = "neuronCount")
    public WebElement neuronCount;

    @FindBy(id = "momentum")
    public WebElement momentum;

    @FindBy(id = "learningRate")
    public WebElement learningRate;

    @FindBy(id = "dataSetName")
    public WebElement dataSetName;
    
    @FindBy(id = "mlpCreateSubmit")
    public WebElement mlpCreateSubmit;

    public MultiLayerPerceptronCreatePage(WebDriver driver) {
        super(driver);
        this.driver.get(createURL);
        initPage();
    }

    public void completeForm(String networkName, String neuronCount, String momentum, String learningRate, String dataSetName) {
        this.networkName.sendKeys(networkName);
        this.neuronCount.sendKeys(neuronCount);
        this.momentum.sendKeys(momentum);
        this.learningRate.sendKeys(learningRate);
        this.dataSetName.sendKeys(dataSetName);
    }
    
    public void submitForm(){
        mlpCreateSubmit.click();
    }

}
