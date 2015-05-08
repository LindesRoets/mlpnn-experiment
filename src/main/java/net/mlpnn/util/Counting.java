package net.mlpnn.util;

import java.util.Collection;
import java.util.function.Predicate;
import net.mlpnn.enums.LearningStatus;
import net.mlpnn.service.MultiLayerPerceptronRunner;

/**
 *
 * @author Lindes Roets
 */
public class Counting {

	public static long getLearningThreadCount(Collection<MultiLayerPerceptronRunner> runners) {

		Predicate<MultiLayerPerceptronRunner> learningThreadFilter = (MultiLayerPerceptronRunner p) -> p.calculateLearningStatus() == LearningStatus.RUNNING;
		return runners.stream().filter(learningThreadFilter).count();
	}

	public static long getPausedThreadCount(Collection<MultiLayerPerceptronRunner> runners) {

		Predicate<MultiLayerPerceptronRunner> pausedThreadFilter = (MultiLayerPerceptronRunner p) -> p.calculateLearningStatus() == LearningStatus.PAUSED;
		return runners.stream().filter(pausedThreadFilter).count();
	}

	public static long getStoppedThreadCount(Collection<MultiLayerPerceptronRunner> runners) {

		Predicate<MultiLayerPerceptronRunner> learningThreadFilter = (MultiLayerPerceptronRunner p) -> p.calculateLearningStatus() == LearningStatus.STOPPED;
		return runners.stream().filter(learningThreadFilter).count();
	}

}
