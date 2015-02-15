package net.mlpnn.controller;

import java.util.ArrayList;
import net.mlpnn.dto.Edge;
import net.mlpnn.dto.Node;
import net.mlpnn.dto.SigmaGraphDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author lindes.roets
 */
@Controller
public class ExampleController {

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting"; //by default, spring will look in the templates folder for an html file with the same name
    }

    @RequestMapping("/sigma/example")
    public String sigma(Model model) {
        return "sigma-example";
    }

    @RequestMapping(value = "/sigma/example/{mlpId}", produces = "application/json")
    @ResponseBody
    public SigmaGraphDTO topology(@PathVariable Long mlpId) {
        Node node1 = new Node();
        node1.setId("n0");
        node1.setLabel("A node");
        node1.setX(0);
        node1.setY(0);
        node1.setSize(3);

        Node node2 = new Node();
        node2.setId("n1");
        node2.setLabel("Another node");
        node2.setX(2);
        node2.setY(0);
        node2.setSize(3);

        Node node3 = new Node();
        node3.setId("n2");
        node3.setLabel("And a last one");
        node3.setX(4);
        node3.setY(0);
        node3.setSize(3);
        
        Node node4 = new Node();
        node4.setId("n3");
        node4.setLabel("class b");
        node4.setX(1);
        node4.setY(1);
        node4.setSize(3);
        
        Node node5 = new Node();
        node5.setId("n4");
        node5.setLabel("class a");
        node5.setX(3);
        node5.setY(1);
        node5.setSize(3);        

        Edge edge1 = new Edge("e0", "n0", "n3");

        Edge edge2 = new Edge("e1", "n1", "n3");

        Edge edge3 = new Edge("e2", "n2", "n3");
        
        Edge edge4 = new Edge("e3", "n0", "n4");

        Edge edge5 = new Edge("e4", "n1", "n4");

        Edge edge6 = new Edge("e5", "n2", "n4");        

        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);
        edges.add(edge6);

        SigmaGraphDTO dto = new SigmaGraphDTO();
        dto.setNodes(nodes);
        dto.setEdges(edges);

        return dto;
    }

}
