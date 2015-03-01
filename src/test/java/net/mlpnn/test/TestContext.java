package net.mlpnn.test;

import org.openqa.selenium.WebDriver;

/**
 *
 * @author lroets
 */
public class TestContext {

	public static WebDriver DRIVER;

	public static String BASE_URL = "http://localhost:8888";

	public static String printContextInfo() {
		StringBuilder sb = new StringBuilder("\nTestData:");
		sb.append("\nEnvironment: ").append(BASE_URL);
		sb.append("\n\n");
		return sb.toString();
	}

	public static void quitDriver() {

		DRIVER.quit();
		DRIVER = null;
	}
}
