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
 *
 * @author Kohwalter
 */
public class ActivityMultiScheme extends ColorScheme {
    
    public ActivityMultiScheme(boolean isZeroWhite, boolean isInverted, String attribute, String valuesList, String g, String y, boolean l) {
        super(false, false, attribute, valuesList, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        this.variables = variables;
        if (v instanceof ActivityVertex) {
            return GetAttributeColor(((ActivityVertex) v).getAttributeValue(this.attribute));
        }
        return ((Vertex) v).getColor();
    }

    //Method to return 7 dif types of colors depending on the value
    public Paint GetAttributeColor(String value) {
        for (int i = 0; i < this.value.length; i++) {
            if (value.equalsIgnoreCase(this.value[i])) {
                int r, g, b = 0;
                r = (int) (120);
                g = (int) (255 / (i + 1));
                b = (int) (255 / ((i + 1) * (i + 1)));
                return new Color(r, g, b);
            }
        }
        return new Color(128, 128, 128);
    }
}
