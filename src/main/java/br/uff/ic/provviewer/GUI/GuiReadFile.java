/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.graphmatching.GraphMerger;
import br.uff.ic.utility.graph.Edge;
import static br.uff.ic.provviewer.GUI.GuiFunctions.PanCameraToFirstVertex;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.utility.IO.UnityReader;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.IO.InputReader;
import br.uff.ic.utility.IO.PROVNReader;
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
//            variables.guiBackground.g2d.dispose();
            variables.guiBackground = new GuiBackground();
            variables.guiBackground.InitBackground(variables, Layouts);

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
                GuiInitialization.NormalizeTime(variables);
//                variables.file = fileChooser.getSelectedFile();
//                variables.graph = GuiReadFile.getGraph(variables.file);
//                variables.collapsedGraph = variables.graph;
//                variables.collapser.Filters(variables);
//                variables.view.repaint();
//                variables.initialGraph = false;
            } else {
                System.out.println("File access cancelled by user.");
            }
            variables.guiBackground.InitBackground(variables, Layouts);
            GraphFrame.edgeFilterList.setSelectedIndex(0);
            GraphFrame.vertexFilterList.setSelectedIndex(0);
            PanCameraToFirstVertex(variables);
        }
    }
    
    
    public static void MergeGraph(Variables variables, JFileChooser fileChooser, JFrame graphFrame, JComboBox Layouts) {
        int returnVal = fileChooser.showOpenDialog(graphFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            DirectedGraph<Object, Edge> fileGraph = GuiReadFile.getGraph(fileChooser.getSelectedFile());
            Map<String, AttributeErrorMargin> restrictionList = defaultRestriction(); 
            GraphMerger merger = new GraphMerger();
            DirectedGraph<Object, Edge> mergedGraph = merger.Merging(variables.graph, fileGraph, restrictionList, similarityThreshold, defaultErrorMargin);
            
            loadGraph(variables, mergedGraph);
        } else {
            System.out.println("File access cancelled by user.");
        }
        variables.guiBackground.InitBackground(variables, Layouts);
        GraphFrame.edgeFilterList.setSelectedIndex(0);
        GraphFrame.vertexFilterList.setSelectedIndex(0);
        PanCameraToFirstVertex(variables);
    }
    
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
