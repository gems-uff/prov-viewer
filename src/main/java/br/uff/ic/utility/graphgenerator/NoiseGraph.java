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
import java.util.Collection;
import java.util.Random;

/**
 *
 * @author Kohwalter
 */
public class NoiseGraph {
    
    String EDGE_DEFAULT_TYPE = "Chronological";
    int id_counter;
    String attribute;
    DirectedGraph<Object, Edge> oracleGraph;
    DirectedGraph<Object, Edge> noiseGraph;
    private boolean isMonotonic = true;

    /**
     * Constructor
     * @param oracleGraph is the template graph
     * @param attribute is the name of the attribute used by the vertices
     */
    NoiseGraph(DirectedGraph<Object, Edge> oracleGraph, String attribute) {
        this.attribute = attribute;
        id_counter = 1;
        this.oracleGraph = oracleGraph;
        this.noiseGraph = Utils.copyGraph(this.oracleGraph);
    }

    NoiseGraph(DirectedGraph<Object, Edge> oracleGraph, String attribute, boolean b) {
        this.attribute = attribute;
        id_counter = 1;
        this.oracleGraph = oracleGraph;
        this.noiseGraph = Utils.copyGraph(this.oracleGraph);
        isMonotonic = b;
    }
    
    public void setMonotonic(boolean t) {
        isMonotonic = t;
    }
    
    /**
     * Method to determine the noise value thresholds
     * @param vertex is the template vertex that will be used to generate a noise vertex
     * @return the noise threshold
     */
    private float noiseThreshold(Vertex vertex) {
        Collection<Object> neighbors = oracleGraph.getNeighbors(vertex);
        float minDelta = Float.POSITIVE_INFINITY;
        for (Object v : neighbors) {
            float n_value;
            float v_value;
            if (!((Vertex) v).getAttributeValue(attribute).contentEquals("Unknown")) {
                n_value = ((Vertex)v).getAttributeValueFloat(attribute);
                if (!vertex.getAttributeValue(attribute).contentEquals("Unknown")) {
                    v_value = vertex.getAttributeValueFloat(attribute);
                    minDelta = Math.min(minDelta, Math.abs(n_value - v_value));
                }
            }
        }
        
        
        return threeSigma(minDelta);
    }
    
    /**
     * Method to return the noise value threshold, which is three sigma
     * @param minDelta is the minimum distance between the template vertex and it's neighbors
     * @return the three sigma
     */
    private float threeSigma(float minDelta) {
        return minDelta;
    }
    
    
    /**
     * Method to generate the noise vertex value
     * @param mean is the template vertex's value
     * @param threeSigma is the threshold for the noise value
     * @return the value for the noise vertex
     */
    private float randomNoiseValue (float mean, float threeSigma)  {
        float value = 0;
        float sigma = (float) (threeSigma * 0.333);
        Random rng = new Random();
        
        value = (float) (mean + sigma * rng.nextGaussian());
        if(value != value) {
            System.out.println("randomNoiseValue NaN!");
            System.out.println("Mean: " + mean);
            System.out.println("Sigma: " + sigma);
        }
        return value;
    }
    
    private Vertex createMonotonicNoise (Edge edge)  {
        float value;
        
        float source = ((Vertex)edge.getSource()).getAttributeValueFloat(attribute);
        float target = ((Vertex)edge.getTarget()).getAttributeValueFloat(attribute);
        value = (float) (Math.min(source, target) + (Math.random() * ((Math.max(source, target) - Math.min(source, target)) + 1)));
//        if(value != value) {
//            System.out.println("createMonotonicNoise NaN!");
//            System.out.println("createMonotonicNoise source: " + source);
//            System.out.println("createMonotonicNoise target: " + target);
//            System.out.println("createMonotonicNoise Edge source: " + ((Vertex)edge.getSource()).getID() + " (" + attribute + ") " + ((Vertex)edge.getSource()).getAttributeValue(attribute));
//            System.out.println("createMonotonicNoise Edge target: " + ((Vertex)edge.getTarget()).getID() + " (" + attribute + ") " + ((Vertex)edge.getTarget()).getAttributeValue(attribute));
//        }
        return newNoiseVertex(value, ((Vertex)edge.getSource()).getTimeString());
    }
    
