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
package br.uff.ic.provviewer.Input;

import br.uff.ic.utility.IO.BasePath;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.GUI.GuiReadFile;
import br.uff.ic.provviewer.GUI.GuiRun;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kohwalter
 */
public class ConfigTest {
    
    public ConfigTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        File graphFile = new File(BasePath.getBasePathForClass(GuiRun.class) + File.separator + "Graph" + File.separator + "Car_Tutorial.xml");
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
//        System.out.println("Initialize");
        Config instance = new Config();
        instance.Initialize(new Variables());
    }

    /**
     * Test of ComputeCoordScale method, of class Config.
     */
    @Test
    public void testComputeCoordScale() {
//        System.out.println("ComputeCoordScale");
        Config instance = new Config();
        instance.ComputeCoordScale();
    }

    /**
     * Test of Initialize method, of class Config.
     */
    @Test
    public void testInitialize_File() {
//        System.out.println("Initialize");
        testFiles("Angry_Robots_config.xml");
        testFiles("SDM_config.xml");
        testFiles("PROV_config.xml");
        testFiles("2D_Tower_Defense_config.xml");
        testFiles("Car_Tutorial_config.xml");
        testFiles("rio_de_janeiro_cidade_config.xml");
        
    }
    
    public void testFiles(String path) {
//        System.out.println("File: " + path);
        String demoPath = File.separator + "Config" + File.separator + path;
        File fXmlFile = new File(BasePath.getBasePathForClass(Config.class) + demoPath);
        Config instance = new Config();
        instance.Initialize(fXmlFile);
    }
    
}
