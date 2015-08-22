/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.Edge.Edge;
import static br.uff.ic.provviewer.GUI.GuiFunctions.PanCameraToFirstVertex;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Input.UnityReader;
import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Class to read the graph and config files from the GUI button (File open)
 * @author Kohwalter
 */
public class GuiReadFile {

    /**
     * Method to read the graph from the XML file
     * @param xmlGraph is the XML file with the graph information
     * @return 
     */
    public static DirectedGraph<Object, Edge> getGraph(File xmlGraph) {
        DirectedGraph<Object, Edge> g = new DirectedSparseMultigraph<Object, Edge>();
        try {
            UnityReader xmlReader = new UnityReader(xmlGraph);
            xmlReader.readFile();
            for (Edge edge : xmlReader.getEdges()) {
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
     */
    public static void openConfigFile(Variables variables, JFileChooser fileChooser, JFrame graphFrame) {
        int returnVal = fileChooser.showOpenDialog(graphFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            variables.config.Initialize(file);
            variables.initConfig = true;

        } else {
            System.out.println("File access cancelled by user.");
        }
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
                variables.file = fileChooser.getSelectedFile();
                variables.graph = GuiReadFile.getGraph(variables.file);
                variables.collapsedGraph = variables.graph;
                variables.collapser.Filters(variables);
                variables.view.repaint();
                variables.initialGraph = false;
            } else {
                System.out.println("File access cancelled by user.");
            }
            GuiBackground.InitBackground(variables, Layouts);
            GraphFrame.FilterList.setSelectedIndex(0);
            PanCameraToFirstVertex(variables);
        }
    }
}
