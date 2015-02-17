package net.mlpnn.test;

import java.util.ArrayList;
import net.mlpnn.dto.FlotChartDTO;
import net.mlpnn.service.GraphService;
import net.mlpnn.service.MultiLayerPerceptronRunner;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lindes Roets
 */
public class GraphServiceUnitTest {

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenRunnerIsNull() {
        GraphService gs = new GraphService();
        FlotChartDTO dto = gs.getFlotChart(null);
    }

    @Test
    public void testNullReturnedWhenTotlaNetworkErrorsIsNull() {
        GraphService gs = new GraphService();
        MultiLayerPerceptronRunner runner = new MultiLayerPerceptronRunner(null, null);
        runner.setTotalNetworkErrors(null);
        FlotChartDTO dto = gs.getFlotChart(runner);
        Assert.assertNull(dto);
    }

    @Test
    public void testCorrectBuildingOfFlotChartDTO() {
        GraphService gs = new GraphService();
        MultiLayerPerceptronRunner runner = new MultiLayerPerceptronRunner(null, null);
        ArrayList<Double> totalNetworkErrors = new ArrayList<>();
        totalNetworkErrors.add(2d);
        totalNetworkErrors.add(3d);
        totalNetworkErrors.add(4d);
        runner.setTotalNetworkErrors(totalNetworkErrors);

        FlotChartDTO dto = gs.getFlotChart(runner);
        Assert.assertEquals(new Double(1d), dto.getCoordinates()[0].getX());
        Assert.assertEquals(totalNetworkErrors.get(0), dto.getCoordinates()[0].getY());

        Assert.assertEquals(new Double(2d), dto.getCoordinates()[1].getX());
        Assert.assertEquals(totalNetworkErrors.get(1), dto.getCoordinates()[1].getY());

        Assert.assertEquals(new Double(3d), dto.getCoordinates()[2].getX());
        Assert.assertEquals(totalNetworkErrors.get(2), dto.getCoordinates()[2].getY());

    }
}
