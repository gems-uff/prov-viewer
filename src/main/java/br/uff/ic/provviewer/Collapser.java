/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
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
package br.uff.ic.provviewer;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.GraphVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Class to Collapse/Expand selected vertices and edges
 *
 * @author Kohwalter
 *
 * TODO: Collapsing only with same Source. Need to collpase with same target as
 * well
 */
public class Collapser {

    /**
     * Method for resetting the Graph to the original state
     *
     * @param variables Variables type
     */
    public void ResetGraph(Variables variables) {
        //Reset graph to the original version
        variables.layout.setGraph(variables.graph);
        variables.collapsedGraph = variables.graph;
        variables.initGraphCollapser();
//        variables.ComputeEdgeTypeValues();
        variables.filter.RemoveFilters(variables);
    }

    //
    /**
     * Method to remove collapsed edges from the node and un-hide edges
     *
     * @param variables Variable type
     * @param vertex Vertex used to get incident edges
     */
    public void RemoveCollapsedEdges(Variables variables, Object vertex) {
        Graph newGraph = variables.layout.getGraph();
        List edges = new ArrayList(variables.layout.getGraph().getIncidentEdges(vertex));
        for (Object edge : edges) {
            ((Edge) edge).setHide(false);
            if (((Edge) edge).isCollapased()) {
                //remove edge
                newGraph.removeEdge(edge);
            }
        }
    }

    /**
     * Method used to collapse vertices
     *
     * @param variables Variables type
     * @param picked Collection of selected vertices
     * @param refresh
     */
    public void Collapse(Variables variables, Collection picked, boolean refresh) {
        //add all filters to avoid losing information
        variables.filter.AddFilters(variables);
        //If selected more than 1 vertex
        if (picked.size() > 1) {
            //Graph inGraph = layout.getGraph();
            GraphVertex clusterGraph = variables.gCollapser.getClusterGraph(variables.layout.getGraph(), picked);

            variables.collapsedGraph = (DirectedGraph<Object, Edge>) variables.gCollapser.collapse(variables.layout.getGraph(), clusterGraph);
            //Compute the collapsed vertex position from each vertex in it
            double sumx = 0;
            double sumy = 0;
            for (Object v : picked) {
                Point2D p = (Point2D) variables.layout.transform(v);
                sumx += p.getX();
                sumy += p.getY();
            }

            //store position
            Point2D cp = new Point2D.Double(sumx / picked.size(), sumy / picked.size());
            variables.view.getRenderContext().getParallelEdgeIndexFunction().reset();
            //set layout to be the collapsed graph
            variables.layout.setGraph(variables.collapsedGraph);
            //set the collapsed vertex position
            variables.layout.setLocation(clusterGraph, cp);
            variables.view.getPickedVertexState().clear();
            
            if(refresh)
                RefreshGraph(variables);
        }

    }
    
    /**
     * Function to refresh the displayed graph
     * @param variables
     */
    public void RefreshGraph(Variables variables)
    {
//        variables.ComputeEdgeTypeValues();
        variables.filter.RemoveFilters(variables);
        variables.view.repaint();
    }

    /**
     * Method for expanding the selected vertices, if they are collapsed
     * vertices
     *
     * @param variables Variables type
     * @param picked Collection of selected vertices
     */
    public void Expander(Variables variables, Collection picked) {
        //for each selected vertex
        for (Object v : picked) {
            //if vertex is a collapsed graph (multiple vertices)
            if (v instanceof GraphVertex) {
                //TODO: Save current filters state
                //Add all filters to not lose edges/information
                variables.filter.AddFilters(variables);
                //Expand the vertex
                variables.collapsedGraph = (DirectedGraph<Object, Edge>) variables.gCollapser.expand(variables.layout.getGraph(), (GraphVertex) v);
                variables.view.getRenderContext().getParallelEdgeIndexFunction().reset();
                variables.layout.setGraph(variables.collapsedGraph);
                //TODO: Load filters state
                //Remove filters to clean the visualization
                variables.filter.RemoveFilters(variables);
//                Filters(variables);
            }
        }
        variables.view.getPickedVertexState().clear();
//        variables.ComputeEdgeTypeValues();
        variables.view.repaint();
    }

    public void CollapseIrrelevant(Variables variables, String list) {

        Collection selected = new ArrayList();
        //System.out.println( "L = " + list);
        List<String> collapsegroup = new ArrayList<>();
        List<String> used = new ArrayList<>();

        String[] elements = list.split(" + ");

        collapsegroup.addAll(Arrays.asList(elements));
        //Sort list by decreasing order of string.length
        Comparator comparator = new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                return c2.length() - c1.length();
            }
        };
        Collections.sort(collapsegroup, comparator);

        Object[] nodes = new Object[variables.layout.getGraph().getVertexCount()];
        nodes = (variables.layout.getGraph().getVertices()).toArray();

        //For each elements of collapses
        for (String collapsegroup1 : collapsegroup) {
//            System.out.println("Current Group = " + collapsegroup1);
            String cg1 = collapsegroup1.replace(" +", "");
//            System.out.println("Current Group = " + cg1);
//            String group = "";
            String[] vertexlist = cg1.split(" / ");
            //For each vertex in the elements
            for (String vertexlist1 : vertexlist) {
                //If vertex was not processed yet
                String vl1 = vertexlist1.replace("/ ", "");
                if (!used.contains(vl1)) {
                    used.add(vl1);
                    //Find the vertex in the graph by its ID
                    for (Object node1 : nodes) {
                        if (((Vertex) (node1)).getID().equalsIgnoreCase(vl1)) {
                            Vertex node = (Vertex) node1;
                            selected.add(node);
//                            group += "," + vertexlist1;
                        }
                    }
                }
            }
//            System.out.println("Current Group = " + group);
            //Collapse selected vertices
            if (!selected.isEmpty() && (selected.size() > 1)) {
                Collapse(variables, selected, false);
            } else {
                //If there is only one vertex, then there is no collapse
                Iterator itr = selected.iterator();
                while (itr.hasNext()) {
                    used.remove(((Vertex) itr.next()).getID());
                }
            }
            selected.clear();
        }
        RefreshGraph(variables);
    }

}