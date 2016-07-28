/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.IO.XMLWriter;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kohwalter
 */
public class NoiseGraph {
    
    String EDGE_DEFAULT_TYPE = "Neutral";
    int id_counter;
    String attribute;
    DirectedGraph<Object, Edge> templateGraph;
    DirectedGraph<Object, Edge> noiseGraph;

    /**
     * Constructor
     * @param templateGraph is the template graph
     * @param attribute is the name of the attribute used by the vertices
     */
    NoiseGraph(DirectedGraph<Object, Edge> templateGraph, String attribute) {
        this.attribute = attribute;
        id_counter = 1;
        this.templateGraph = templateGraph;
        this.noiseGraph = templateGraph;
    }
    
    /**
     * Method to determine the noise value thresholds
     * @param vertex is the template vertex that will be used to generate a noise vertex
     * @return the noise threshold
     */
    private float noiseThreshold(Vertex vertex) {
        Collection<Object> neighbors = templateGraph.getNeighbors(vertex);
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
    private double randomNoiseValue (float mean, float threeSigma)  {
        double value;
        double sigma = threeSigma / 3;
        Random rng = new Random();
        
        value = mean + sigma * rng.nextGaussian();
        
//        while((value < mean - threeSigma) || (value > mean + threeSigma)) {
//            value = mean + sigma * rng.nextGaussian();
//        }
        
        return value;
    }
    
    /**
     * Method to create a new noise vertex
     * @param noiseValue is the value for the noise vertex
     * @return the noise vertex
     */
    private Vertex newNoiseVertex(double noiseValue, String date) {
        String id;
        id = "noise_" + id_counter;
        id_counter++;
        
        Vertex noise = new ActivityVertex(id, id, date);
        GraphAttribute att = new GraphAttribute(attribute, Double.toString(noiseValue));
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
    private void addNoise (Object[] templateVertices) {
        int random = pickRandomly(templateVertices.length);
        Object vertex = templateVertices[random];
        double threeSigma = noiseThreshold((Vertex) vertex);
        float mean = getMean(vertex);
        double noiseValue = randomNoiseValue(mean, (float) threeSigma);
        
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
    public DirectedGraph<Object, Edge> generateNoiseGraph(float noiseFactor, float noiseProbability) {
        int noiseQuantity = (int) (templateGraph.getVertices().size() * noiseFactor);
        Object[] templateVertices = templateGraph.getVertices().toArray();
        
        for( int i = 0; i < noiseQuantity; i++) {
            if(generateNewNoise(noiseProbability)) {
                addNoise(templateVertices);
            }
        }
        
        Utils.exportGraph(noiseGraph, "noise_graph");
        
        return noiseGraph;
    }
}
