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
package br.uff.ic.provviewer.GUI;

import br.uff.ic.graphmatching.GraphMerger;
import br.uff.ic.utility.graph.Edge;
import static br.uff.ic.provviewer.GUI.GuiFunctions.PanCameraToFirstVertex;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Input.SimilarityConfig;
import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.utility.IO.UnityReader;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.IO.InputReader;
import br.uff.ic.utility.IO.PROVNReader;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.apache.commons.io.FilenameUtils;

/**
 * Class to read the graph and config files from the GUI button (File open)
 * @author Kohwalter
 */
public class GuiReadFile {

    /**
     * Method to read the graph from the XML file
     * @param graphFile is the XML file with the graph information
     * @return 
     */
    public static DirectedGraph<Object, Edge> getGraph(File graphFile) {
        DirectedGraph<Object, Edge> g = new DirectedSparseMultigraph<>();
        Map<String, Object> vertices = new HashMap<>();
        try {
            String extension = FilenameUtils.getExtension(graphFile.toPath().toString());
            InputReader fileReader;
            if(extension.equalsIgnoreCase("xml"))
                fileReader = new UnityReader(graphFile);
            else
                fileReader = new PROVNReader(graphFile);
                
            fileReader.readFile();
            
            for (Edge edge : fileReader.getEdges()) {
                if(edge.hasAttribute(VariableNames.vertexNewTarget) || edge.hasAttribute(VariableNames.vertexNewSource)) {
                    if(fileReader instanceof UnityReader) {
                        Object newTarget = edge.getTarget();
                        Object newSource = edge.getSource();
                        if(edge.hasAttribute(VariableNames.vertexNewTarget)) {
                            newTarget = ((UnityReader)fileReader).getNewPointer(edge.getAttributeValue(VariableNames.vertexNewTarget));
                        }
                        if(edge.hasAttribute(VariableNames.vertexNewSource)) {
                            newSource = ((UnityReader)fileReader).getNewPointer(edge.getAttributeValue(VariableNames.vertexNewSource));
                        }
//                        System.out.println("newSourceID: " + edge.getAttributeValue("NewSource"));
//                        System.out.println("newTargetID: " + edge.getAttributeValue("NewTarget"));
//                        System.out.println("newSource: " + ((Vertex)newSource).getID());
//                        System.out.println("newTarget: " + ((Vertex)newTarget).getID());
                        g.addEdge(edge, newSource, newTarget);
                        vertices.put(((Vertex)newSource).getID(), newSource);
                        vertices.put(((Vertex)newTarget).getID(), newTarget);
                    }
                } else {
                    g.addEdge(edge, edge.getSource(), edge.getTarget());
                    vertices.put(((Vertex)edge.getSource()).getID(), edge.getSource());
                    vertices.put(((Vertex)edge.getTarget()).getID(), edge.getTarget());
                }
            }
            // Adds vertices that had no edge connecting then
            for(Vertex v : fileReader.getNodes()) {
                if(!vertices.containsKey(v.getID()))
                    g.addVertex(v);
            }
            
        } catch (URISyntaxException ex) {
            Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return g;
    }

    /**
     * Method to read the XML configuration file
     * @param variables
     * @param fileChooser is the file chooser from the interface
     * @param graphFrame is the tool's main frame
     * @param Layouts
     */
    public static void openConfigFile(Variables variables, JFileChooser fileChooser, JFrame graphFrame, JComboBox Layouts) {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(graphFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            variables.config.Initialize(file);
            variables.initConfig = true;
            variables.guiBackground = new GuiBackground();
            variables.guiBackground.InitBackground(variables, Layouts);
            GraphFrame.edgeFilterList.setSelectedIndex(0);
            GraphFrame.vertexFilterList.setSelectedIndex(0);

        } else {
            System.out.println("File access cancelled by user.");
        }
    }
    
    private static void loadGraph(Variables variables, DirectedGraph<Object, Edge> openGraph) {
        variables.graph = openGraph;
        variables.collapsedGraph = variables.graph;
        variables.filter.Filters(variables);
        variables.view.repaint();
        variables.initialGraph = false;
        Utils.NormalizeTime(variables.graph, false);
    }

    /**
     * Method to open the XML graph file
     * @param variables
     * @param fileChooser is the file chooser from the interface
     * @param graphFrame is the tool's main frame
     * @param Layouts is the tool's layout selection field
     */
    public static void openGraphFile(Variables variables, JFileChooser fileChooser, JFrame graphFrame, JComboBox Layouts) {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (variables.initConfig) {
            int returnVal = fileChooser.showOpenDialog(graphFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                loadGraph(variables, GuiReadFile.getGraph(fileChooser.getSelectedFile()));
                variables.originalGraphPath = fileChooser.getSelectedFile().getName();
            } else {
                System.out.println("File access cancelled by user.");
            }
            GuiInitialization.ReInitializeAfterReadingFile(variables, Layouts);
        }
    }
    
    /**
     * Method to load the merge configuration
     * @param variables
     * @param fileChooser is the filechooser from the interface
     * @param graphFrame is the main frame
     */
    public static void loadMergeConfiguration(Variables variables, JFileChooser fileChooser, JFrame graphFrame) {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog(graphFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            variables.mergeConfig = new SimilarityConfig();
            variables.mergeConfig.readFile(file);
        } else {
            System.out.println("File access cancelled by user.");
        }
    }
    
    /**
     * Method to load the similarity configuration
     * @param variables
     * @param fileChooser is the filechooser from the interface
     * @param graphFrame is the main frame
     */
    public static void loadSimilarityConfiguration(Variables variables, JFileChooser fileChooser, JFrame graphFrame) {
        int returnVal = fileChooser.showOpenDialog(graphFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            variables.similarityConfig = new SimilarityConfig();
            variables.similarityConfig.readFile(file);
            GraphFrame.attributeDisplaySimConfig.setSelected(false);
            
            // For Debug
            System.out.println("Similarity Config Loaded");

        } else {
            System.out.println("File access cancelled by user.");
        }
    }
    
    /**
     * Method from the tool's interface to merge the current graph with another from a file or folder
     * @param variables
     * @param fileChooser the filechooser from the interface
     * @param graphFrame the main frame
     * @param Layouts the layout combo box from the interface
     */
    public static void MergeGraph(Variables variables, JFileChooser fileChooser, JFrame graphFrame, JComboBox Layouts) {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fileChooser.showOpenDialog(graphFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            mergeFilesFromFolder(fileChooser.getSelectedFile(), variables);
//            DirectedGraph<Object, Edge> fileGraph = GuiReadFile.getGraph(fileChooser.getSelectedFile());
//            variables.mergingWithGraphPath = fileChooser.getSelectedFile().getName();
//            System.out.println("Original File: " + variables.originalGraphPath);
//            System.out.println("Merging File: " + variables.mergingWithGraphPath);
//            GraphMerger merger = new GraphMerger(variables.originalGraphPath, variables.mergingWithGraphPath);
//            DirectedGraph<Object, Edge> mergedGraph = merger.Merging(variables.graph, fileGraph, variables.mergeConfig.getRestrictionList(), variables.mergeConfig.getVocabulary(), variables.mergeConfig.getSimilarityThreshold(), variables.mergeConfig.getDefaultError(), variables.mergeConfig.getDefaultWeight());
//            
//            loadGraph(variables, mergedGraph);
//            System.out.println("Merging Complete!");
        } else {
            System.out.println("File access cancelled by user.");
        }
        variables.guiBackground.InitBackground(variables, Layouts);
        variables.updateNumberOfGraphs();
        GraphFrame.edgeFilterList.setSelectedIndex(0);
        GraphFrame.vertexFilterList.setSelectedIndex(0);
        PanCameraToFirstVertex(variables);
        variables.config.resetVertexModeInitializations();
    }
    
    /**
     * Method that merges all graphs from the selected folder
     * @param folder the selected item from the filechooser
     * @param variables 
     */
    public static void mergeFilesFromFolder(final File folder, Variables variables) {
        if(folder.isDirectory()) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    mergeFilesFromFolder(fileEntry, variables);
                } else {
                    mergeGraphs(variables, fileEntry);
                }
            }
        }
        else {
            mergeGraphs(variables, folder);
        }
    }
    
    /**
     * Method to merge two graphs
     * @param variables contains the first graph
     * @param file is the file that has the second graph
     */
    private static void mergeGraphs(Variables variables, File file) {
        DirectedGraph<Object, Edge> fileGraph = GuiReadFile.getGraph(file);
        variables.mergingWithGraphPath = file.getName();
//        System.out.println("Original File: " + variables.originalGraphPath);
        System.out.println("Merging File: " + variables.mergingWithGraphPath);
        GraphMerger merger = new GraphMerger(variables.originalGraphPath, variables.mergingWithGraphPath);
        DirectedGraph<Object, Edge> mergedGraph = merger.Merging(variables.graph, fileGraph, variables.mergeConfig.getRestrictionList(), variables.mergeConfig.getVocabulary(), variables.mergeConfig.getSimilarityThreshold(), variables.mergeConfig.getDefaultError(), variables.mergeConfig.getDefaultWeight());
        loadGraph(variables, mergedGraph);
    }
}
