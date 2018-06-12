package com.stemby.commons.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.stemby.commons.util.Matrix;

/**
 * Class that includes algorithms on graphs. It contains only static methods,
 * so it is not possible to use any constructor.
 * 
 * @author stemby
 */
public class Graph {

    private Graph() {}

    /**
     * It returns a list of the connected components of a graph. The graph is
     * represented using an adjacency matrix. Every connected component is
     * represented using a set of nodes (indexes of the adjacency matrix).
     * 
     * @param   matrix  The adjacency matrix that represents the graph.
     * @return          A list of the connected components of the given graph.
     */
    public static List<Set<Integer>> getConnectedComponents(Matrix matrix) {
        List<Set<Integer>> connectedComponentList = new ArrayList<>();
        int nodeCount = matrix.getRowCount();
        boolean[] visited = new boolean[nodeCount];
        for (int node = 0; node < nodeCount; node++) {
            if (!visited[node]) {
                Set<Integer> connectedComponent = new HashSet<>();
                List<Integer> nodeToVisitList = new ArrayList<>();
                nodeToVisitList.add(node);
                while (!nodeToVisitList.isEmpty()) {
                    Integer nextNodeToVisit = nodeToVisitList.remove(nodeToVisitList.size()-1);
                    if (!visited[nextNodeToVisit]) {
                        for (int neighbourNode = 0; neighbourNode < nodeCount; neighbourNode++) {
                            if (matrix.getAsFloat(nextNodeToVisit, neighbourNode) > 0) {
                                nodeToVisitList.add(neighbourNode);
                            }
                        }
                        visited[nextNodeToVisit] = true;
                        connectedComponent.add(nextNodeToVisit);
                    }
                }
                connectedComponentList.add(connectedComponent);
            }
        }
        return connectedComponentList;
    }

}