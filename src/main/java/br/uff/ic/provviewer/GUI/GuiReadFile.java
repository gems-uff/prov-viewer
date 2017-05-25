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
import br.uff.ic.utility.IO.UnityReader;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.IO.InputReader;
import br.uff.ic.utility.IO.PROVNReader;
import br.uff.ic.utility.Utils;
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
    
    static float similarityThreshold = 0.95f;
    static String defaultErrorMargin = "5%";

    /**
     * Method to read the graph from the XML file
     * @param graphFile is the XML file with the graph information
     * @return 
     */
    public static DirectedGraph<Object, Edge> getGraph(File graphFile) {
        DirectedGraph<Object, Edge> g = new DirectedSparseMultigraph<>();
        try {
            String extension = FilenameUtils.getExtension(graphFile.toPath().toString());
            InputReader fileReader;
            if(extension.equalsIgnoreCase("xml"))
                fileReader = new UnityReader(graphFile);
            else
                fileReader = new PROVNReader(graphFile);
                
            fileReader.readFile();
            
            for (Edge edge : fileReader.getEdges()) {
                g.addEdge(edge, edge.getSource(), edge.getTarget());
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
        if (variables.initConfig) {
            int returnVal = fileChooser.showOpenDialog(graphFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                loadGraph(variables, GuiReadFile.getGraph(fileChooser.getSelectedFile()));
                variables.originalGraphPath = fileChooser.getSelectedFile().getName();
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
    }
    
    /**
     * 
     * @param variables
     * @param fileChooser
     * @param graphFrame 
     */
    public static void loadMergeConfiguration(Variables variables, JFileChooser fileChooser, JFrame graphFrame) {
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
     * 
     * @param variables
     * @param fileChooser
     * @param graphFrame 
     */
    public static void loadSimilarityConfiguration(Variables variables, JFileChooser fileChooser, JFrame graphFrame) {
        int returnVal = fileChooser.showOpenDialog(graphFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            variables.similarityConfig = new SimilarityConfig();
            variables.similarityConfig.readFile(file);
            
            // For Debug
            System.out.println("Similarity Config Loaded");
            System.out.println(variables.similarityConfig.getDefaultError());
            System.out.println(variables.similarityConfig.getSimilarityThreshold());

        } else {
            System.out.println("File access cancelled by user.");
        }
    }
    
    /**
     * 
     * @param variables
     * @param fileChooser
     * @param graphFrame
     * @param Layouts 
     */
    public static void MergeGraph(Variables variables, JFileChooser fileChooser, JFrame graphFrame, JComboBox Layouts) {
        int returnVal = fileChooser.showOpenDialog(graphFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            DirectedGraph<Object, Edge> fileGraph = GuiReadFile.getGraph(fileChooser.getSelectedFile());
            variables.mergingWithGraphPath = fileChooser.getSelectedFile().getName();
            System.out.println("Original File: " + variables.originalGraphPath);
            System.out.println("Merging File: " + variables.mergingWithGraphPath);
            GraphMerger merger = new GraphMerger(variables.originalGraphPath, variables.mergingWithGraphPath);
            DirectedGraph<Object, Edge> mergedGraph = merger.Merging(variables.graph, fileGraph, variables.mergeConfig.getRestrictionList(), variables.mergeConfig.getVocabulary(), variables.mergeConfig.getSimilarityThreshold(), variables.mergeConfig.getDefaultError(), variables.mergeConfig.getDefaultWeight());
            
            loadGraph(variables, mergedGraph);
            System.out.println("Merging Complete!");
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
     * Changed to use the AngryBotsMergeConfigExample.xml
     * @deprecated
     * @return 
     */
    private static Map<String, AttributeErrorMargin> defaultRestriction(){
        Map<String, AttributeErrorMargin> restrictionList = new HashMap<String, AttributeErrorMargin>();
        AttributeErrorMargin epsilon;
        
        epsilon = new AttributeErrorMargin("ObjectPosition_X", "5%");
        restrictionList.put("ObjectPosition_X", epsilon);
        
        epsilon = new AttributeErrorMargin("ObjectPosition_Y", "5%");
        restrictionList.put("ObjectPosition_Y", epsilon);
        
        epsilon = new AttributeErrorMargin("ObjectPosition_Z", "5%");
        restrictionList.put("ObjectPosition_Z", epsilon);
        return restrictionList;
    }
}
