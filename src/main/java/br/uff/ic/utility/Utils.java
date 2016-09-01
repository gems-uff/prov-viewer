/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

import br.uff.ic.utility.IO.XMLWriter;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import br.uff.ic.utility.graphgenerator.NoiseGraph;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Class that contains utility functions
 *
 * @author Kohwalter
 */
public class Utils {

    /**
     * Method to export the graph in an xml format
     * @param graph is the desired graph to be exported 
     * @param fileName is the name of the file (without extension)
     */
    public static void exportGraph(DirectedGraph<Object, Edge> graph, String fileName) {
        try {
            XMLWriter xmlWriter = new XMLWriter(graph.getVertices(), graph.getEdges());
            xmlWriter.saveToXML(fileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoiseGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Method to check if it is possible to parse the value to float
     *
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
     *
     * @param value is the String to be verified
     * @return true if it is possible to convert to a float
     */
    public static float convertFloat(String value) {
        value = value.replace(" ", "");
        return Float.parseFloat(value);
    }

    /**
     * Function that verifies if it is possible to convert the String to a float
     *
     * @param value is the String to be verified
     * @return true if it is possible to convert to a float
     */
    public static double convertDouble(String value) {
        value = value.replace(" ", "");
        return Double.parseDouble(value);
    }

    /**
     * Function that verifies if it is possible to convert the String to an
     * integer
     *
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
     *
     * @param value is the String to be converted
     * @return the int value of the string
     */
    public static int convertInt(String value) {
        value = value.replace(" ", "");
        return Integer.valueOf(value);
    }

    /**
     * Function to convert a String to an Int value, rounding it
     *
     * @param value is the String to be converted
     * @return the rounded int value of the string
     */
    public static float roundToInt(String value) {
        value = value.replace(" ", "");
        return Math.round(Float.parseFloat(value));
    }

    /**
     * Function to compare two floats with an epsilon margin of error
     *
     * @param f1 is the first float
     * @param f2 is the second float
     * @param epsilon is the margin of error for the comparison
     * @return true if floats are equals within the epsilon error margin
     */
    public static boolean FloatEqualTo(double f1, double f2, double epsilon) {
        return Math.abs(f1 - f2) <= epsilon;
    }

    public static boolean FloatSimilar(double f1, double f2, double epsilon) {
        double max = Math.max(f1, f2);
        double min = Math.min(f1, f2);

        return min + Math.abs(max * epsilon) >= max;
    }

    /**
     * Clamp function to clamp the value between min and max
     *
     * @param min is the minimal accepted value
     * @param max is the maximum accepted value
     * @param value is the value to be clamped between min and max
     * @return clamped value
     */
    public static double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Method to convert a string to XMLGregorianCalendar
     *
     * @param time is the desired date in the "yyyy-MM-dd'T'HH:mm:ss"
     * @return the converted XMLGregorian Date
     */
    public static XMLGregorianCalendar stringToXMLGregorianCalendar(String time) {
        try {
            XMLGregorianCalendar result = null;
            Date date;
            SimpleDateFormat simpleDateFormat;
            GregorianCalendar gregorianCalendar;

            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            date = simpleDateFormat.parse(time);
            gregorianCalendar
                    = (GregorianCalendar) GregorianCalendar.getInstance();
            gregorianCalendar.setTime(date);
            result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
//            System.out.println("Date: " + result.toString());
            return result;
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Function that verifies if it is possible to convert the String to Date
     *
     * @param value is the String to be verified
     * @return true if it is possible to convert to Date
     */
    public static boolean tryParseDate(String value) {
        try {
            SimpleDateFormat simpleDateFormat;
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            simpleDateFormat.parse(value);
            return true;
        } catch (ParseException ex) {

        }
        return false;
    }

    /**
     * Convert a Date in the format of a string to double
     *
     * @param d1 is the string to be converted
     * @return the value in Double for the date
     */
    public static double convertStringDateToDouble(String d1) {
        // Try to convert to date format and return how many milliseconds have passed since January 1, 1970, 00:00:00 GMT
        try {
            Date date;
            SimpleDateFormat simpleDateFormat;
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            date = simpleDateFormat.parse(d1);
            return (double) date.getTime();
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Method to return the median of a numeric array
     *
     * @param l is the array
     * @param start
     * @param end
     * @return the median
     */
    public static String median(Object[] l, int start, int end) {
        if (end - start > 1) {
            int middle = (end - start) / 2; //l.length / 2;
            middle = middle + start;
            boolean isString = false;
            double[] v = new double[l.length];
            int i = 0;
            for (Object s : l) {
                if (tryParseFloat((String) s)) {
                    v[i++] = Double.parseDouble((String) s);
                } else {
                    isString = true;
                }

            }
            if (!isString) {
                Arrays.sort(v);
                if ((end - start) % 2 == 0) {
                    double left = v[middle - 1];
                    double right = v[middle];
                    return String.valueOf((left + right) / 2);
                } else {
                    return String.valueOf(v[middle]);
                }
            } else {
                Arrays.sort(l);
                return (String) l[middle];
            }
        } else {
            return (String) l[start];
        }
    }

    /**
     * Retrieve the quartile value from an array
     *
     * @param values THe array of data
     * @param lowerPercent The percent cut off. For the lower quartile use 25,
     * for the upper-quartile use 75
     * @return
     */
    public static String quartile(Object[] values, int lowerPercent) {

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }
        int start, end;
        if (lowerPercent == 1) {
            start = 0;
            end = values.length / 2;
        } else {
            if ((values.length % 2) == 0) {
                start = (values.length / 2);
            } else {
                start = (values.length / 2) + 1;
            }
            end = values.length;
        }

        return median(values, start, end);
//        Arrays.sort(values);
//        // Rank order the values
//        double[] v = new double[values.length];
//        System.arraycopy(values, 0, v, 0, values.length);
//        Arrays.sort(v);
//
//        int n = (int) Math.round(v.length * lowerPercent / 100);
//        
//        return v[n];

    }

    /**
     * Method to remove the infinity numbers from an arrayList
     * @param allNumbers is the arrayList of float
     * @return the same arrayList without the infinity numbers
     */
    public static ArrayList<Float> removeInfinity(ArrayList<Float> allNumbers) {
        ArrayList<Float> normalNumbers = new ArrayList<>();
        for (float number : allNumbers) {
            if ((number != Float.NEGATIVE_INFINITY) && (number != Float.POSITIVE_INFINITY)) {
                normalNumbers.add(number);
            }
        }
        return normalNumbers;
    }

    /**
     * Method to remove outliers from a list
     * @param allNumbers is the arrayList of float
     * @return the same arrayList without the outliers
     */
    public static ArrayList<Float> removeOutLierAnalysis(ArrayList<Float> allNumbers) {
        if (allNumbers.isEmpty()) {
            return null;
        }

        ArrayList<Float> normalNumbers = new ArrayList<>();
        Collections.sort(allNumbers);
//        double mean;
        double q1;
        double q3;
        if (allNumbers.size() % 2 == 0) {
            int position;
//            mean = (allNumbers.get(position) + allNumbers.get(position - 1)) * 0.5;
            position = (int) (allNumbers.size() * 0.25);
            q1 = (allNumbers.get(position) + allNumbers.get(position - 1)) * 0.5;
            position = (int) (allNumbers.size() * 0.75);
            q3 = (allNumbers.get(position) + allNumbers.get(position - 1)) * 0.5;
        } else {
            int position;
//            mean = allNumbers.get(position);
            position = (int) (allNumbers.size() * 0.25);
            q1 = allNumbers.get(position);
            position = (int) (allNumbers.size() * 0.75);
            q3 = allNumbers.get(position);
        }
        double iqr = q3 - q1;
        double lowerThreshold = q1 - iqr * 1.5;
        double upperThreshold = q3 + iqr * 1.5;

        for (float number : allNumbers) {
            if ((lowerThreshold <= number) && (number <= upperThreshold)) {
                normalNumbers.add(number);
            }
        }

        return normalNumbers;
    }
    
    /**
     * Method to compute the standard deviation
     * @param list is the list of float values
     * @return the stdev
     */
    public static double stdev(Double[] list){
        double mean = 0.0;
        mean = mean(list);
        return stdev(list, mean);
    }
    
    /**
     * Method to compute the standard deviation when the mean is already known
     * @param list is the list of float values
     * @param mean is the known mean of the list
     * @return the stdev
     */
    public static double stdev(Double[] list, double mean){
        double num=0.0;
        double numi = 0.0;
        double deno = 0.0;

        for (int i=0; i <list.length; i++){
             numi = Math.pow((list[i] - mean),2);
             num+=numi;
             deno =list.length - 1;  
        }


        double stdevResult = Math.sqrt(num/deno);
        return stdevResult;
    }
    
    /**
     * Computes the standard deviation for the attribute
     *
     * @param vertices the list of vertices
     * @param attribute the attribute in which we want to calculate the standard deviation
     * @return the standard deviation
     */
    public static double std(Collection<Object> vertices, String attribute) {
        ArrayList<Double> values = new ArrayList<>();
        for (Object v1 : vertices) {
            double val = ((Vertex) v1).getAttributeValueFloat(attribute);
            if (!(val != val)) {
                values.add(val);
            }
        }
        Double[] doubleArray = new Double[values.size()];
        doubleArray = Utils.listToDoubleArray(values);
        return stdev(doubleArray);
    }
    
    /**
     * Method to compute the mean of a list
     * @param list is the list
     * @return the mean of the list
     */
    public static double mean(Double[] list) {
        double mean = 0.0;
        double sum = 0.0;
        for (int i=0; i < list.length; i++){
            sum+=list[i];            
        }
        mean = sum / list.length;
        
        return mean;
    }
    
    /**
     * Method to find the minimum value of a list
     * @param list is the list of values
     * @return the minimum value of the list
     */
    public static double minimumValue(Double[] list) {
        double min = Double.POSITIVE_INFINITY;
        for (int i=0; i < list.length; i++){
            min = Math.min(min, list[i]);            
        }
        
        
        return min;
    }
    /**
     * Method to find the maximum value of a list
     * @param list is the list of values
     * @return the maximum value of the list
     */
    public static double maximumValue(Double[] list) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i=0; i < list.length; i++){
            max = Math.max(max, list[i]);            
        }
        return max;
    }
    
    /**
     * Method to convert a list of objects to an arraylist of double
     * @param values is the list of objects to be convertable
     * @return the arraylist of double
     */
    public static Double[] listToDoubleArray(ArrayList<Double> values) {
        Double[] doubleArray = new Double[values.size()];
        int i = 0;
        for (Double f : values) {
            doubleArray[i++] = (f != null ? f : Double.NaN);
        }
        return doubleArray;
    }

    /**
     * Method to clone a graph
     * @param graph The graph to be cloned
     * @return a clone of the graph
     */
    public static DirectedGraph<Object, Edge> copyGraph(DirectedGraph<Object, Edge> graph) {
        DirectedGraph<Object, Edge> clone = new DirectedSparseMultigraph<>();;
        for (Edge e : graph.getEdges()) {
            clone.addEdge(e, e.getSource(), e.getTarget());
        }
        
        return clone;
    }
}
