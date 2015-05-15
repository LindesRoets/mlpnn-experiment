package net.mlpnn.test.automation;

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
//		for (int i = 80; i < 120; i += 5) {
//			StringBuilder sb = new StringBuilder();
//			sb.append("?neuronCount=").append(i);
//			sb.append("&networkName=").append("Iris - " + i);
//			sb.append("&momentum=").append(.75);
//			sb.append("&learningRate=").append(.75);
//			sb.append("&dataSetName=").append("IRIS");
//			ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:8888/mlp/train" + sb.toString(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
//			System.out.println(entity.getBody());
//			Thread.sleep(5000);
//		}

		for (int i = 25; i < 725; i += 1) {
			StringBuilder sb = new StringBuilder();
			sb.append("?neuronCount=").append(i);
			sb.append("&networkName=").append("Glass - " + i);
			sb.append("&momentum=").append(.75);
			sb.append("&learningRate=").append(.75);
			sb.append("&dataSetName=").append("GLASS");
			ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:8888/mlp/train" + sb.toString(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
			System.out.println(entity.getBody());
			Thread.sleep(30000);
		}

//		for (int i = 80; i < 120; i += 5) {
//			StringBuilder sb = new StringBuilder();
//			sb.append("?neuronCount=").append(i);
//			sb.append("&networkName=").append("Ionosphere - " + i);
//			sb.append("&momentum=").append(.75);
//			sb.append("&learningRate=").append(.75);
//			sb.append("&dataSetName=").append("IONOSPHERE");
//			ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:8888/mlp/train" + sb.toString(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
//			System.out.println(entity.getBody());
//			Thread.sleep(5000);
//		}

//		for (int i = 80; i < 120; i += 5) {
//			StringBuilder sb = new StringBuilder();
//			sb.append("?neuronCount=").append(i);
//			sb.append("&networkName=").append("Dermatology - " + i);
//			sb.append("&momentum=").append(.75);
//			sb.append("&learningRate=").append(.75);
//			sb.append("&dataSetName=").append("DERMATOLOGY");
//			ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:8888/mlp/train" + sb.toString(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
//			System.out.println(entity.getBody());
//			Thread.sleep(5000);
//		}
	}
}
