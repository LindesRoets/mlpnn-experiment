package net.mlpnn.service;

import java.util.ArrayList;
import java.util.List;
import net.mlpnn.dto.FlotChartDTO;
import net.mlpnn.rep.Coordinate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 *
 * @author Lindes Roets
 */
@Service
public class GraphService {

    public FlotChartDTO getFlotChart(MultiLayerPerceptronRunner runner) {
        Assert.notNull(runner);
        FlotChartDTO flotChartDTO = new FlotChartDTO();
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        if (runner.getTotalNetworkErrors() == null) {
            return null;
        }
        for (int i = 0; i < runner.getTotalNetworkErrors().size(); i++) {
            Coordinate coordinate = new Coordinate(new Double(i + 1), runner.getTotalNetworkErrors().get(i));
            coordinates.add(coordinate);
        }
        flotChartDTO.setCoordinates(coordinates.toArray(new Coordinate[coordinates.size()]));
        return flotChartDTO;
    }

    public double[][] getFlotChartDoubleArray(MultiLayerPerceptronRunner runner) {
        Assert.notNull(runner);

        if (runner.getTotalNetworkErrors() == null) {
            return null;
        }
        double[][] coordinates = new double[runner.getTotalNetworkErrors().size()][2];
        for (int i = 0; i < runner.getTotalNetworkErrors().size(); i++) {
            Coordinate coordinate = new Coordinate(new Double(i + 1), runner.getTotalNetworkErrors().get(i));
            coordinates[i] = coordinate.getArrayFormat();
        }

        return coordinates;
    }

    public double[][] getAllConvergenceErrors(List<MultiLayerPerceptronRunner> runners) {
        Assert.notNull(runners);

        double[][] coordinates = new double[runners.size()][2];
        int count = 0;
        for (MultiLayerPerceptronRunner runner : runners) {
            Coordinate coordinate = new Coordinate(new Double(runner.getForm().getNeuronCount()), new Double(runner.getPerceptron().getLearningRule().getCurrentIteration()));
            coordinates[count] = coordinate.getArrayFormat();
            count++;

        }
        return coordinates;
    }

}
