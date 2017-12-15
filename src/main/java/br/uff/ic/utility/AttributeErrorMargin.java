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

package br.uff.ic.utility;

/**
 * Class used for creating a list of acceptable error margins for specific attributes
 * Allows to define the weight for the attribute to be used during similarity evaluation
 * @author Kohwalter
 */
public class AttributeErrorMargin {
    private String name;
    private String value;
    private float weight;

    public AttributeErrorMargin(String n, String v) {
        this.name = n;
        this.value = v;
        this.weight = 1;
    }
    
    public AttributeErrorMargin(String n, String v, float w) {
        this.name = n;
        this.value = v;
        this.weight = w;
    }
    
    /**
     * Method to return the attribute name
     * @return name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Method to return the attribute value
     * @return name
     */
    public String getValue() {
        return this.value;
    }
    
    /**
     * Method to return the attribute weight
     * @return name
     */
    public float getWeight() {
        return this.weight;
    }
    
    /**
     * Method to set the attribute name
     * @param t is the new attribute name
     */
    public void setName(String t) {
        this.name = t;
    }

    /**
     * Method to set the attribute value
     * @param t is the new value
     */
    public void setValue(String t) {
        this.value = t;
    }
    
    /**
     * Method to set the attribute weight
     * @param t is the new weight
     */
    public void setWeight(float t) {
        this.weight = t;
    }
}
