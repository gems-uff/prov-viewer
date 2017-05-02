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

package br.uff.ic.utility.graphgenerator;

import br.uff.ic.utility.GraphAttribute;
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
        GraphAttribute att = new GraphAttribute(attribute, String.valueOf(generateOracleNumber()));
        v.addAttribute(att);
        if(att.getValue().equalsIgnoreCase("NaN"))
            System.out.println("Oracle value NaN!");
//        System.out.println("V = " + att.getValue());
        
        att = new GraphAttribute("isOracle", "true");
        v.addAttribute(att);
    }
    
    /**
     * Method to generate a random oracle value for the vertex
     * @return the random value
     */
    private float generateOracleNumber() {
        int min = MIN_ORACLE_VALUE;
        int max = MAX_ORACLE_VALUE;
        float value = (float) (min + (Math.random() * ((max - min) + 1)));
        return value;
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
       
        oracleGraphNumber++;
        return templateGraph;
    }
    
    public DirectedGraph<Object, Edge> generateLinearGraphBig() {
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
        Vertex v11 = new ActivityVertex("v11_ORACLE", "v11_ORACLE", "10");
        Vertex v12 = new ActivityVertex("v12_ORACLE", "v12_ORACLE", "11");
        Vertex v13 = new ActivityVertex("v13_ORACLE", "v13_ORACLE", "12");
        Vertex v14 = new ActivityVertex("v14_ORACLE", "v14_ORACLE", "13");
        Vertex v15 = new ActivityVertex("v15_ORACLE", "v15_ORACLE", "14");
        Vertex v16 = new ActivityVertex("v16_ORACLE", "v16_ORACLE", "15");
        Vertex v17 = new ActivityVertex("v17_ORACLE", "v17_ORACLE", "16");
        Vertex v18 = new ActivityVertex("v18_ORACLE", "v18_ORACLE", "17");
        Vertex v19 = new ActivityVertex("v19_ORACLE", "v19_ORACLE", "18");
        Vertex v20 = new ActivityVertex("v20_ORACLE", "v20_ORACLE", "19");
        
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
        generateOracleValues(v11);
        generateOracleValues(v12);
        generateOracleValues(v13);
        generateOracleValues(v14);
        generateOracleValues(v15);
        generateOracleValues(v16);
        generateOracleValues(v17);
        generateOracleValues(v18);
        generateOracleValues(v19);
        generateOracleValues(v20);
        
        Edge e1 = new Edge("E1", "", "", "", v1, v2);
        Edge e2 = new Edge("E2", "", "", "", v2, v3);
        Edge e3 = new Edge("E3", "", "", "", v3, v4);
        Edge e4 = new Edge("E4", "", "", "", v4, v5);
        Edge e5 = new Edge("E5", "", "", "", v5, v6);
        Edge e6 = new Edge("E6", "", "", "", v6, v7);
        Edge e7 = new Edge("E7", "", "", "", v7, v8);
        Edge e8 = new Edge("E8", "", "", "", v8, v9);
        Edge e9 = new Edge("E9", "", "", "", v9, v10);
        Edge e10 = new Edge("E10", "", "", "", v10, v11);
        Edge e11 = new Edge("E11", "", "", "", v11, v12);
        Edge e12 = new Edge("E12", "", "", "", v12, v13);
        Edge e13 = new Edge("E13", "", "", "", v13, v14);
        Edge e14 = new Edge("E14", "", "", "", v14, v15);
        Edge e15 = new Edge("E15", "", "", "", v15, v16);
        Edge e16 = new Edge("E16", "", "", "", v16, v17);
        Edge e17 = new Edge("E17", "", "", "", v17, v18);
        Edge e18 = new Edge("E18", "", "", "", v18, v19);
        Edge e19 = new Edge("E19", "", "", "", v19, v20);
        
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
        templateGraph.addEdge(e10, e10.getSource(), e10.getTarget());
        templateGraph.addEdge(e11, e11.getSource(), e11.getTarget());
        templateGraph.addEdge(e12, e12.getSource(), e12.getTarget());
        templateGraph.addEdge(e13, e13.getSource(), e13.getTarget());
        templateGraph.addEdge(e14, e14.getSource(), e14.getTarget());
        templateGraph.addEdge(e15, e15.getSource(), e15.getTarget());
        templateGraph.addEdge(e16, e16.getSource(), e16.getTarget());
        templateGraph.addEdge(e17, e17.getSource(), e17.getTarget());
        templateGraph.addEdge(e18, e18.getSource(), e18.getTarget());
        templateGraph.addEdge(e19, e19.getSource(), e19.getTarget());
       
        oracleGraphNumber++;
        return templateGraph;
    }
    
    public DirectedGraph<Object, Edge> generateDagGraph() {
        Vertex v1 = new ActivityVertex("v1_ORACLE", "v1_ORACLE", "0");
        Vertex v2 = new ActivityVertex("v2_ORACLE", "v2_ORACLE", "1");
        Vertex v3 = new ActivityVertex("v3_ORACLE", "v3_ORACLE", "2");
        Vertex v4 = new ActivityVertex("v4_ORACLE", "v4_ORACLE", "3");
        Vertex v5 = new ActivityVertex("v5_ORACLE", "v5_ORACLE", "4");
//        Vertex v6 = new ActivityVertex("v6_ORACLE", "v6_ORACLE", "5");
        
        generateOracleValues(v1);
        generateOracleValues(v2);
        generateOracleValues(v3);
        generateOracleValues(v4);
        generateOracleValues(v5);
        
//        Edge e6 = new Edge("E5", "", "", "", v6, v1);
        Edge e1 = new Edge("E1", "", "", "", v1, v2);
        Edge e2 = new Edge("E2", "", "", "", v1, v3);
        Edge e3 = new Edge("E3", "", "", "", v2, v4);
        Edge e4 = new Edge("E4", "", "", "", v3, v4);
        Edge e5 = new Edge("E5", "", "", "", v4, v5);
        
        
        DirectedGraph<Object, Edge> templateGraph = new DirectedSparseMultigraph<>();
        templateGraph.addEdge(e1, e1.getSource(), e1.getTarget());
        templateGraph.addEdge(e2, e2.getSource(), e2.getTarget());
        templateGraph.addEdge(e3, e3.getSource(), e3.getTarget());
        templateGraph.addEdge(e4, e4.getSource(), e4.getTarget());
        templateGraph.addEdge(e5, e5.getSource(), e5.getTarget());
//        templateGraph.addEdge(e6, e6.getSource(), e6.getTarget());
        
        oracleGraphNumber++;
        return templateGraph;
    }
    
    public DirectedGraph<Object, Edge> generateTreeGraph() {
         Vertex v1 = new ActivityVertex("v1_ORACLE", "v1_ORACLE", "0");
        Vertex v2 = new ActivityVertex("v2_ORACLE", "v2_ORACLE", "1");
        Vertex v3 = new ActivityVertex("v3_ORACLE", "v3_ORACLE", "2");
        Vertex v4 = new ActivityVertex("v4_ORACLE", "v4_ORACLE", "3");
        Vertex v5 = new ActivityVertex("v5_ORACLE", "v5_ORACLE", "4");
        Vertex v6 = new ActivityVertex("v6_ORACLE", "v6_ORACLE", "5");
        Vertex v7 = new ActivityVertex("v7_ORACLE", "v7_ORACLE", "6");
        Vertex v8 = new ActivityVertex("v8_ORACLE", "v8_ORACLE", "7");
        Vertex v9 = new ActivityVertex("v9_ORACLE", "v9_ORACLE", "8");
        
        generateOracleValues(v1);
        generateOracleValues(v2);
        generateOracleValues(v3);
        generateOracleValues(v4);
        generateOracleValues(v5);
        generateOracleValues(v6);
        generateOracleValues(v7);
        generateOracleValues(v8);
        generateOracleValues(v9);
        
        Edge e1 = new Edge("E1", "", "", "", v1, v2);
        Edge e2 = new Edge("E2", "", "", "", v1, v3);
        Edge e3 = new Edge("E3", "", "", "", v2, v4);
        Edge e4 = new Edge("E4", "", "", "", v2, v5);
        Edge e5 = new Edge("E5", "", "", "", v5, v6);
        Edge e6 = new Edge("E6", "", "", "", v5, v7);
        Edge e7 = new Edge("E7", "", "", "", v3, v8);
        Edge e8 = new Edge("E8", "", "", "", v8, v9);
        
        DirectedGraph<Object, Edge> templateGraph = new DirectedSparseMultigraph<>();
        templateGraph.addEdge(e1, e1.getSource(), e1.getTarget());
        templateGraph.addEdge(e2, e2.getSource(), e2.getTarget());
        templateGraph.addEdge(e3, e3.getSource(), e3.getTarget());
        templateGraph.addEdge(e4, e4.getSource(), e4.getTarget());
        templateGraph.addEdge(e5, e5.getSource(), e5.getTarget());
        templateGraph.addEdge(e6, e6.getSource(), e6.getTarget());
        templateGraph.addEdge(e7, e7.getSource(), e7.getTarget());
        templateGraph.addEdge(e8, e8.getSource(), e8.getTarget());
        
        oracleGraphNumber++;
        return templateGraph;
    }
    
    public DirectedGraph<Object, Edge> generateTreeGraphBig() {
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
        Vertex v11 = new ActivityVertex("v11_ORACLE", "v11_ORACLE", "10");
        Vertex v12 = new ActivityVertex("v12_ORACLE", "v12_ORACLE", "11");
        Vertex v13 = new ActivityVertex("v13_ORACLE", "v13_ORACLE", "12");
        Vertex v14 = new ActivityVertex("v14_ORACLE", "v14_ORACLE", "13");
        Vertex v15 = new ActivityVertex("v15_ORACLE", "v15_ORACLE", "14");
        Vertex v16 = new ActivityVertex("v16_ORACLE", "v16_ORACLE", "15");
        Vertex v17 = new ActivityVertex("v17_ORACLE", "v17_ORACLE", "16");
        Vertex v18 = new ActivityVertex("v18_ORACLE", "v18_ORACLE", "17");
        Vertex v19 = new ActivityVertex("v19_ORACLE", "v19_ORACLE", "18");
        
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
        generateOracleValues(v11);
        generateOracleValues(v12);
        generateOracleValues(v13);
        generateOracleValues(v14);
        generateOracleValues(v15);
        generateOracleValues(v16);
        generateOracleValues(v17);
        generateOracleValues(v18);
        generateOracleValues(v19);
        
        Edge e1 = new Edge("E1", "", "", "", v1, v2);
        Edge e2 = new Edge("E2", "", "", "", v1, v3);
        Edge e3 = new Edge("E3", "", "", "", v2, v4);
        Edge e4 = new Edge("E4", "", "", "", v2, v5);
        Edge e5 = new Edge("E5", "", "", "", v5, v6);
        Edge e6 = new Edge("E6", "", "", "", v5, v7);
        Edge e7 = new Edge("E7", "", "", "", v3, v8);
        Edge e8 = new Edge("E8", "", "", "", v8, v9);
        
        DirectedGraph<Object, Edge> templateGraph = new DirectedSparseMultigraph<>();
        templateGraph.addEdge(e1, e1.getSource(), e1.getTarget());
        templateGraph.addEdge(e2, e2.getSource(), e2.getTarget());
        templateGraph.addEdge(e3, e3.getSource(), e3.getTarget());
        templateGraph.addEdge(e4, e4.getSource(), e4.getTarget());
        templateGraph.addEdge(e5, e5.getSource(), e5.getTarget());
        templateGraph.addEdge(e6, e6.getSource(), e6.getTarget());
        templateGraph.addEdge(e7, e7.getSource(), e7.getTarget());
        templateGraph.addEdge(e8, e8.getSource(), e8.getTarget());
        
        oracleGraphNumber++;
        return templateGraph;
    }
    
    public DirectedGraph<Object, Edge> createOracleGraph(String typeGraph) {
        if(typeGraph.equalsIgnoreCase("DAG"))
            return generateDagGraph();
        else if(typeGraph.equalsIgnoreCase("TREE"))
            return generateTreeGraph();
        else if(typeGraph.equalsIgnoreCase("LinearBig"))
            return generateLinearGraphBig();
        else
            return generateLinearGraph();
    }
}
