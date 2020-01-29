/*
 * The MIT License
 *
 * Copyright 2020 Kohwalter.
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
package br.uff.ic.provviewer.markov;

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Kohwalter
 */
public class Markov {
    
//    public Map<String, Map<String, Double>> probability = new HashMap<>();
    public Map<String, Map<String, Double>> probabilityFirst = new HashMap<>();
//    public Map<String, Map<String, Double>> probability_independant = new HashMap<>();
    DecimalFormat df = new DecimalFormat("#.###");
    private int labelsCount = 0; 
    private double minProbability = 0.005;
//    Map<String, Boolean> visistedLabels = new HashMap<>();

    public void computeMarkovChain(Variables variables) {
        long start = System.currentTimeMillis();
        Collection<String> allLabels = Utils.DetectAllPossibleValuesFromAttribute(variables.graph.getVertices(), VariableNames.label);
        labelsCount = allLabels.size();
        for (Edge e : variables.graph.getEdges()) {
            calculateMarkovValues(e, variables.numberOfGraphs);
        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println("Elapsed Time computing Markov variables: " + elapsedTime);
        System.out.println("Markov calculation is done!");
    }

    public void calculateMarkovValues(Edge e, int ngraphs) {
        if (!(e.getTarget() instanceof AgentVertex)) {
            float eFreq = e.updateFrequency(ngraphs);
            Vertex vsource = (Vertex) e.getSource();
            float sourceFreq = vsource.updateFrequency(ngraphs);
            Vertex vstarget = (Vertex) e.getTarget();
            float targetFreq = vstarget.updateFrequency(ngraphs);
            e.addAttribute(new GraphAttribute(VariableNames.MarkovIn, String.valueOf(eFreq / targetFreq), "ProvViewer")); // Futuro
            e.addAttribute(new GraphAttribute(VariableNames.MarkovOut, String.valueOf(eFreq / sourceFreq), "ProvViewer")); // Passado
        }
    }
    
    public void computeMarkovInChainFromOrigin(Variables variables) {
        long start = System.currentTimeMillis();
        variables.layout.getGraph().getVertices().stream().map((v) -> {
            ((Vertex) v).attributes.remove(VariableNames.MarkovInLayout);
            return v;
        }).forEachOrdered((v) -> {
            ((Vertex) v).attributes.remove(VariableNames.MarkovOutLayout);
        });
//        for(Object v : variables.layout.getGraph().getVertices()) {
//            ((Vertex)v).attributes.remove(VariableNames.MarkovInLayout);
//            ((Vertex)v).attributes.remove(VariableNames.MarkovOutLayout);
//        }

        PickedState<Object> picked_state = variables.view.getPickedVertexState();
        this.initProbabilityMap();
        if (picked_state.getPicked().size() > 0) {
            Vertex source = (Vertex) picked_state.getPicked().toArray()[0];
            markovInChain(new ArrayList<String>(), source, 1, 0, 50, variables);
            markovOutChain(source, 1, 0, 50, variables);
            this.printAllProbabilities();
        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println("Elapsed Time for Markov Chain: " + elapsedTime);
    }

    public void markovInChain(ArrayList<String> parent, Vertex source, double markov, int currentDepth, int maxDepth, Variables variables) {      
        if (currentDepth < maxDepth && markov > this.minProbability) {
            List<Edge> i = new ArrayList(variables.layout.getGraph().getInEdges(source));
            for(Edge edge : i) {
                double markIn = Double.valueOf(edge.getAttributeValue(VariableNames.MarkovIn));
                addEvent(source, parent, markIn * markov);
                ArrayList<String> hasVisitedLabels = cloneArrayList(parent);
                Vertex newSource = (Vertex) edge.getSource();
                markovInChain(hasVisitedLabels, newSource, markIn * markov, currentDepth + 1, maxDepth, variables);
            }
        }
    }
    
    private void addEvent(Vertex source, ArrayList<String> parent, double markov) {
        // Lets compute the markov probability to reach this vertex
        if (source.hasAttribute(VariableNames.MarkovInLayout)) {
            if (Double.compare(Double.valueOf(source.getAttributeValue(VariableNames.MarkovInLayout)), markov) < 0) {
                source.addAttribute(new GraphAttribute(VariableNames.MarkovInLayout, String.valueOf(markov), "ProvViewer"));
            }
        } else {
            source.addAttribute(new GraphAttribute(VariableNames.MarkovInLayout, String.valueOf(markov), "ProvViewer"));
        }
        
        // Lets compute the probability of occuring at least one of each event type
        if(!parent.contains(source.getLabel())) {
            this.addProbability(this.probabilityFirst, source.getLabel(), source.getID(), markov);
            parent.add(source.getLabel());
        }
        else {
            if(this.probabilityFirst.get(source.getLabel()).containsKey(source.getID())) {
                this.addProbability(this.probabilityFirst, source.getLabel(), source.getID(), markov);
            }
        }
    }
    
    private ArrayList<String> cloneArrayList(ArrayList<String> oldArray) {
        ArrayList<String> copy = new ArrayList<String>();
        copy.addAll(oldArray);
        return copy;
    }
    
    public void markovOutChain(Vertex source, double markov, int currentDepth, int maxDepth, Variables variables) {
        if (source.hasAttribute(VariableNames.MarkovOutLayout)) {
            if (Float.valueOf(source.getAttributeValue(VariableNames.MarkovOutLayout)) < markov) {
                source.addAttribute(new GraphAttribute(VariableNames.MarkovOutLayout, String.valueOf(markov), "ProvViewer"));
            }
        } else {
            source.addAttribute(new GraphAttribute(VariableNames.MarkovOutLayout, String.valueOf(markov), "ProvViewer"));
        }
        if (currentDepth < maxDepth && markov > 0.005) {
            Iterator<Edge> i = variables.layout.getGraph().getOutEdges(source).iterator();
            while (i.hasNext()) {
                Edge edge = (Edge) i.next();
                if (!(edge.getTarget() instanceof AgentVertex)) {
                    Vertex newSource = (Vertex) edge.getTarget();
                    double markovOut = Double.valueOf(edge.getAttributeValue(VariableNames.MarkovOut));
                    markovOutChain(newSource, markovOut * markov, currentDepth + 1, maxDepth, variables);
                }
            }
        }
    }
    
    public void initProbabilityMap() {
        this.probabilityFirst = new HashMap<>();
    }
    
    public void addProbability(Map<String, Map<String, Double>> probMap, String label, String innerKey, double v){
        if(probMap.containsKey(label)) {
            Map<String, Double> innerMap = probMap.get(label);
            if(innerMap.containsKey(innerKey)) {
                if(Double.compare(innerMap.get(innerKey), v) < 0) {
                    innerMap.put(innerKey, v);
                }
            }
            else {
                innerMap.put(innerKey, v);
            }
        }
        else {
            Map<String, Double> innerMap = new HashMap<>();
            innerMap.put(innerKey, v);
            probMap.put(label, innerMap);
        }
    }
    
    public void printAllProbabilities() {
//        printAllDefaultProbabilities();
//        printAllSumProbabilities();
        printAllSumFirstProbabilities();
//        printAllIndependantProbabilities();
    }
//    public void printAllSumProbabilities() {
//        for(String label : probability.keySet()) {
////            System.out.println();
////            System.out.print(label + ": ");
//            Map<String, Double> innerMap = probability.get(label);
//            double prob = 0;
//            for(double v : innerMap.values()) {
//                prob += v;
////                System.out.print(df.format(v) + " + ");
//            }
////            System.out.println();
////            System.out.println("total = " + prob);
//            System.out.println("(Sum) Probability (" + label + ") = " + df.format(prob * 100) + "%");
//        }
//    }
    
//    public void printAllDefaultProbabilities() {
//        for(String label : probability.keySet()) {
////            System.out.println();
////            System.out.print(label + ": ");
//            Map<String, Double> innerMap = probability.get(label);
//            double prob = 1;
//            for(double v : innerMap.values()) {
//                prob *= (1 - v);
////                System.out.print(df.format(1 - v) + " * ");
//            }
////            System.out.println();
////            System.out.println("total = " + prob);
//            System.out.println("(Default) Probability (" + label + ") = " + df.format((1 - prob) * 100) + "%");
//        }
//    }
    
    public void printAllSumFirstProbabilities() {
        for(String label : probabilityFirst.keySet()) {
//            System.out.println();
//            System.out.print(label + ": ");
            Map<String, Double> innerMap = probabilityFirst.get(label);
            double prob = 0;
            for(double v : innerMap.values()) {
                prob += v;
//                System.out.print(df.format(v) + " + ");
            }
//            System.out.println();
//            System.out.println("total = " + prob);
            System.out.println("(StopFirst) Probability (" + label + ") = " + df.format(prob * 100) + "%");
        }
    }
//    
//    public void printAllIndependantProbabilities() {
//        for(String label : probability_independant.keySet()) {
////            System.out.println();
////            System.out.print(label + ": ");
//            Map<String, Double> innerMap = probability_independant.get(label);
//            double prob = 1;
//            for(double v : innerMap.values()) {
//                prob *= (1 - v);
////                System.out.print(df.format(1 - v) + " * ");
//            }
////            System.out.println();
////            System.out.println("total = " + (1 - prob));
//            System.out.println("(Independant) Probability (" + label + ") = " + df.format((1 - prob) * 100) + "%");
//        }
//    }
}
