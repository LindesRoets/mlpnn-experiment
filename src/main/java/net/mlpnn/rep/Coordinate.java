package net.mlpnn.rep;

/**
 *
 * @author Lindes Roets
 */
public class Coordinate {

    public Coordinate(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    private Double x;
    private Double y;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public double[] getArrayFormat() {
        double[] coordinate = new double[2];
        coordinate[0] = getX();
        coordinate[1] = getY();
        return coordinate;
    }
}
