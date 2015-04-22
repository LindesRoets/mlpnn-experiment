package net.mlpnn.test.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import net.mlpnn.enums.LearningStatus;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import net.mlpnn.service.MultiLayerPerceptronRunner;
import net.mlpnn.util.Counting;
import org.junit.Assert;
import org.junit.Test;
import org.neuroph.nnet.learning.BackPropagation;

/**
 *
 * @author Lindes Roets
 */
public class CollectionsTest {

	@Test
	public void testShuffleWithSameRandomizer() {

		List<Integer> numbers = getOneToTenList();
		List<Integer> numbers2 = getOneToTenList();

		Random r = new Random(1);
		Collections.shuffle(numbers, r);
		Collections.shuffle(numbers2, r);

		Assert.assertEquals(numbers.get(1), numbers2.get(1));

	}

	@Test
	public void testMultiLayerPerceptronRunnerSorting() {
		MultilayerPercetpronParametersForm form1 = new MultilayerPercetpronParametersForm();
		form1.setNeuronCount(2);

		MultilayerPercetpronParametersForm form2 = new MultilayerPercetpronParametersForm();
		form2.setNeuronCount(4);

		MultilayerPercetpronParametersForm form3 = new MultilayerPercetpronParametersForm();
		form3.setNeuronCount(6);

		List<MultiLayerPerceptronRunner> runners = new ArrayList<>();
		runners.add(new MultiLayerPerceptronRunner(null, form3));
		runners.add(new MultiLayerPerceptronRunner(null, form2));
		runners.add(new MultiLayerPerceptronRunner(null, form1));

		Assert.assertEquals((Integer) 6, runners.get(0).getForm().getNeuronCount());

		Comparator<MultiLayerPerceptronRunner> byNeuronCount = (runner1, runner2)
			-> Integer.compare(runner1.getForm().getNeuronCount(), runner2.getForm().getNeuronCount());

		runners.sort(byNeuronCount);

		Assert.assertEquals((Integer) 2, runners.get(0).getForm().getNeuronCount());
	}

	@Test
	public void testCounting() {
		MultilayerPercetpronParametersForm form1 = new MultilayerPercetpronParametersForm();
		form1.setNeuronCount(2);

		MultilayerPercetpronParametersForm form2 = new MultilayerPercetpronParametersForm();
		form2.setNeuronCount(4);

		MultilayerPercetpronParametersForm form3 = new MultilayerPercetpronParametersForm();
		form3.setNeuronCount(6);
		
		

		List<MultiLayerPerceptronRunner> runners = new ArrayList<>();
		MultiLayerPerceptronRunner runner1 = new MultiLayerPerceptronRunner(null, form3);
		MultiLayerPerceptronRunner runner2 = new MultiLayerPerceptronRunner(null, form3);
		MultiLayerPerceptronRunner runner3 = new MultiLayerPerceptronRunner(null, form3);
		runners.add(runner1);
		runners.add(runner2);
		runners.add(runner3);
		
		//all are stopped since the perceptron is null in this test
		Assert.assertEquals(3l, Counting.getStoppedThreadCount(runners));
	}

	private List<Integer> getOneToTenList() {
		List<Integer> numbers = new ArrayList<>();

		numbers.add(1);
		numbers.add(2);
		numbers.add(3);
		numbers.add(4);
		numbers.add(5);
		numbers.add(6);
		numbers.add(7);
		numbers.add(8);
		numbers.add(9);
		numbers.add(10);
		return numbers;
	}

}
