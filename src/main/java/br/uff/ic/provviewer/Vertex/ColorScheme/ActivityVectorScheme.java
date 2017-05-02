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
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Color;
import java.awt.Paint;

/**
 * Uses a Normalized Vector to color the vertex accordingly
 * @author Kohwalter
 */
public class ActivityVectorScheme extends ColorScheme {

    public ActivityVectorScheme(boolean isZeroWhite, boolean isInverted, String attribute, String empty, String g, String y, boolean l) {
        super(false, false, attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        String vecValue;
        if (v instanceof ActivityVertex) {
            vecValue = ((ActivityVertex) v).getAttributeValue(this.attribute);
            vecValue = vecValue.replace("(", "");
            vecValue = vecValue.replace(")", "");
            vecValue = vecValue.replace(" ", "");
            String[] vec3 = vecValue.split(","); 
            double vx = Float.parseFloat(vec3[0]);
            double vy = Float.parseFloat(vec3[1]);
            double vz = Float.parseFloat(vec3[2]);
            int cx = (int) ((vx * 0.5 + 0.5) * 255);
            int cy = (int) ((vy * 0.5 + 0.5) * 255);
            int cz = (int) ((vz * 0.5 + 0.5) * 255);
            
            return new Color (cx,cy,cz);

        }
        return ((Vertex) v).getColor();
    }
}
