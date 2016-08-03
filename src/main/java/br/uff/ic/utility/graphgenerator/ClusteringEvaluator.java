/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

import static br.uff.ic.provviewer.GUI.GuiInference.ColorSchemeCollapse;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Kohwalter
 */
public class ClusteringEvaluator {

    int NUMBER_OF_ORACLE_GRAPHS = 10;
    int NUMBER_OF_NOISE_GRAPHS = 10;
    double averageFmeasure = 0;
    float noiseProbability = 1.0F;
    double averageRecall = 0;
    float noiseFactor = 3.0F;
    double averagePrecision = 0;

    public void comparePRF(DirectedGraph<Object, Edge> oracle, String list, OracleGraph oracleGraph) {
        List<String> clusters = new ArrayList<>();
        double relevantDocuments = oracle.getVertexCount();
        double retrievedDocuments;
        double intersection = 0;
        double precision;
        double recall;
        double fmeasure;
        String[] elements = list.split(" ");
        clusters.addAll(Arrays.asList(elements));
        retrievedDocuments = clusters.size();
        for (String cluster : clusters) {
            boolean computedCluster = false;
            System.out.println("Cluster: " + cluster);
            for (Object v : oracle.getVertices()) {
                String id = ((Vertex) v).getID();
                if (cluster.contains(id) && !computedCluster) {
                    computedCluster = true;
                    intersection++;
                }
            }
        }
        precision = intersection / retrievedDocuments;
        recall = intersection / relevantDocuments;
        fmeasure = 2 * (precision * recall) / (precision + recall);
        averagePrecision += precision;
        averageRecall += recall;
        averageFmeasure += fmeasure;
        System.out.println("Intersection: " + intersection);
        System.out.println("Retrieved Documents: " + retrievedDocuments);
        System.out.println("Relevant Documents: " + relevantDocuments);
        System.out.println("");
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F-Measure: " + fmeasure);
    }

    public void collapse(OracleGraph oracleGraph) {
        int i = 0;
        int j = 0;
        for (j = 0; j < NUMBER_OF_ORACLE_GRAPHS; j++) {
            System.out.println("===============================");
            System.out.println("ORACLE NUMBER #" + j);
            DirectedGraph<Object, Edge> oracle = oracleGraph.generateLinearGraph();
            System.out.println("===============================");
            for (i = 0; i < NUMBER_OF_NOISE_GRAPHS; i++) {
                System.out.println("===============================");
                System.out.println("TEST NUMBER #" + i);
                NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute);
                DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability);
                String clusters = ColorSchemeCollapse(oracleGraph.attribute, noiseGraph);
                System.out.println("Finished Collapsing");
                System.out.println("Oracle size: " + oracle.getVertexCount());
                System.out.println("NoiseGraph size: " + noiseGraph.getVertexCount());
                comparePRF(oracle, clusters, oracleGraph);
            }
        }
        System.out.println("=========================");
        System.out.println("Average Precision: " + averagePrecision / (i * j));
        System.out.println("Average Recall: " + averageRecall / (i * j));
        System.out.println("Average F-Measure: " + averageFmeasure / (i * j));
    }
    
}
