package com.company;

import java.util.List;
import java.util.ArrayList;

public class Node {
    private Node parent;
    private String attribute;
    private ArrayList<Edge> edges;
    private List<Node> children;

    public List<Node> getChildren() {
        if(edges == null || edges.isEmpty()) { return null; }

        List<Node> children = new ArrayList<>();
        for(Edge e : edges) {
            children.add(e.getChild());
        }
        return children;
    }
}
