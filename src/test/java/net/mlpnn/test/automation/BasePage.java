package net.mlpnn.test.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 *
 * @author lroets
 */
public class BasePage {

    protected WebDriver driver;

    public BasePage() {
    }

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public void initPage() {
        PageFactory.initElements(this.driver, this);
    }
}
