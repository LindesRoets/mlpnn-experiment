package net.mlpnn.enums;

import java.io.Serializable;
import net.mlpnn.service.MultiLayerPerceptronRunner;

/**
 * 
 * Represents the different learning status a given {@link MultiLayerPerceptronRunner} can have.
 *
 * @author Lindes Roets
 */
public enum LearningStatus implements Serializable {

	STOPPED,
	RUNNING,
	PAUSED

}
