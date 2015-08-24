/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.provviewer.BasePath;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.GUI.GuiReadFile;
import br.uff.ic.provviewer.GUI.GuiRun;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kohwalter
 */
public class ConfigTest {
    
    public ConfigTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        File graphFile = new File(BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
        final DirectedGraph<Object, Edge> graph = GuiReadFile.getGraph(graphFile);
        GraphFrame frame = new GraphFrame(graph);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of Initialize method, of class Config.
     */
    @Test
    public void testInitialize_0args() {
        System.out.println("Initialize");
        Config instance = new Config();
        instance.Initialize();
    }

    /**
     * Test of ComputeCoordScale method, of class Config.
     */
    @Test
    public void testComputeCoordScale() {
        System.out.println("ComputeCoordScale");
        Config instance = new Config();
        instance.ComputeCoordScale();
    }

    /**
     * Test of Initialize method, of class Config.
     */
    @Test
    public void testInitialize_File() {
        System.out.println("Initialize");
        testFiles("Angry_Robots_config.xml");
        testFiles("bus_config.xml");
        testFiles("config.xml");
        testFiles("map_config.xml");
        testFiles("2D_Provenance_config.xml");
        
    }
    
    public void testFiles(String path) {
        System.out.println("File: " + path);
        String demoPath = File.separator + "Config" + File.separator + path;
        File fXmlFile = new File(BasePath.getBasePathForClass(Config.class) + demoPath);
        Config instance = new Config();
        instance.Initialize(fXmlFile);
    }
    
}
