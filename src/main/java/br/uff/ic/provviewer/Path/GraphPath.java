/*
 * The MIT License
 *
 * Copyright 2020 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uff.ic.provviewer.Path;

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class GraphPath {

    /**
     * Method to find a path between two selected vertices
     *
     * @param variables
     * @return the edges that compose the path
     */
    public static Collection<Edge> FindPath(Variables variables) {
        Vertex source;
        Vertex target;
        PickedState<Object> picked_state = variables.view.getPickedVertexState();
        if (!picked_state.getPicked().isEmpty() && picked_state.getPicked().size() > 1) {
            source = (Vertex) picked_state.getPicked().toArray()[0];
            target = (Vertex) picked_state.getPicked().toArray()[1];
//            System.out.println("Source: " + source.getID());
//            System.out.println("target: " + target.getID());
            Map<String, Edge> path = BFS(source, target, variables.layout.getGraph());
            return CleanPath(path, source.getID(), target.getID());
        }
        return null;
    }

    /**
     * Method to calculate the probability of reaching the destination from the
     * source following the cleanedPath path
     *
     * @param variables
     * @param cleanedPath requires the path
     * @return the detailed information in the form of a String for the
     * TooltipDialogBox
     */
    public static String PathProbability(Variables variables, Collection<Edge> cleanedPath) {
        String answer = "";
        if (cleanedPath != null) {
            float probabilityIn = 1;
            float probabilityOut = 1;
            String probPathIn = "";
            String probPathOut = "";
            String path = "";
            PickedState<Edge> picked_edge_state = variables.view.getPickedEdgeState();
            picked_edge_state.clear();
            for (Edge e : cleanedPath) {
                float in = Float.valueOf(e.getAttributeValue(VariableNames.MarkovIn));
                float out = Float.valueOf(e.getAttributeValue(VariableNames.MarkovOut));
                probPathIn += " * " + in;
                probPathOut += " * " + out;
                path += e.getID() + " - > ";
                System.out.print(e.getID() + " - > ");
                probabilityIn = probabilityIn * in;
                probabilityOut = probabilityOut * out;
                picked_edge_state.pick(e, true);
            }
            System.out.println();
//            System.out.println("probability: " + probability);
            PickedState<Object> picked_state = variables.view.getPickedVertexState();
            Vertex source = (Vertex) picked_state.getPicked().toArray()[0];
            Vertex target = (Vertex) picked_state.getPicked().toArray()[1];
            picked_state.clear();
            answer = "Source: " + source.getID()
                    + "\n" + "Target: " + target.getID()
                    + "\n" + "Path: " + path
                    + "\nThe probability of taking this path, linking the selected source to the destination is: "
                    + "\n" + "Prob IN: " + probabilityIn + " ( 1" + probPathIn + ")"
                    + "\n" + "Prob OUT: " + probabilityOut + " ( 1" + probPathOut + ")"
                    + "\n";
        }

//        answer += "<br>The probability of taking this path, linking the selected source to the destination is: ";
        return answer;
    }

    /**
     * Method to compute the Path probability, invoking the methods FindPath and
     * PathProbability
     *
     * @param variables
     * @return the detailed information in the form of a String for the
     * TooltipDialogBox
     */
    public static String ComputePath(Variables variables) {
        return PathProbability(variables, FindPath(variables));
    }

    /**
     * Method to clean the path returned from BFS, removing the paths that did
     * not reach the destination This algorithm backtracks from the Target to
     * the Source
     *
     * @param path is the path returned by BFS
     * @param source is the ID of the source vertex
     * @param target is the ID of the target vertex
     * @return the shortest path
     */
    public static Collection<Edge> CleanPath(Map<String, Edge> path, String source, String target) {
        Collection<Edge> cleanedPath = new ArrayList<>();
        String destination = target;
        while (!destination.equals(source)) {
            cleanedPath.add(path.get(destination));
            destination = ((Vertex) path.get(destination).getTarget()).getID();
        }
        return cleanedPath;
    }
    
    /**
     * Breadth first search algorithm that returns the minimum path if exist
     * @param source is the source vertex
     * @param target is the destination vertex
     * @param graph is the graph
     * @return the list of edges connecting source to target
     */
    public static Map<String, Edge> BFS(Vertex source, Vertex target, Graph graph)
    {

        Map<String, Boolean> visited = new HashMap<>();
        Map<String, Edge> path = new HashMap<>();

        LinkedList<Vertex> queue = new LinkedList<>();
        LinkedList<Edge> edgeQueue = new LinkedList<>();
 
        // Mark the current node as visited and enqueue it
        visited.put(source.getID(), Boolean.TRUE);
        queue.add(source);
 
        while (!queue.isEmpty())
        {
            // Dequeue a vertex from queue and print it
            source = queue.poll();
            Edge next = edgeQueue.poll();
            if(next != null)
                path.put(source.getID(), next);
//            System.out.print(source.getID() + " -> ");
            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            Iterator<Object> i = graph.getInEdges(source).iterator();
            while (i.hasNext())
            {
                Edge edge = (Edge) i.next();
                Vertex n = (Vertex) edge.getSource();
                if (!visited.containsKey(n.getID()))
                {
                    visited.put(n.getID(), Boolean.TRUE);
                    queue.add(n);
                    edgeQueue.add(edge);
                    if(n.getID().equalsIgnoreCase(target.getID())) {
//                        System.out.println("Reached target: " + n.getID());
                        path.put(n.getID(), edge);
                        return path;
                    }
                }
            }
        }
        System.out.println();
        System.out.println("No path exists");
        return null;
    }
}
