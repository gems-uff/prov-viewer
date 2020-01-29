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
package br.uff.ic.debugging;

import br.uff.ic.provviewer.GUI.GuiButtons;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Kohwalter
 */
public class WorkFlow {
    public void WorkFlow() {
        // TODO: Generate a graphFile for each trial and export it as trial_#.xml
        int rand = 3;
        int maxOk = 10;
        int a;
        int b;
        int c;
        int d;
        int e;
        int f;
        int i = 0;
        int j = 0;
        while(i < maxOk) {
            a = (int) (Math.random() * rand);
            b = (int) (Math.random() * rand);
            c = (int) (Math.random() * rand);
            d = (int) (Math.random() * rand);
            e = (int) (Math.random() * rand);
            f = (int) (Math.random() * rand);

            float v1;
            float v2;
            float v3;
            float v4;
            float v5;
            float v6;
            float v7;
            float v8;
            float v9;
            float num;
            String result;

            v1 = a + b;
            v2 = v1 + c;
            v3 = v2 * f;
            v4 = v3 - c * f;
            v5 = v1 + d;
            v6 = v5 / e;
            v7 = v4 * v6;
            v8 = v7 * a;
            v9 = v8 / b;
            num = v9;

//            System.out.println();
            j++;
//            System.out.println("Trial #" + j++);
//            System.out.println("a: " + a + " " + "b: " + b + " " + "c: " + c + " " + "d: " + d + " " + "e: " + e + " " + "f: " + f);
//            System.out.println("result: " + num);
            if (num == (int)num) {
//                System.out.println("OK #" + i);
                System.out.println("a: " + a + " " + "b: " + b + " " + "c: " + c + " " + "d: " + d + " " + "e: " + e + " " + "f: " + f + " OK #" + j);
                result = "OK";
                i++;
            }
            else {
//                System.out.println("Not OK");
                System.out.println("a: " + a + " " + "b: " + b + " " + "c: " + c + " " + "d: " + d + " " + "e: " + e + " " + "f: " + f + " Not OK #" + j);
                result = "Not OK";
            }
            String workflowName = "workflow_trial_" + (j - 1);
            Collection<Object> vertices = new ArrayList<>();
            Collection<Edge> edges = new ArrayList<>();
            Vertex vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7, vertex8, vertex9, vertexresult;
            vertex1 = new ActivityVertex("trial_" + (j - 1) + "_v1", "a + b", "");
            vertex1.addAttribute(new GraphAttribute("x", "0", workflowName));
            vertex1.addAttribute(new GraphAttribute("y", "1", workflowName));
            vertices.add(vertex1);
            
            vertex2 = new ActivityVertex("trial_" + (j - 1) + "_v2", "v1 + c", "");
            vertex2.addAttribute(new GraphAttribute("x", "1", workflowName));
            vertex2.addAttribute(new GraphAttribute("y", "3", workflowName));
            vertices.add(vertex2);
            
            vertex3 = new ActivityVertex("trial_" + (j - 1) + "_v3", "v2 * f", "");
            vertex3.addAttribute(new GraphAttribute("x", "2", workflowName));
            vertex3.addAttribute(new GraphAttribute("y", "3", workflowName));
            vertices.add(vertex3);
            
            vertex4 = new ActivityVertex("trial_" + (j - 1) + "_v4", "v3 - c * f", "");
            vertex4.addAttribute(new GraphAttribute("x", "3", workflowName));
            vertex4.addAttribute(new GraphAttribute("y", "3", workflowName));
            vertices.add(vertex4);
            
            vertex5 = new ActivityVertex("trial_" + (j - 1) + "_v5", "v1 + d", "");
            vertex5.addAttribute(new GraphAttribute("x", "1", workflowName));
            vertex5.addAttribute(new GraphAttribute("y", "0", workflowName));
            vertices.add(vertex5);
            
            vertex6 = new ActivityVertex("trial_" + (j - 1) + "_v6", "v5 / e", "");
            vertex6.addAttribute(new GraphAttribute("x", "2", workflowName));
            vertex6.addAttribute(new GraphAttribute("y", "0", workflowName));
            vertices.add(vertex6);
            
            vertex7 = new ActivityVertex("trial_" + (j - 1) + "_v7", "v4 * v6", "");
            vertex7.addAttribute(new GraphAttribute("x", "4", workflowName));
            vertex7.addAttribute(new GraphAttribute("y", "1", workflowName));
            vertices.add(vertex7);
            
            vertex8 = new ActivityVertex("trial_" + (j - 1) + "_v8", "v7 * a", "");
            vertex8.addAttribute(new GraphAttribute("x", "5", workflowName));
            vertex8.addAttribute(new GraphAttribute("y", "1", workflowName));
            vertices.add(vertex8);
            
            vertex9 = new ActivityVertex("trial_" + (j - 1) + "v9", "v8 / b", "");
            vertex9.addAttribute(new GraphAttribute("x", "6", workflowName));
            vertex9.addAttribute(new GraphAttribute("y", "1", workflowName));
            vertices.add(vertex9);
            
            vertexresult = new ActivityVertex("trial_" + (j - 1) + "_result", result, "");
            vertexresult.addAttribute(new GraphAttribute("x", "7", workflowName));
            vertexresult.addAttribute(new GraphAttribute("y", "1", workflowName));
            vertices.add(vertexresult);
            
            Edge edge;
            edge = new Edge("trial_" + (j - 1) + "_e1", vertex1, vertex2);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e2", vertex2, vertex3);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e3", vertex3, vertex4);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e4", vertex1, vertex5);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e5", vertex5, vertex6);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e6", vertex6, vertex7);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e7", vertex4, vertex7);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e8", vertex7, vertex8);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e9", vertex8, vertex9);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e10", vertex9, vertexresult);
            edges.add(edge);
            
            Vertex ea, eb, ec, ed, ee, ef;
            ea = new EntityVertex("trial_" + (j - 1) + "_a", "a", "");
            ea.addAttribute(new GraphAttribute("value", Integer.toString(a), workflowName));
            ea.addAttribute(new GraphAttribute("x", "2", workflowName));
            ea.addAttribute(new GraphAttribute("y", Integer.toString(5 + a), workflowName));
            vertices.add(ea);
            
            eb = new EntityVertex("trial_" + (j - 1) + "_b", "b", "");
            eb.addAttribute(new GraphAttribute("value", Integer.toString(b), workflowName));
            eb.addAttribute(new GraphAttribute("x", "3", workflowName));
            eb.addAttribute(new GraphAttribute("y", Integer.toString(5 + b), workflowName));
            vertices.add(eb);
            
            ec = new EntityVertex("trial_" + (j - 1) + "_c", "c", "");
            ec.addAttribute(new GraphAttribute("value", Integer.toString(c), workflowName));
            ec.addAttribute(new GraphAttribute("x", "4", workflowName));
            ec.addAttribute(new GraphAttribute("y", Integer.toString(5 + c), workflowName));
            vertices.add(ec);
            
            ed = new EntityVertex("trial_" + (j - 1) + "_d", "d", "");
            ed.addAttribute(new GraphAttribute("value", Integer.toString(d), workflowName));
            ed.addAttribute(new GraphAttribute("x", "5", workflowName));
            ed.addAttribute(new GraphAttribute("y", Integer.toString(5 + d), workflowName));
            vertices.add(ed);
            
            ee = new EntityVertex("trial_" + (j - 1) + "_e", "e", "");
            ee.addAttribute(new GraphAttribute("value", Integer.toString(e), workflowName));
            ee.addAttribute(new GraphAttribute("x", "6", workflowName));
            ee.addAttribute(new GraphAttribute("y", Integer.toString(5 + e), workflowName));
            vertices.add(ee);
            
            ef = new EntityVertex("trial_" + (j - 1) + "_f", "f", "");
            ef.addAttribute(new GraphAttribute("value", Integer.toString(f), workflowName));
            ef.addAttribute(new GraphAttribute("x", "7", workflowName));
            ef.addAttribute(new GraphAttribute("y", Integer.toString(5 + f), workflowName));
            vertices.add(ef);
            
            edge = new Edge("trial_" + (j - 1) + "_e11", ea, vertex1);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e12", ea, vertex8);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e13", eb, vertex1);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e14", eb, vertex9);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e15", ec, vertex4);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e16", ec, vertex2);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e17", ed, vertex5);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e18", ee, vertex6);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e19", ef, vertex3);
            edges.add(edge);
            edge = new Edge("trial_" + (j - 1) + "_e20", ef, vertex4);
            edges.add(edge);
            
            
            GuiButtons.ExportGraphXML(vertices, edges, workflowName);
        }
    }
}
