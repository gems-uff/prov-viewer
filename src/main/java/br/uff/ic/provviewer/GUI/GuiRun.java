/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.BasePath;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.File;

/**
 * Class to run the application
 * @author Kohwalter
 */
public class GuiRun {
    
    /**
     * Main loop of the program
     */
    public static void Run()
    {
        System.out.println("Graph: " + BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
        File graphFile = new File(BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
        final DirectedGraph<Object, Edge> graph = GuiReadFile.getGraph(graphFile);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
                
            @Override
                public void run() {
                    new GraphFrame(graph).setVisible(true);
                }
            });
    }
    
}
