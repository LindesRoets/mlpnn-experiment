package net.mlpnn.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import net.mlpnn.enums.DataSetInfo;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * The form captures all the inputs from the client to initialize training of a
 * {@link org.neuroph.nnet.MultiLayerPerceptron}
 *
 * @author Lindes Roets
 */
public class MultilayerPercetpronParametersForm implements Serializable {

	private static final long serialVersionUID = -5129638297481151445L;

	/**
	 * Used for display in interface. The name of a specific {@link net.mlpnn.service.MultiLayerPerceptronRunner}
	 */
	@NotEmpty
	private String networkName;

	/**
	 * Defines the number of neurons for the hidden layer
	 */
	@NotNull
	private Integer neuronCount;

	@NotNull
	private Double momentum;

	@NotNull
	private Double learningRate;

	/**
	 * This is the name of the data set that will be used to instantiate the {@link DataSetInfo}
	 */
	@NotEmpty
	private String dataSetName;

	public Integer getNeuronCount() {
		return neuronCount;
	}

	public void setNeuronCount(int neuronCount) {
		this.neuronCount = neuronCount;
	}

	public Double getMomentum() {
		return momentum;
	}

	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

	public Double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

}
