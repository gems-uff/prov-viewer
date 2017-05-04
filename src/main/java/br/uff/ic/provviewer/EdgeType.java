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
package br.uff.ic.provviewer;

import java.awt.Color;

/**
 * Class to define the parameters for each edge type
 * @author Kohwalter
 */
public class EdgeType {
    public String type;
    public String stroke;
    public String collapse;
    public float max;
    public float min;
    public int count;
    public float total;
    public boolean isInverted;
    public Color edgeColor;
    
    /**
     * Empty constructor
     */
    public EdgeType(){
        this.type = "";
        this.stroke = "";
        this.collapse = "";
        this.max = (float) Double.NEGATIVE_INFINITY;
        this.min = (float) Double.POSITIVE_INFINITY;
        this.count = 0;
        this.total = 0;
        this.isInverted = false;
        this.edgeColor = new Color(0 , 0, 0);
    }
    
    /**
     * Default constructor
     * @param type is the edge type
     * @param stroke is the stroke
     * @param collapse is the collapse
     */
    public EdgeType(String type, String stroke, String collapse){
        this.type = type;
        this.stroke = stroke;
        this.collapse = collapse;
        this.isInverted = false;
    }
    
    public EdgeType(String type, String stroke, String collapse, boolean isInverted){
        this.type = type;
        this.stroke = stroke;
        this.collapse = collapse;
        this.isInverted = isInverted;
    }
    
}
