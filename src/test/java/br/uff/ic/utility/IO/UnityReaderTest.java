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
package br.uff.ic.utility.IO;

import br.uff.ic.provviewer.GUI.GuiRun;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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
//        System.out.println("readFile");
//        testFiles(Variables.demo);
        testFiles(File.separator + "Graph" + File.separator + "Car_Tutorial.xml");
        testFiles(File.separator + "Graph" + File.separator + "Angry_Robots.xml");
        testFiles(File.separator + "Graph" + File.separator + "Map.xml");
        testFiles(File.separator + "Graph" + File.separator + "SDM_Example.xml");
    }
    
    public void testFiles(String path) throws Exception {
        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + path);
        UnityReader instance = new UnityReader(f);
        instance.readFile();
    }
}
