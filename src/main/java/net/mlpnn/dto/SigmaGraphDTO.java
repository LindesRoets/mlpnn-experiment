package net.mlpnn.dto;

import net.mlpnn.rep.Node;
import net.mlpnn.rep.Edge;
import java.util.List;

/**
 *
 * @author Lindes Roets
 */
public class SigmaGraphDTO {

    private List<Node> nodes;
    private List<Edge> edges;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}
