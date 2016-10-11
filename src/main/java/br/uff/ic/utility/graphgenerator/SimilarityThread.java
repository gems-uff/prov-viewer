/*
 * Copyright (C) 2016 Kohwalter
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package br.uff.ic.utility.graphgenerator;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.provviewer.Inference.AutomaticInference;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kohwalter
 */
public class SimilarityThread implements Runnable{
    OracleGraph oracleGraph;
    DirectedGraph<Object, Edge> oracle; 
    DirectedGraph<Object, Edge> noiseGraph; 
    boolean updateError; 
    boolean verifyWithinCluster;
    ArrayList<Float> t;
    int size; 
    int thresholdIncrease; 
    int qnt;
    ArrayList<ConcurrentHashMap<String, Object>> clusters;
            
            
    SimilarityThread(ArrayList<ConcurrentHashMap<String, Object>> answer, OracleGraph og, DirectedGraph<Object, Edge> noiseGraph, boolean updateError, 
            boolean verifyWithinCluster,
            ArrayList<Float> t,
            int minSize, int thresholdIncrease, int qnt) {
        oracleGraph = og;
        this.noiseGraph = noiseGraph;
        this.updateError = updateError;
        this.verifyWithinCluster = verifyWithinCluster;
        this.t = t;
        this.size = minSize;
        this.thresholdIncrease = thresholdIncrease;
        this.qnt = qnt;
        this.clusters = answer;
    }
    
    /**
     * Method to run the Similairty algorithm to find the clusters
     * @param noiseGraph is the graph that we want to run the algorithm
     * @param updateError defines if this algorithm will use a global epsilon or if each cluster will have its own epsilon
     * @param verifyWithinCluster defines if the algorithm will compare with the actual neighbor or with everyone already in the cluster in order to decide if the new element will join
     * @param minSize is a configuration value for the similarity algorithm
     * @param thresholdIncrease is a configuration value for the similarity algorithm
     * @param qnt is a configuration value for the similarity algorithm
     * @return the duration it took to find the clusters
     * @throws IOException 
     */
    public long SimilarityCollapse(DirectedGraph<Object, Edge> noiseGraph, boolean updateError, 
            boolean verifyWithinCluster,
            int minSize, int thresholdIncrease, int qnt) throws IOException {
        
        GraphMatching combiner = configureSimilarityMatcher(noiseGraph);
        AutomaticInference infer = new AutomaticInference(combiner, noiseGraph, minSize, thresholdIncrease, qnt);
        
        // Measure time
        long startTime = System.nanoTime();
        clusters.addAll(infer.applySimilarity(updateError, verifyWithinCluster));
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        
        // Compare results with the oracle
        return duration;
    }
    
    /**
     * Method to configure the similarity cluster for the experiment
     * @param noiseGraph is the graph that will be used in the similarity algorithm
     * @return 
     */
    private GraphMatching configureSimilarityMatcher(DirectedGraph<Object, Edge> noiseGraph) {
        float std = Utils.std(noiseGraph.getVertices(), oracleGraph.attribute);
        float similarityThreshold = 0.5f;
        String defaultError = "0";
        Map<String, AttributeErrorMargin> restrictionList = new HashMap<>();
        AttributeErrorMargin epsilon;
        epsilon = new AttributeErrorMargin(oracleGraph.attribute, String.valueOf(std));
        restrictionList.put(oracleGraph.attribute, epsilon);
        return new GraphMatching(restrictionList, similarityThreshold, defaultError, 0);
    }

    @Override
    public void run() {
        try {
            float time = SimilarityCollapse(noiseGraph, true, false, size, thresholdIncrease, qnt);
            t.add(time);
        } catch (IOException ex) {
            Logger.getLogger(SimilarityThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