    /**
     * Method to create a new noise vertex
     * @param noiseValue is the value for the noise vertex
     * @return the noise vertex
     */
    private Vertex newNoiseVertex(float noiseValue, String date) {
        String id;
        id = id_counter + "N";
        id_counter++;
        
        Vertex noise = new ActivityVertex(id, id, date);
        GraphAttribute att = new GraphAttribute(attribute, String.valueOf(noiseValue));
//        if(noiseValue != noiseValue)
//            System.out.println("noiseValue NaN!");
        noise.addAttribute(att);
        
        return noise;
    }
    
    /**
     * Method to return the mean of the normal distribution (AKA template vertex value)
     * @param vertex is the template vertex
     * @return the mean
     */
    private float getMean(Object vertex) {
        return((Vertex) vertex).getAttributeValueFloat(attribute);
    }
    
    /**
     * Method to insert the noise vertex in the graph
     * @param vertex is the template vertex that generated the noise
     * @param noise is the noise vertex
     */
    private void insertNoise(Vertex vertex, Vertex noise) {
        Collection<Edge> neighborEdges = noiseGraph.getIncidentEdges(vertex);
        int random = pickRandomly(neighborEdges.size());
        Edge edge = (Edge) neighborEdges.toArray()[random];
        if(isMonotonic) {
            noise = createMonotonicNoise(edge);
        }
        String edgeID = "Noise_Edge_" + id_counter; 
        Edge noiseEdge_1 = new Edge(edgeID + "_1", edge.getType(), edge.getLabel(), "", edge.getTarget(), noise);
        Edge noiseEdge_2 = new Edge(edgeID + "_2", edge.getType(), edge.getLabel(), "", noise, edge.getSource());
        
        noiseGraph.addEdge(noiseEdge_1, noiseEdge_1.getSource(), noiseEdge_1.getTarget());
        noiseGraph.addEdge(noiseEdge_2, noiseEdge_2.getSource(), noiseEdge_2.getTarget());
        noiseGraph.removeEdge(edge);
    }
    
    /**
     * Method to pick randomly a value between 0 and size
     * @param size is the maximum value
     * @return the picked value
     */
    private int pickRandomly(int size) {
        int min = 0;
        int max = size - 1;
        return min + (int)(Math.random() * ((max - min) + 1));
    }
    
    /**
     * Method to create and insert the noise vertex in the noiseGraph
     */
    
    private void addNoise (Object[] oracleVertices) {
        int random = pickRandomly(oracleVertices.length);
        Object vertex = oracleVertices[random];
        float threeSigma = noiseThreshold((Vertex) vertex);
        float mean = getMean(vertex);
        float noiseValue = randomNoiseValue(mean, threeSigma);
        
        Vertex noise = newNoiseVertex(noiseValue, ((Vertex)vertex).getTimeString());
        
        insertNoise((Vertex) vertex, noise);
    }
    
    /**
     *Method to determine if it will generate a new noise vertex
     * @param noiseProbability is the probability of generating a new noise, ranging from 0 to 1
     * @return true or false
     */
    private boolean generateNewNoise(float noiseProbability) {
        if(Math.random() < noiseProbability)
            return true;
        else
            return false;
    }
    
    /**
     * Method to generate the noiseGraph based on the templateGraph
     * @param noiseFactor is the the maximum number of inserted noises, ranging from 0 to 1. This factor is multiplied by the templateGraph's size
     * @param noiseProbability is the probability to create a new noise
     * @return the noiseGraph, which is a templateGraph with noise
     */
    public DirectedGraph<Object, Edge> generateNoiseGraph(float noiseFactor, float noiseProbability, String noiseGraphName) {
        if(noiseFactor < 1) {
            noiseFactor = 1;
        }
        int noiseQuantity = (int) (oracleGraph.getVertices().size() * noiseFactor);
        Object[] oracleVertices = oracleGraph.getVertices().toArray();
        
        for( int i = 0; i < noiseQuantity; i++) {
            if(generateNewNoise(noiseProbability)) {
                addNoise(oracleVertices);
            }
        }
        
//        Utils.exportGraph(noiseGraph, "noise_graph" + "_" + noiseGraphName);
        return noiseGraph;
    }
}
