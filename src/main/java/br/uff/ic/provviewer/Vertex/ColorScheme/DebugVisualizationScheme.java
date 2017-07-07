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

package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kohwalter
 */
public class DebugVisualizationScheme extends ColorScheme {
    int totalNumberGraphs = 2;
    List<Vertex> alwaysWrong = new ArrayList<>();
    List<Vertex> reasonsForFailure = new ArrayList<>();
    

    public DebugVisualizationScheme(String attribute, List<Vertex> alwaysWrong, List<Vertex> reasonsForFailure) {
        super(attribute);
        this.alwaysWrong = alwaysWrong;
        this.reasonsForFailure = reasonsForFailure;
    }

    public DebugVisualizationScheme(boolean isZeroWhite, boolean isInverted, String attribute, String empty, String g, String y, boolean l) {
        super(false, false, attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        String att = this.attribute;
        att = att.replace("Debug_", "");
        if(!isInitialized) {
            totalNumberGraphs = variables.numberOfGraphs;
            isInitialized = true;
        }
        String[] graphFiles = ((Vertex)v).getAttributeValues(VariableNames.GraphFile);
        if(reasonsForFailure.contains((Vertex)v)) {
            return new Color(255, 255, 0);
        }
        else if(alwaysWrong.contains((Vertex)v)) {
            return new Color(0, 255, 255);
        }
        else if (graphFiles.length == totalNumberGraphs)
            return new Color(200, 200, 200);
        else if (((Vertex)v).getAttributeValue(VariableNames.GraphFile).contains(att))
            return new Color(0, 255, 0);
        else
//            return ((Vertex) v).getColor();
            return new Color(255, 0, 0);
    }
}
