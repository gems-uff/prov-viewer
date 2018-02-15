/*
 * The MIT License
 *
 * Copyright 2018 Kohwalter.
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
package br.uff.ic.provviewer;

import br.uff.ic.provviewer.Vertex.ColorScheme.DebugVisualizationScheme;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class ProvDebugger {

    /**
     *
     *
     * @param v is the current vertex
     * @param activity is the configuration that allows checking activity vertices
     * @param agent is the configuration that allows checking agent vertices
     * @param entity is the configuration that allows checking entity vertices
     * @return true if the current vertex fits the used configuration for debugging
     */
    private static boolean debugOptionModes(Object v, boolean activity, boolean agent, boolean entity) {
        if ((v instanceof EntityVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)) && entity) {
            return true;
        } else if ((v instanceof ActivityVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) && activity) {
            return true;
        } else if ((v instanceof AgentVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexAgentAttribute)) && agent) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Debug function that returns the causes of a failure and proposes a solution
     * @param variables
     * @param trial is the trial we want to analize
     * @param correctTrials is the list of known trials that worked
     * @param activity
     * @param agent
     * @param entity
     */
    public static void debugTrial(Variables variables, String trial, List<String> correctTrials, boolean activity, boolean agent, boolean entity) {
        String trial_test = "workflow_trial_6.xml";
        List<String> correctTrials_test = new ArrayList<>();
        correctTrials_test.add("workflow_trial_7.xml");
        correctTrials_test.add("workflow_trial_11.xml");
        correctTrials_test.add("workflow_trial_16.xml");
        correctTrials_test.add("workflow_trial_17.xml");
        correctTrials_test.add("workflow_trial_18.xml");
        correctTrials_test.add("workflow_trial_22.xml");
        correctTrials_test.add("workflow_trial_24.xml");
        correctTrials_test.add("workflow_trial_25.xml");
        correctTrials_test.add("workflow_trial_28.xml");
        correctTrials_test.add("workflow_trial_32.xml");
        Map<String, List<Vertex>> reasons = DebugTrials(trial_test, correctTrials_test, variables.graph, activity, agent, entity);
        List<Vertex> alwaysWrong = DebugTrialsAlwaysWrong(correctTrials_test, variables, activity, agent, entity);
        DebugVisualizationScheme graphMode = new DebugVisualizationScheme("Debug_" + trial_test, alwaysWrong, reasons);
        variables.config.vertexModes.put("Debug_" + trial_test, graphMode);
        variables.config.InterfaceStatusFilters();
    }
    public static void debugTrial_original(Variables variables, String trial, List<String> correctTrials, boolean activity, boolean agent, boolean entity) {
        Map<String, List<Vertex>> reasons = DebugTrials(trial, correctTrials, variables.graph, activity, agent, entity);
        List<Vertex> alwaysWrong = DebugTrialsAlwaysWrong(correctTrials, variables, activity, agent, entity);
        DebugVisualizationScheme graphMode = new DebugVisualizationScheme("Debug_" + trial, alwaysWrong, reasons);
        variables.config.vertexModes.put("Debug_" + trial, graphMode);
        variables.config.InterfaceStatusFilters();
    }

    /**
     * Method to find the causes that led the trial to fail
     *
     * @param trial is the trial we are debugging
     * @param correctTrials is the list of trials that worked
     * @param variables contains the graph
     * @return two lists inside a map: "correct" showing an example to make the trial work; "error" showing the vertices that caused the failure
     */
    public static Map<String, List<Vertex>> DebugTrials(String trial, List<String> correctTrials, Graph graph, boolean activity, boolean agent, boolean entity) {
        Graph g = graph;
        Map<String, List<Vertex>> diffs = new HashMap<>();
        // Runs the graph multiple times, once for each "OK" graph
        //        for(String t : correctTrials) {
        //            List<Vertex> diffVertices = new ArrayList<>();
        //            for (Object v : g.getVertices()) {
        //                if(v instanceof EntityVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)) {
        //                    if (((Vertex)v).getAttributeValue(VariableNames.GraphFile).contains(t)) {
        //                        if (!((Vertex)v).getAttributeValue(VariableNames.GraphFile).contains(trial)) {
        //                            diffVertices.add((Vertex) v);
        //                        }
        //                    }
        //                }
        //            }
        //            diffs.put(t, diffVertices);
        //        }
        // Run through the graph only once, building the DIFFS to all "OK" graphs
        for (Object v : g.getVertices()) {
            if (debugOptionModes(v, activity, agent, entity)) {
                for (String t : correctTrials) {
                    if (((Vertex) v).getAttributeValue(VariableNames.GraphFile).contains(t)) {
                        if (!((Vertex) v).getAttributeValue(VariableNames.GraphFile).contains(trial)) {
                            List<Vertex> diffVertices = new ArrayList<>();
                            if (diffs.containsKey(t)) {
                                diffVertices = diffs.get(t);
                            }
                            diffVertices.add((Vertex) v);
                            diffs.put(t, diffVertices);
                        }
                    }
                }
            }
        }
        int min = Integer.MAX_VALUE;
        List<Vertex> minDiff = new ArrayList<>();
        List<Vertex> failureVertices = new ArrayList<>();
        List<Vertex> originalTrialVertices = new ArrayList<>();
        String minDiffTrial = "";
        for (String key : diffs.keySet()) {
            int diffsize = diffs.get(key).size();
            if (min > diffsize) {
                min = diffsize;
                minDiff = diffs.get(key);
                minDiffTrial = key;
            }
        }
        //        for(List<Vertex> diff : diffs.values()) {
        //            if(min > diff.size()) {
        //                min = diff.size();
        //                minDiff = diff;
        //            }
        //        }
        for (Object v : g.getVertices()) {
            if (debugOptionModes(v, activity, agent, entity)) {
                if (((Vertex) v).getAttributeValue(VariableNames.GraphFile).contains(trial)) {
                    originalTrialVertices.add((Vertex) v);
                    if (!((Vertex) v).getAttributeValue(VariableNames.GraphFile).contains(minDiffTrial)) {
                        failureVertices.add((Vertex) v);
                    }
                }
            }
        }
        String ids = "";
        String failureIds = "";
        for (Vertex v : minDiff) {
            ids += " " + v.getLabel();
        }
        for (Vertex v : failureVertices) {
            failureIds += " " + v.getID();
        }
        System.out.println("Min diff is: " + min);
        System.out.println("Error Reasons: " + ids);
        System.out.println("Vertices that led to failure: " + failureIds);
        Map<String, List<Vertex>> debugResult = new HashMap<>();
        debugResult.put("correct", minDiff);
        debugResult.put("error", failureVertices);
        debugResult.put("originalVertices", originalTrialVertices);
        return debugResult;
        // Need to mark with ORANGE border the cases that lead this trial to failure
    }

    /**
     * Method to find out the configurations that always led to an error
     *
     * @param correctTrials is the list of the correct trials
     * @param variables has the graph
     */
    public static List<Vertex> DebugTrialsAlwaysWrong(List<String> correctTrials, Variables variables, boolean activity, boolean agent, boolean entity) {
        Graph g = variables.graph;
        List<Vertex> alwaysWrong = new ArrayList<>();
        for (Object v : g.getVertices()) {
            if (debugOptionModes(v, activity, agent, entity)) {
                boolean isCorrect = false;
                for (String t : correctTrials) {
                    if (((Vertex) v).getAttributeValue(VariableNames.GraphFile).contains(t)) {
                        isCorrect = true;
                    }
                }
                if (!isCorrect) {
                    alwaysWrong.add((Vertex) v);
                }
            }
        }
        for (Vertex v : alwaysWrong) {
            System.out.println("Always leads to error: " + v.getLabel() + ": " + v.getAttributeValue("value"));
            float support = v.getFrequencyValue(variables.numberOfGraphs);
            float confidence = 1.0F;
            float lift = confidence / (1 - support);
            System.out.println("support: " + support * 100 + " %");
            System.out.println("confidence: " + confidence * 100 + " %");
            System.out.println("lift: " + lift);
        }
        return alwaysWrong;
        // Need to mark with RED border the cases that always lead to failure
    }
    
}
