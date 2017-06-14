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
package br.uff.ic.graphmatching;

import br.uff.ic.graphmatching.Heuristics.SimpleHeuristic;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.Vocabulary;
import br.uff.ic.utility.graph.Edge;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class GraphMerger {
    
//    private String graph01;
//    private String graph02;
    
    
    public GraphMerger(String g01, String g02) {
//        graph01 = "(" + g01 + ")";
//        graph02 = "(" + g02 + ")";
    }
    public GraphMerger() {
//        graph01 = "Graph_01";
//        graph02 = "Graph_02";
    }
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param vocabulary
     * @param similarityThreshold
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList,
            Vocabulary vocabulary,
            float similarityThreshold) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, vocabulary.getVocabulary(), similarityThreshold);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param vocabulary
     * @param similarityThreshold
     * @param defaultError
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList,
            Vocabulary vocabulary,
            float similarityThreshold, String defaultError) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, vocabulary.getVocabulary(), similarityThreshold, defaultError);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param vocabulary
     * @param similarityThreshold
     * @param defaultError
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList,
             Map<String, String> vocabulary,
            float similarityThreshold, String defaultError) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, vocabulary, similarityThreshold, defaultError);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param vocabulary
     * @param similarityThreshold
     * @param defaultError
     * @param defaultWeight
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList,
             Map<String, String> vocabulary,
            float similarityThreshold, String defaultError, float defaultWeight) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, vocabulary, similarityThreshold, defaultError, defaultWeight);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param similarityThreshold
     * @param defaultError
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList, 
            float similarityThreshold, String defaultError) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, similarityThreshold, defaultError);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param similarityThreshold
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList, 
            float similarityThreshold) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, similarityThreshold);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param combiner
     * @return 
     */
    private DirectedGraph<Object, Edge> MergeGraphs(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            GraphMatching combiner) {
        // Correct IDs
//        Utils.updateVertexIDs(graph_01, graph01);
//        Utils.updateVertexIDs(graph_02, graph02);
//        Utils.updateEdgeIDs(graph_01, graph01);
//        Utils.updateEdgeIDs(graph_02, graph02);
        Utils.NormalizeTime(graph_01, true);
        Utils.NormalizeTime(graph_02, true);
        
        // Matching Heuristic
        MatchingHeuristic heuristic = new SimpleHeuristic();
        heuristic.MatchGraphs(graph_01, graph_02, combiner);
        
        return combiner.CG();
        // After matching all vertices
//        combiner.addVertices(graph_01.getVertices());
//        combiner.addVertices(graph_02.getVertices());
//        Collection<Edge> updatedG1edges = combiner.updateEdges(graph_01.getEdges());
//        Collection<Edge> updatedG2edges = combiner.updateEdges(graph_02.getEdges());
//        combiner.addEdges(updatedG1edges);
//        combiner.addEdges(updatedG2edges);
//        return combiner.getCombinedGraph();
    }
}
