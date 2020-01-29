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

import br.uff.ic.utility.Dbscan;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kohwalter
 */
public class DbscanThread implements Runnable{
    
    ArrayList<ConcurrentHashMap<String, Object>> clusters;
    double epsilon;
    OracleGraph oracleGraph;
    DirectedGraph<Object, Edge> noiseGraph;
    ArrayList<Double> t;
            
            
    DbscanThread(ArrayList<ConcurrentHashMap<String, Object>> answer, OracleGraph og, DirectedGraph<Object, Edge> noiseGraph, double eps, ArrayList<Double> t) {
        this.clusters = answer;
        this.oracleGraph = og;
        this.noiseGraph = noiseGraph;
        this.epsilon = eps;
        this.t = t;
    }
    
    /**
     * Method to run an algorithm similar to the DBSCAN
     * @param noiseGraph is the graph used for finding the clusters
     * @param epsMod is the dbscan's epsilon
     * @return the time it took to execute this algorithm
     * @throws IOException 
     */
    public long dbscan(DirectedGraph<Object, Edge> noiseGraph, double epsMod) throws IOException {
        
        double eps = epsMod; 
        eps = Utils.std(noiseGraph.getVertices(), oracleGraph.attribute) * epsMod;
        Dbscan instance = new Dbscan(noiseGraph, oracleGraph.attribute, eps, 1);
        long startTime = System.nanoTime();
        clusters.addAll(instance.applyDbscan());
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.

        return duration;
    }

    @Override
    public void run() {
        try {
            double time = dbscan(noiseGraph, epsilon);
            t.add(time);
        } catch (IOException ex) {
            Logger.getLogger(DbscanThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

