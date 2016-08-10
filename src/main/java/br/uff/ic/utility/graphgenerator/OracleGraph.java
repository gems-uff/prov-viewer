/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

/**
 *
 * @author Kohwalter
 */
public class OracleGraph {
    
    int MIN_ORACLE_VALUE = -200;
    int MAX_ORACLE_VALUE = 200;
    String attribute;
    int oracleGraphNumber = 0;
    
    /**
     * Constructor
     * @param attribute 
     */
    public OracleGraph(String attribute, int min, int max) {
        this.MIN_ORACLE_VALUE = min;
        this.MAX_ORACLE_VALUE = max;
        this.attribute = attribute;
    }
    
    /**
     * Add the oracle attributes to the oracle vertex
     * @param v is the oracle vertex to be edited
     */
    private void generateOracleValues(Vertex v) {
        GraphAttribute att = new GraphAttribute(attribute, Double.toString(generateOracleNumber()));
        v.addAttribute(att);
        System.out.println("V = " + att.getValue());
        
        att = new GraphAttribute("isOracle", "true");
        v.addAttribute(att);
    }
    
    /**
     * Method to generate a random oracle value for the vertex
     * @return the random value
     */
    private double generateOracleNumber() {
        int min = MIN_ORACLE_VALUE;
        int max = MAX_ORACLE_VALUE;
        return min + (Math.random() * ((max - min) + 1));
    }
    
    /**
     * Create a linear graph with 10 vertices
     * @return the linear graph
     */
    public DirectedGraph<Object, Edge> generateLinearGraph() {
        Vertex v1 = new ActivityVertex("v1_ORACLE", "v1_ORACLE", "0");
        Vertex v2 = new ActivityVertex("v2_ORACLE", "v2_ORACLE", "1");
        Vertex v3 = new ActivityVertex("v3_ORACLE", "v3_ORACLE", "2");
        Vertex v4 = new ActivityVertex("v4_ORACLE", "v4_ORACLE", "3");
        Vertex v5 = new ActivityVertex("v5_ORACLE", "v5_ORACLE", "4");
        Vertex v6 = new ActivityVertex("v6_ORACLE", "v6_ORACLE", "5");
        Vertex v7 = new ActivityVertex("v7_ORACLE", "v7_ORACLE", "6");
        Vertex v8 = new ActivityVertex("v8_ORACLE", "v8_ORACLE", "7");
        Vertex v9 = new ActivityVertex("v9_ORACLE", "v9_ORACLE", "8");
        Vertex v10 = new ActivityVertex("v10_ORACLE", "v10_ORACLE", "9");
        
        generateOracleValues(v1);
        generateOracleValues(v2);
        generateOracleValues(v3);
        generateOracleValues(v4);
        generateOracleValues(v5);
        generateOracleValues(v6);
        generateOracleValues(v7);
        generateOracleValues(v8);
        generateOracleValues(v9);
        generateOracleValues(v10);
        
        Edge e1 = new Edge("E1", "", "", "", v1, v2);
        Edge e2 = new Edge("E2", "", "", "", v2, v3);
        Edge e3 = new Edge("E3", "", "", "", v3, v4);
        Edge e4 = new Edge("E4", "", "", "", v4, v5);
        Edge e5 = new Edge("E5", "", "", "", v5, v6);
        Edge e6 = new Edge("E6", "", "", "", v6, v7);
        Edge e7 = new Edge("E7", "", "", "", v7, v8);
        Edge e8 = new Edge("E8", "", "", "", v8, v9);
        Edge e9 = new Edge("E9", "", "", "", v9, v10);
        
        DirectedGraph<Object, Edge> templateGraph = new DirectedSparseMultigraph<>();
        templateGraph.addEdge(e1, e1.getSource(), e1.getTarget());
        templateGraph.addEdge(e2, e2.getSource(), e2.getTarget());
        templateGraph.addEdge(e3, e3.getSource(), e3.getTarget());
        templateGraph.addEdge(e4, e4.getSource(), e4.getTarget());
        templateGraph.addEdge(e5, e5.getSource(), e5.getTarget());
        templateGraph.addEdge(e6, e6.getSource(), e6.getTarget());
        templateGraph.addEdge(e7, e7.getSource(), e7.getTarget());
        templateGraph.addEdge(e8, e8.getSource(), e8.getTarget());
        templateGraph.addEdge(e9, e9.getSource(), e9.getTarget());
        
        Utils.exportGraph(templateGraph, "oracleLinearGraph" + "_" + oracleGraphNumber);
        oracleGraphNumber++;
        return templateGraph;
    }
    
}
