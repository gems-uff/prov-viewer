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

package br.uff.ic.provviewer.Vertex;

import java.awt.Color;
import java.awt.Paint;

/**
 * Vertex Filter Painters using traffic light color (fixed, non-gradient) scheme
 * (Green, Yellow, Red)
 *
 * @author Kohwalter
 */
public class VertexFilterPainter {
    //Only value
    //Value comparison ranging from 0 to 100

    int valueGreenThreshold;
    int valueYellowThreshold;
    int iValueGreenThreshold;
    int iValueYellowThreshold;
    //Compare value with a constant. Color defined by %
    //Normally, the constant is the average or the sum of values from the same type
    double constantGreenThreshold;
    double constantYellowThreshold;

    /**
     * Value Threshold initialization. Set the lower threshold (i.e. Green color
     * at value > 70%)
     *
     * @param valueGreen Green color (hard value) threshold. i.e. value > 10 ==
     * Green color
     * @param valueYellow Yellow color (hard value) threshold. i.e. value > 4 ==
     * Yellow color
     * @param iValueGreen Inverted values Green color threshold (the lower the
     * better)
     * @param iValueYellow Inverted values Yellow color threshold (the lower the
     * better)
     * @param constantGreen Green color threshold (in percentage, i.e. value >
     * constant * 0.7 (70%) == Green color)
     * @param constantYellow Yellow color threshold (in percentage, i.e. value >
     * constant * 0.4 (40%) == Yellow color))
     */
    public VertexFilterPainter(int valueGreen, int valueYellow,
            int iValueGreen, int iValueYellow,
            double constantGreen, double constantYellow) {
        valueGreenThreshold = valueGreen;
        valueYellowThreshold = valueYellow;

        iValueGreenThreshold = iValueGreen;
        iValueYellowThreshold = iValueYellow;

        constantGreenThreshold = constantGreen;
        constantYellowThreshold = constantYellow;
    }

    /**
     * Method to define color by using hard value thresholds.
     *
     * @param value Value to be analyzed
     * @return color
     */
    public Paint ValueCompareColor(int value) {
        if (value > valueGreenThreshold) {
            return new Color(0, 255, 0);
        } else {
            if (value > valueYellowThreshold) {
                return new Color(255, 255, 0);
            } else {
                return new Color(255, 0, 0);
            }
        }
    }

    /**
     * Method to define color by using inverted value thresholds. The lower the
     * value is, the better.
     *
     * @param value Value to be analyzed
     * @return color
     */
    public Paint InvertedValueCompareColor(int value) {
        if (value < iValueGreenThreshold) {
            return new Color(0, 255, 0);
        } else {
            if (value < iValueYellowThreshold) {
                return new Color(255, 255, 0);
            } else {
                return new Color(255, 0, 0);
            }
        }
    }

    /**
     * Method to compare a value with a constant (in terms of percentage)
     *
     * @param value Value to be analyzed
     * @param constant Constant value to be used during the analysis
     * @return color
     */
    public Paint ConstantCompareColor(int value, float constant) {
        if (value > (constant * constantGreenThreshold)) {
            return new Color(0, 255, 0);
        } else {
            if (value > (constant * constantYellowThreshold)) {
                return new Color(255, 255, 0);
            } else if (value < 1000) {
                return new Color(140, 23, 23);
            } else {
                return new Color(255, 0, 0);
            }
        }
    }
}
