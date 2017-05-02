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

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.GraphUtils;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Kohwalter
 */
public abstract class ColorScheme {

    public String attribute;
    public String[] value;
    public double max = Double.NEGATIVE_INFINITY;
    public double min = Double.POSITIVE_INFINITY;
    public String givenMax;
    public String givenMin;
    public boolean limited;
    private boolean computedMinMax;
    public String restrictedAttribute;
    public String restrictedValue;
    private boolean isZeroWhite = true;
    private boolean isInverted = false;
    public Variables variables;
    public double derivateMax = Double.NEGATIVE_INFINITY;
    public double derivateMin = Double.POSITIVE_INFINITY;

    /**
     * This constructor is used by the Default color scheme
     *
     * @param attribute
     */
    public ColorScheme(String attribute) {
        this.attribute = attribute;
        this.value = null;
        this.givenMax = null;
        this.givenMin = null;
        this.limited = false;
        this.computedMinMax = false;
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
    }

    /**
     * All new Vertex Paint Mode classes must use a constructor with 4 params,
     * in the following order and types: String, String, double, double So it
     * can be recognized by config.java Note that the second String actually
     * goes to to a String[] variable and is split with " " due to how XML list
     * works
     *
     * @param attribute
     * @param value
     * @param max
     * @param min
     * @param limited
     */
    public ColorScheme(boolean isWhite, boolean isInverted, String attribute, String value, String max, String min, boolean limited) {
        this.attribute = attribute;
        this.value = value.split(" ");
        this.givenMax = max;
        this.givenMin = min;
        this.limited = limited;
        this.computedMinMax = false;
        this.isZeroWhite = isWhite;
        this.isInverted = isInverted;
    }

    public ColorScheme(boolean isWhite, boolean isInverted, String attribute, String value, String max, String min, boolean limited, String rA, String rV) {
        this.attribute = attribute;
        this.value = value.split(" ");
        this.givenMax = max;
        this.givenMin = min;
        this.limited = limited;
        this.computedMinMax = false;
        this.restrictedAttribute = rA;
        this.restrictedValue = rV;
        this.isZeroWhite = isWhite;
        this.isInverted = isInverted;
    }

    public String GetName() {
        return attribute;
    }

    public Paint CompareValue(float value, double min, double max, boolean inverted) {
        if (isZeroWhite) {
            return splittedTrafficLight(value, min, max, inverted);
        } else {
            return trafficLight(value, min, max, inverted);
        }
    }

    public Paint trafficLight(float value, double min, double max, boolean inverted) {
        if (inverted) {
            double aux = min;
            min = max;
            max = aux;
        }
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
        return new Color(Math.min(255, 510 - proportion), Math.min(255, proportion), 0);
    }

    public Paint splittedTrafficLight(float value, double min, double max, boolean inverted) {
        // normalize the color between 0 and 1
        float vPositive;
        float vNegative;

        // Fix one of the extremes to be zero in order to always have white as zero
        if (min > 0) {
            min = 0;
        }
        if (max < 0) {
            max = 0;
        }

        if (min < 0 && max > 0) {
            vNegative = (float) (Math.abs(value - min) / (float) Math.abs(0 - min));
            vPositive = (float) (Math.abs(value - 0) / (float) Math.abs(max - 0));
        } else {
            vPositive = (float) (Math.abs(value - min) / (float) Math.abs(max - min));
            vNegative = vPositive;
        }

        if (value == 0) {
            return new Color(255, 255, 255);
        }
        if (!inverted) {
            if (value > 0) {
                return compareValueGreen(vPositive, min, max);
            } else {
                return compareValueRed(1 - vNegative, min, max);
            }
        } else {
            if (value >= 0) {
                return compareValueRed(vPositive, min, max);
            } else {
                return compareValueGreen(1 - vNegative, min, max);
            }
        }
    }

    public Paint compareValueGreen(float value, double min, double max) {
        int aR = 255;
        int aG = 255;
        int aB = 255;  // RGB for the lowest value.
        int bR = 0;
        int bG = 255;
        int bB = 0;    // RGB for the highest value.

        return gradientColor(aR, aG, aB, bR, bG, bB, value);
    }

    public Paint compareValueRed(float value, double min, double max) {
        int aR = 255;
        int aG = 255;
        int aB = 255;  // RGB for the lowest value.
        int bR = 255;
        int bG = 0;
        int bB = 0;    // RGB for the highest value.
        return gradientColor(aR, aG, aB, bR, bG, bB, value);
    }

