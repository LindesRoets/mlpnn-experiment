package net.mlpnn.util;

import java.util.Comparator;
import java.util.List;
import net.mlpnn.service.MultiLayerPerceptronRunner;

/**
 *
 * @author Lindes Roets
 */
public class Sorting {

	public static List<MultiLayerPerceptronRunner> sortRunnersByNeuronCount(List<MultiLayerPerceptronRunner> runners) {
		//Sort runners by the neuron count
		Comparator<MultiLayerPerceptronRunner> byNeuronCount = (MultiLayerPerceptronRunner runner1, MultiLayerPerceptronRunner runner2) -> Integer
			.compare(runner1.getForm().getNeuronCount(), runner2.getForm().getNeuronCount());
		runners.sort(byNeuronCount);
		return runners;
	}
	
	

}
