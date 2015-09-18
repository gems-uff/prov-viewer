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
    
    public static float convertFloat(String value) {
        value = value.replace(" ", "");
        return Float.parseFloat(value);
    }
    
    public static boolean tryParseInt(String value) {
        value = value.replace(" ", "");
        try {
            Integer.valueOf(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    public static int convertInt(String value) {
        value = value.replace(" ", "");
        return Integer.valueOf(value);
    }
    
    public static float roundToInt(String value) {
        value = value.replace(" ", "");
        return Math.round(Float.parseFloat(value));
    }
    
    public static boolean FloatEqualTo(float left, float right, float epsilon) {
        return Math.abs(left - right) <= epsilon;
    }
}
