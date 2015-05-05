package net.mlpnn.test.automation;

import java.util.HashMap;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Lindes Roets
 */
public class MultiLayerPerceptronStarter {

	@Test
	public void testDataSet() throws InterruptedException {
		for (int i = 2; i < 100; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("?neuronCount=").append(i);
			sb.append("&networkName=").append("Iris - "+i);
			sb.append("&momentum=").append(.75);
			sb.append("&learningRate=").append(.75);
			sb.append("&dataSetName=").append("IRIS");
			ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:8888/mlp/train"+sb.toString(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
			System.out.println(entity.getBody());
			Thread.sleep(5000);
		}

	}
}
