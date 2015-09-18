/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

/**
 * Class that contains utility functions
 * @author Kohwalter
 */
public class Utils {

    /**
     * Method to check if it is possible to parse the value to float
     * @param value desired to be parsed to float
     * @return boolean
     */
    public static boolean tryParseFloat(String value) {
        value = value.replace(" ", "");
        value = value.replace(",", ".");
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    /**
     * Function that verifies if it is possible to convert the String to a float
     * @param value is the String to be verified
     * @return true if it is possible to convert to a float
     */
    public static float convertFloat(String value) {
        value = value.replace(" ", "");
        return Float.parseFloat(value);
    }
    
    /**
     * Function that verifies if it is possible to convert the String to an integer
     * @param value is the String to be verified
     * @return true if it is possible to convert to an integer
     */
    public static boolean tryParseInt(String value) {
        value = value.replace(" ", "");
        try {
            Integer.valueOf(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    /**
     * Function to convert a String to an Int value
     * @param value is the String to be converted
     * @return the int value of the string
     */
    public static int convertInt(String value) {
        value = value.replace(" ", "");
        return Integer.valueOf(value);
    }
    
    /**
     * Function to convert a String to an Int value, rounding it
     * @param value is the String to be converted
     * @return the rounded int value of the string
     */
    public static float roundToInt(String value) {
        value = value.replace(" ", "");
        return Math.round(Float.parseFloat(value));
    }
    
    /**
     * Function to compare two floats with an epsilon margin of error
     * @param f1 is the first float
     * @param f2 is the second float
     * @param epsilon is the margin of error for the comparison
     * @return true if floats are equals within the epsilon error margin
     */
    public static boolean FloatEqualTo(float f1, float f2, float epsilon) {
        return Math.abs(f1 - f2) <= epsilon;
    }
    
    /**
     * Clamp function to clamp the value between min and max
     * @param min is the minimal accepted value
     * @param max is the maximum accepted value
     * @param value is the value to be clamped between min and max
     * @return clamped value
     */
    public static double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(max, value));
    }
}
