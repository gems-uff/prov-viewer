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
import br.uff.ic.utility.Dbscan;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kohwalter
 */
public class DbscanThread implements Runnable{
    
    StringBuffer clusters;
    double epsilon;
    OracleGraph oracleGraph;
    DirectedGraph<Object, Edge> noiseGraph;
    ArrayList<Double> t;
            
            
    DbscanThread(StringBuffer answer, OracleGraph og, DirectedGraph<Object, Edge> noiseGraph, double eps, ArrayList<Double> t) {
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
        Dbscan instance = new Dbscan(noiseGraph, oracleGraph.attribute, eps, 1);
        long startTime = System.nanoTime();
        clusters.append(instance.applyDbscan());
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

