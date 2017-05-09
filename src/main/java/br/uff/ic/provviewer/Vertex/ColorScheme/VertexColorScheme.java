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

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.AttValueColor;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kohwalter
 */
public class VertexColorScheme extends ColorScheme {
    public List<AttValueColor> activityVC = new ArrayList<>();
    public List<AttValueColor> entityVC = new ArrayList<>();
    public List<AttValueColor> agentVC = new ArrayList<>();

    public VertexColorScheme(String attribute, List<AttValueColor> aVC, List<AttValueColor> eVC, List<AttValueColor> agVC) {
        super(attribute);
        activityVC.addAll(aVC);
        entityVC.addAll(eVC);
        agentVC.addAll(agVC);
    }

    public VertexColorScheme(boolean isZeroWhite, boolean isInverted, String attribute, String empty, String g, String y, boolean l) {
        super(isZeroWhite, isInverted, attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        this.variables = variables;
//        if (v instanceof ActivityVertex) {
//            return ((ActivityVertex) v).getDefaultColor(variables);
//        }
//        else
//            return ((Vertex) v).getColor();
        if (v instanceof ActivityVertex) {
            return getDefaultColor(this.activityVC, v);
        }
        else if (v instanceof EntityVertex) {
            return getDefaultColor(this.entityVC, v);
        }
        else
            return getDefaultColor(this.agentVC, v);
    }
    
    public Paint getDefaultColor(List<AttValueColor> avc, Object v) {
        for (int i = 0; i < avc.size(); i++) {
            if (((Vertex)v).getAttributeValue(avc.get(i).name).equalsIgnoreCase(avc.get(i).value)) {
                return avc.get(i).color;
            }
        }
        return ((Vertex) v).getColor();
    }
}