    private Paint gradientColor(int aR, int aG, int aB, int bR, int bG, int bB, float v) {
        int red = (int) ((float) (bR - aR) * v + aR);      // Evaluated as -255*value + 255.
        int green = (int) ((float) (bG - aG) * v + aG);      // Evaluates as 0.
        int blue = (int) ((float) (bB - aB) * v + aB);      // Evaluates as 255*value + 0.
        return new Color(red, green, blue);
    }

    public Paint GetMinMaxColor(Object v) {
        if (!((Vertex) v).getAttributeValue(this.attribute).contentEquals("Unknown")) {
            // 
//            boolean isDerivate = true;
            if (variables.doDerivate) {
                float slope = GraphUtils.getSlope(v, this);
                if (slope == Float.NEGATIVE_INFINITY) {
                    return new Color(0, 0, 0);
                } else if (slope < this.derivateMin) {
                    slope = (float) this.derivateMin;
                } else if (slope > this.derivateMax) {
                    slope = (float) this.derivateMax;
                }
                return this.CompareValue(slope, this.derivateMin, this.derivateMax, isInverted);
            } //
            else if (!limited) {
                return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), this.min, this.max, isInverted);
            } else {
                if (this.givenMax == null) {
                    return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), Double.parseDouble(this.givenMin), this.max, isInverted);
                }
                if (this.givenMin == null) {
                    return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), this.min, Double.parseDouble(this.givenMax), isInverted);
                } else {
                    return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), Double.parseDouble(this.givenMin), Double.parseDouble(this.givenMax), isInverted);
                }
            }
        }
        return ((Vertex) v).getColor();
    }

    public void ComputeValue(DirectedGraph<Object, Edge> graph, boolean isActivity) {
        if (variables.changedOutliersOption && variables.doDerivate) {
            computedMinMax = false;
            variables.changedOutliersOption = !variables.changedOutliersOption;
        }
        if (!computedMinMax) {
            Collection<Object> nodes = graph.getVertices();
            ArrayList<Float> derivateValues = new ArrayList<>();
            for (Object node : nodes) {
                if (!((Vertex) node).getAttributeValue(this.attribute).contentEquals("Unknown")) {
                    this.max = Math.max(this.max, ((Vertex) node).getAttributeValueFloat(this.attribute));
                    this.min = Math.min(this.min, ((Vertex) node).getAttributeValueFloat(this.attribute));
                    derivateValues.add(GraphUtils.getSlope(node, this));
                }
            }
            derivateValues = Utils.removeInfinity(derivateValues);
            if (variables.removeDerivateOutliers) {
                derivateValues = Utils.removeOutLierAnalysis(derivateValues);
            } else {
                Collections.sort(derivateValues);
            }
            this.derivateMax = derivateValues.get(derivateValues.size() - 1);
            this.derivateMin = derivateValues.get(0);
            computedMinMax = true;
        }
    }

    public void ComputeRestrictedValue(DirectedGraph<Object, Edge> graph, boolean isActivity, String aRestriction, String aValue) {
        if (variables.changedOutliersOption && variables.doDerivate) {
            computedMinMax = false;
            variables.changedOutliersOption = !variables.changedOutliersOption;
        }
        if (!computedMinMax) {
            Collection<Object> nodes = graph.getVertices();
            ArrayList<Float> derivateValues = new ArrayList<>();
            for (Object node : nodes) {
                if (!((Vertex) node).getAttributeValue(this.attribute).contentEquals("Unknown")) {
                    if (((Vertex) node).getAttributeValue(aRestriction).equalsIgnoreCase(aValue)) {
                        this.max = Math.max(this.max, ((Vertex) node).getAttributeValueFloat(this.attribute));
                        this.min = Math.min(this.min, ((Vertex) node).getAttributeValueFloat(this.attribute));
                        derivateValues.add(GraphUtils.getSlope(node, this));
                    }
                }
            }
            derivateValues = Utils.removeInfinity(derivateValues);
            if (variables.removeDerivateOutliers) {
                derivateValues = Utils.removeOutLierAnalysis(derivateValues);
            } else {
                Collections.sort(derivateValues);
            }
            this.derivateMax = derivateValues.get(derivateValues.size() - 1);
            this.derivateMin = derivateValues.get(0);
            computedMinMax = true;
        }
    }

    public abstract Paint Execute(Object v, final Variables variables);

}
