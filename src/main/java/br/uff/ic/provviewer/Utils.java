/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer;

/**
 *
 * @author Kohwalter
 */
public class Utils {

    /**
     * Method to check if it is possible to parse the value to float
     * @param value desired to be parsed to float
     * @return boolean
     */
    public static boolean tryParseFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
}
