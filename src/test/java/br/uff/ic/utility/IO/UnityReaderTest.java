/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.IO;

import br.uff.ic.utility.IO.UnityReader;
import br.uff.ic.utility.IO.BasePath;
import br.uff.ic.provviewer.GUI.GuiRun;
import br.uff.ic.provviewer.Variables;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kohwalter
 */
public class UnityReaderTest {
    
    public UnityReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of readFile method, of class UnityReader.
     */
    @Test
    public void testReadFile() throws Exception {
        System.out.println("readFile");
        testFiles(Variables.demo);
        testFiles(File.separator + "Graph" + File.separator + "Car_Tutorial.xml");
        testFiles(File.separator + "Graph" + File.separator + "Angry_Robots.xml");
        testFiles(File.separator + "Graph" + File.separator + "bus.xml");
        testFiles(File.separator + "Graph" + File.separator + "Map.xml");
        testFiles(File.separator + "Graph" + File.separator + "Input.xml");
    }
    
    public void testFiles(String path) throws Exception {
        System.out.println("File: " + path);
        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
        UnityReader instance = new UnityReader(f);
        instance.readFile();
    }

    /**
     * Test of readVertex method, of class UnityReader.
     */
    @Test
    public void testReadVertex() throws Exception {
        System.out.println("readVertex");
        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
        UnityReader instance = new UnityReader(f);
        instance.readVertex();
    }

    /**
     * Test of readEdge method, of class UnityReader.
     */
    @Test
    public void testReadEdge() throws Exception {
        System.out.println("readEdge");
        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
        UnityReader instance = new UnityReader(f);
        instance.readEdge();
    }
    
}
