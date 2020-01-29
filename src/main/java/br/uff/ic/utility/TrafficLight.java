/*
 * The MIT License
 *
 * Copyright 2020 Kohwalter.
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

import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class TrafficLight {

    private static Paint gradientColor(int aR, int aG, int aB, int bR, int bG, int bB, double v, int alpha) {
        int red = (int) ((double) (bR - aR) * v + aR); // Evaluated as -255*value + 255.
        int green = (int) ((double) (bG - aG) * v + aG); // Evaluates as 0.
        int blue = (int) ((double) (bB - aB) * v + aB); // Evaluates as 255*value + 0.
        return new Color(red, green, blue, alpha);
    }

    public static Paint compareValueGreen(double value, double min, double max, int alpha) {
        int aR = 255;
        int aG = 255;
        int aB = 255; // RGB for the lowest value.
        int bR = 0;
        int bG = 128;
        int bB = 0; // RGB for the highest value.
        return gradientColor(aR, aG, aB, bR, bG, bB, value, alpha);
    }

    public static Paint compareValueRed(double value, double min, double max, int alpha) {
        int aR = 255;
        int aG = 255;
        int aB = 255; // RGB for the lowest value.
        int bR = 255;
        int bG = 0;
        int bB = 0; // RGB for the highest value.
        return gradientColor(aR, aG, aB, bR, bG, bB, value, alpha);
    }
    public static Paint splittedTrafficLight(double value, double min, double max, boolean inverted) {
        return splittedTrafficLight(value, min, max, inverted, 255);
    }
    public static Paint splittedTrafficLight(double value, double min, double max, boolean inverted, int alpha) {
        // normalize the color between 0 and 1
        double vPositive;
        double vNegative;
        // Fix one of the extremes to be zero in order to always have white as zero
        if (min > 0) {
            min = 0;
        }
        if (max < 0) {
            max = 0;
        }
        if (min < 0 && max > 0) {
            vNegative = (double) (Math.abs(value - min) / (double) Math.abs(0 - min));
            vPositive = (double) (Math.abs(value - 0) / (double) Math.abs(max - 0));
        } else {
            vPositive = (double) (Math.abs(value - min) / (double) Math.abs(max - min));
            vNegative = vPositive;
        }
        if (value == 0) {
            return new Color(255, 255, 255, alpha);
        }
        if (!inverted) {
            if (value > 0) {
                return compareValueGreen(vPositive, min, max, alpha);
            } else {
                return compareValueRed(1 - vNegative, min, max, alpha);
            }
        } else {
            if (value >= 0) {
                return compareValueRed(vPositive, min, max, alpha);
            } else {
                return compareValueGreen(1 - vNegative, min, max, alpha);
            }
        }
    }

    /**
     * Basic traffic light color scheme, returning a gradient from red to orange
     * to green
     *
     * @param value is the current value
     * @param min is the minimum possible value
     * @param max is the maximum possilble value
     * @param inverted defines if it is red to green or green to red
     * @return the color based on the current value
     */
    public static Paint trafficLight(double value, double min, double max, boolean inverted) {
        if (inverted) {
            double aux = min;
            min = max;
            max = aux;
        }
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (double) Math.abs(max - min));
        return new Color(Math.min(255, 510 - proportion), Math.min(255, proportion), 0);
    }

    /**
     * Method that return a grayscale color from the grayscale gradient
     *
     * @param value is the current value that we want the color
     * @param max is the maximum possible value that the previous value can
     * assume
     * @return the gray color corresponding to the value, which is based on
     * value/max
     */
    public static Color getGrayscaleColor(double value, double max) {
        if (max == 0) {
            max = 1;
        }
        double gray = value / max * 191;
        int rgbNum = 255 - (int) gray;
        return new Color(rgbNum, rgbNum, rgbNum);
    }

}
