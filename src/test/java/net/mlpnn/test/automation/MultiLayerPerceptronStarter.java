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
		for (int i = 700; i < 1001; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("?neuronCount=").append(i);
			sb.append("&networkName=").append("Dermatology - "+i);
			sb.append("&momentum=").append(.75);
			sb.append("&learningRate=").append(.75);
			sb.append("&dataSetName=").append("DERMATOLOGY");
			ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:8888/mlp/train"+sb.toString(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
			System.out.println(entity.getBody());
			Thread.sleep(5000);
		}

	}
}
