package net.mlpnn.test;

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
		for (int i = 2; i < 10; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("?neuronCount=").append(i);
			sb.append("&networkName=").append("Glass - "+i);
			sb.append("&momentum=").append(.75);
			sb.append("&learningRate=").append(.75);
			sb.append("&dataSetName=").append("GLASS");
			ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:8888/mlp/train"+sb.toString(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
			System.out.println(entity.getBody());
			Thread.sleep(5000);
		}

	}
}
