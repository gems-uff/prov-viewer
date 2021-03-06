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

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.IO.XMLWriter;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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

    Color[] colors = new Color[17];

    public static Color getColor(int i) {
        int size = 25; // Array max position + 1
        int index = i % size;
        Color[] colors = new Color[size];
        colors[0] = new Color(0, 0, 0); // Black
        colors[1] = new Color(0, 0, 255); // Blue
        colors[2] = new Color(153, 153, 0); // Yellow-Mostard
        colors[3] = new Color(0, 255, 255); // Cyan/Aqua
        colors[4] = new Color(255, 0, 255); // Magenta
        colors[5] = new Color(128, 128, 128); // Gray
        colors[6] = new Color(128, 0, 0); // Brown / Maroon
        colors[7] = new Color(128, 128, 0); // olive
        colors[8] = new Color(210, 105, 30); // Chocolate
        colors[9] = new Color(0, 0, 128); // Navy
        colors[10] = new Color(255, 128, 128); // Pink
        colors[11] = new Color(255, 128, 0); // Orange
        colors[12] = new Color(192, 192, 192); // Silver / Light-Gray
        colors[13] = new Color(70, 130, 180); // Steel blue
        colors[14] = new Color(184, 134, 11); // Dark golden rod
        // Similar colors...
        colors[15] = new Color(0, 128, 128); // Teal
        colors[16] = new Color(0, 128, 0); // Green
        colors[17] = new Color(255, 0, 0); // Red
        colors[18] = new Color(0, 255, 0); // Lime/Green
        colors[19] = new Color(245, 245, 220); // Beige
        colors[20] = new Color(128, 0, 128); // Purple
        colors[21] = new Color(218,112,214); // Orchid
        colors[22] = new Color(210,105,30); // Chocolate
        colors[23] = new Color(240,255,255); // Azure
        colors[24] = new Color(255,20,147); // Deep-Pink

        return colors[index];
    }

    /**
     * Method to check if it is possible to parse the value to double
     *
     * @param value desired to be parsed to double
     * @return boolean
     */
    public static boolean tryParseDouble(String value) {
        value = value.replace(" ", "");
        value = value.replace(",", ".");
        if (value.isEmpty()) {
            return false;
        } else if (value == null) {
            return false;
        } else if (value.equalsIgnoreCase("")) {
            return false;
        } else if (value.equalsIgnoreCase("NaN")) {
            return false;
        } else {
            try {
                String v = new BigDecimal(value).toPlainString();
                Double.parseDouble(v);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }

    }

    /**
     * Function that verifies if it is possible to convert the String to a double
     *
     * @param value is the String to be verified
     * @return true if it is possible to convert to a double
     */
    public static double convertStringToDouble(String value) {
        value = value.replace(" ", "");
        value = value.replace(",", ".");
        String v;
        if(value.contains("Infinity")) {
            return Double.POSITIVE_INFINITY;
        } else {
            v = new BigDecimal(value.trim()).toPlainString();
            return Double.valueOf(v);
        }
        
        
    }

    /**
     * Function that verifies if it is possible to convert the String to a double
     *
     * @param value is the String to be verified
     * @return true if it is possible to convert to a double
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
    public static double roundToInt(String value) {
        value = value.replace(" ", "");
        return Math.round(convertStringToDouble(value));
    }

    /**
     * Function to compare two doubles with an epsilon margin of error
     *
     * @param f1 is the first double
     * @param f2 is the second double
     * @param epsilon is the margin of error for the comparison
     * @return true if doubles are equals within the epsilon error margin
     */
//    public static boolean DoubleEqualTo(double f1, double f2, double epsilon) {
//        return Math.abs(f1 - f2) <= epsilon;
//    }

    public static boolean DoubleEqualTo(double f1, double f2, double epsilon) {
        return Math.abs(f1 - f2) <= epsilon;
    }

    public static boolean DoubleSimilar(double f1, double f2, double epsilon) {
        double max = Math.max(f1, f2);
        double min = Math.min(f1, f2);

        return min + Math.abs(max * epsilon) >= max;
    }
    
    public static boolean DoubleLesserThan(double f1, double f2, double epsilon) {
        if (Double.compare(f1, f2) < 0)
            return true;
        else
            return false;
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
            XMLGregorianCalendar result;
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
//        System.out.println("tryParseDate: " + value);
        if (value.isEmpty()) {
            return false;
        } else if (value == null) {
            return false;
        } else if (value.equalsIgnoreCase("")) {
            return false;
        } else if (value.equalsIgnoreCase("NaN")) {
            return false;
        }
        try {
            SimpleDateFormat simpleDateFormat;
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            simpleDateFormat.parse(value);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    /**
     * Convert a Date in the format of a string to double
     *
     * @param d1 is the string to be converted
     * @return the value in Double for the date
     */
    public static long convertStringDateToLong(String d1) {
        // Try to convert to date format and return how many milliseconds have passed since January 1, 1970, 00:00:00 GMT
        try {
            Date date;
            SimpleDateFormat simpleDateFormat;
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            date = simpleDateFormat.parse(d1);
            return date.getTime();
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
                if (tryParseDouble((String) s)) {
                    v[i++] = convertStringToDouble((String) s);
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
    }

    /**
     * Method to remove the infinity numbers from an arrayList
     *
     * @param allNumbers is the arrayList of double
     * @return the same arrayList without the infinity numbers
     */
    public static ArrayList<Double> removeInfinity(ArrayList<Double> allNumbers) {
        ArrayList<Double> normalNumbers = new ArrayList<>();
        for (double number : allNumbers) {
            if ((number != Double.NEGATIVE_INFINITY) && (number != Double.POSITIVE_INFINITY)) {
                normalNumbers.add(number);
            }
        }
        return normalNumbers;
    }

    /**
     * Method to remove outliers from a list
     *
     * @param allNumbers is the arrayList of double
     * @param att
     * @return the same arrayList without the outliers
     */
    public static ArrayList<Double> removeOutLierAnalysis(ArrayList<Double> allNumbers, String att) {
        if (allNumbers.isEmpty()) {
            return null;
        }
        ArrayList<Double> normalNumbers = new ArrayList<>();

        ThresholdValues thresholds = calculateOutliers(allNumbers, att);

        for (double number : allNumbers) {
            if ((thresholds.lowerThreshold <= number) && (number <= thresholds.upperThreshold)) {
                normalNumbers.add(number);
            }
        }

        return normalNumbers;
    }
    
    /**
     * Method to calculate the upper and lower outliers thresholds from a sequence of numbers
     * @param allNumbers contains the numbers
     * @param att
     * @return the lower and upper thresholds for outlier detection
     */
    public static ThresholdValues calculateOutliers(ArrayList<Double> allNumbers, String att) {
        Collections.sort(allNumbers);
        double q1;
        double q3;
        if (allNumbers.size() % 2 == 0) {
            int position;
//            mean = (allNumbers.get(position) + allNumbers.get(position - 1)) * 0.5;
            position = (int) (allNumbers.size() * 0.25);
            q1 = (allNumbers.get(position) + allNumbers.get(position - 1)) * 0.5f;
            position = (int) (allNumbers.size() * 0.75);
            q3 = (allNumbers.get(position) + allNumbers.get(position - 1)) * 0.5f;
        } else {
            int position;
//            mean = allNumbers.get(position);
            position = (int) (allNumbers.size() * 0.25);
            q1 = allNumbers.get(position);
            position = (int) (allNumbers.size() * 0.75);
            q3 = allNumbers.get(position);
        }
        double iqr = q3 - q1;
        double lowerThreshold = q1 - iqr * 1.5f;
        double upperThreshold = q3 + iqr * 1.5f;
        return new ThresholdValues(att, lowerThreshold, upperThreshold);
    }

    /**
     * Method to compute the standard deviation
     *
     * @param list is the list of double values
     * @return the stdev
     */
    public static double stdev(Double[] list) {
        double mean;
        mean = mean(list);
        return stdev(list, mean);
    }

    /**
     * Method to compute the standard deviation when the mean is already known
     *
     * @param list is the list of double values
     * @param mean is the known mean of the list
     * @return the stdev
     */
    public static double stdev(Double[] list, double mean) {
        double num = 0.0f;
        double numi;
        double deno = 0.0f;

        for (Double list1 : list) {
            numi = (double) Math.pow(list1 - mean, 2);
            num += numi;
            deno = list.length - 1;
        }

        double stdevResult = (double) Math.sqrt(num / deno);

//        double v = (double) ((int) stdevResult * 10000) * 0.0001f;
        return stdevResult;
    }

    /**
     * Computes the standard deviation for the attribute
     *
     * @param vertices the list of vertices
     * @param attribute the attribute in which we want to calculate the standard
     * deviation
     * @return the standard deviation
     */
    public static double std(Collection<Object> vertices, String attribute) {
        ArrayList<Double> values = new ArrayList<>();
        for (Object v1 : vertices) {
            double val = ((Vertex) v1).getAttributeValueDouble(attribute);
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
     *
     * @param list is the list
     * @return the mean of the list
     */
    public static double mean(Double[] list) {
        double mean;
        double sum = 0.0F;
        for (Double list1 : list) {
            sum += list1;
        }
        mean = sum / list.length;

        return mean;
    }

    /**
     * Method to find the minimum value of a list
     *
     * @param list is the list of values
     * @return the minimum value of the list
     */
    public static double minimumValue(Double[] list) {
        double min = Double.POSITIVE_INFINITY;
        for (Double list1 : list) {
            min = Math.min(min, list1);
        }

        return min;
    }

    /**
     * Method to find the maximum value of a list
     *
     * @param list is the list of values
     * @return the maximum value of the list
     */
    public static double maximumValue(Double[] list) {
        double max = Double.NEGATIVE_INFINITY;
        for (Double list1 : list) {
            max = Math.max(max, list1);
        }
        return max;
    }
    
    /**
     * Method to find the maximum value in the graph from an attribute
     * @param nodes is the list of vertices
     * @param attribute is the attribute that we want to find the maximum value
     * @return the maximum value of attribute
     */
    public static double findMaximumAttributeValue(Collection<Object> nodes, String attribute) {
        double max = Double.NEGATIVE_INFINITY;
        for (Object node : nodes) {
            if (!((Vertex) node).getAttributeValue(attribute).contentEquals("Unknown")) {
                max = Math.max(max, ((Vertex) node).getAttributeValueDouble(attribute));
            }
        }
        return max;
    }

    /**
     * Method to convert a list of objects to an arraylist of double
     *
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
     *
     * @param graph The graph to be cloned
     * @return a clone of the graph
     */
    public static DirectedGraph<Object, Edge> copyGraph(DirectedGraph<Object, Edge> graph) {
        DirectedGraph<Object, Edge> clone = new DirectedSparseMultigraph<>();
        for (Edge e : graph.getEdges()) {
            clone.addEdge(e, e.getSource(), e.getTarget());
        }

        return clone;
    }

    /**
     * Function to convert the timestamp to different time scales
     *
     * @param timeFormat is the original timestamp scale used in the graph XML
     * @param time is the time we want to convert
     * @param timeScale is the current timescale being used
     * @return the converted value
     */
    public static long convertTime(String timeFormat, double time, String timeScale) {
        TimeUnit t;
        if(time == -1)
            return (long) time;
        // Convert the number to a lower scale being used to avoid losing the decimals
        switch (timeFormat) {
            case "nanoseconds":
                t = TimeUnit.NANOSECONDS;
                break;
            case "microseconds":
                t = TimeUnit.MICROSECONDS;
                break;
            case "milliseconds":
                t = TimeUnit.MILLISECONDS;
                break;
            case "seconds":
                t = TimeUnit.SECONDS;
                break;
            case "minutes":
                t = TimeUnit.MINUTES;
                break;
            case "hours":
                t = TimeUnit.HOURS;
                break;
            case "days":
                t = TimeUnit.DAYS;
                break;
            case "weeks":
                t = TimeUnit.DAYS;
                time = time / 7;
                break;
            default:
                t = TimeUnit.NANOSECONDS;
                break;
        }
        switch (timeScale) {
            case "nanoseconds":
                return t.toNanos((long) time);
            case "microseconds":
                return t.toMicros((long) time);
            case "milliseconds":
                return t.toMillis((long) time);
            case "seconds":
                return t.toSeconds((long) time);
            case "minutes":
                return t.toMinutes((long) time);
            case "hours":
                return t.toHours((long) time);
            case "days":
                return t.toDays((long) time);
            case "weeks":
                return (int) t.toDays((long) time) / 7;
            default:
                return (long) time;
        }
    }
    
        /**
     * Method to normalize vertex's timestamps to start from 0
     * @param graph
     * @param overwriteTime
     */
    public static void NormalizeTime(DirectedGraph<Object, Edge> graph, boolean overwriteTime) {
        Collection<Object> vertices = graph.getVertices();
        double minTime = Double.POSITIVE_INFINITY;
        for (Object v : vertices) {
            if (((Vertex) v).getTime() != -1) {
                minTime = Math.min(minTime, ((Vertex) v).getTime());
            }
        }
        // Normalize time
        for (Object v : vertices) {
            if (((Vertex) v).getTime() >= 0) {
                double normalized = ((Vertex) v).getTime() - minTime;
                ((Vertex) v).setNormalizedTime(normalized);
                if (overwriteTime && minTime != 0) {
                    ((Vertex) v).setTime(Double.toString(normalized));
                }
            } else {
                ((Vertex) v).setNormalizedTime(-1);
            }
        }
    }
    
    /**
     * Method to detect all values for the ATTRIBUTE that appears in the graph
     * @param vertices
     * @param attribute
     * @return 
     */
    public static Collection<String> DetectAllPossibleValuesFromAttribute(Collection<Object> vertices, String attribute) {
        Map<String, String> attributeList = new HashMap<>();
        for (Object v : vertices) {
            String value = ((Vertex) v).getAttributeValue(attribute);
            if(!value.contains("Unknown")) {
                String[] values = value.split(", ");
                for(String s : values) {
                    attributeList.put(s, s);
                }
            }
        }
        return attributeList.values();
    }
    
    /**
     * Method that determines if the provided attribute is a timestamp/date/time type of attribute
     * @param attribute the attribute we want to check
     * @return TRUE if it is Time/Timestamp/Date or FALSE if not
     */
    public static boolean isItTime(String attribute) {
        if(attribute.equalsIgnoreCase("Time"))         {
            return true;
        }
        if(attribute.equalsIgnoreCase("Timestamp")) {
            return true;
        }
        if(attribute.equalsIgnoreCase("Date")) {
            return true;
        }
        return false;
    }
    
    /**
     * Method to return a comparator that compares two vertices based on their timestamps
     * @return the comparator
     */
    public static Comparator getVertexTimeComparator() {
        Comparator comparator = new Comparator<Object>() {
            @Override
            public int compare(Object c1, Object c2) {
                    double c1t = ((Vertex) c1).getTime();
                    double c2t = ((Vertex) c2).getTime();
                    if (c1t != c2t) {
                        return Double.compare(c1t, c2t);
                    } else {
                        return ((Vertex) c2).getNodeType().compareTo(((Vertex) c1).getNodeType());
                    }
                    //TODO make agent lose priority to appear after the activity
            }
        };
        return comparator;
    }
    
    /**
     * Generic comparator
     * @param attribute Is the attribute that we want to sort
     * @return the comparator for the given attribute
     */
    public static Comparator getVertexAttributeComparator(final String attribute) {
        Comparator comparator = new Comparator<Object>() {
            @Override
            public int compare(Object c1, Object c2) {
                    long c1t = 0;
                    long c2t = 0;
                    if(!tryParseDouble(((Vertex) c1).getAttributeValue(attribute))){
                        String c1v = ((Vertex) c1).getAttributeValue(attribute);
                        String c2v = ((Vertex) c2).getAttributeValue(attribute);
                        int result = c1v.compareTo(c2v);
                        if(result == 0) {
                            return untieVertexAttributeComparator(c1, c2);
                        }
                        else
                            return result;
                    } else {
                        c1t = (long) (((Vertex) c1).getAttributeValueDouble(attribute) * 1000);
                        c2t = (long) (((Vertex) c2).getAttributeValueDouble(attribute)* 1000);
                    }
                    if (c1t != c2t) {
                        return Long.compare(c1t, c2t);
                    } else {
                        return untieVertexAttributeComparator(c1, c2);
//                        return ((Vertex) c1).getNodeType().compareTo(((Vertex) c2).getNodeType());
                    }
            }
        };
        return comparator;
    }
    
    private static int untieVertexAttributeComparator(Object c1, Object c2) {
        if((((Vertex) c1).getNodeType() != null) && (((Vertex) c2).getNodeType() != null)){
            if((((Vertex) c1).getNodeType().equalsIgnoreCase("Agent")) && !(((Vertex) c2).getNodeType().equalsIgnoreCase("Agent")))
                return -1;
            else if((((Vertex) c2).getNodeType().equalsIgnoreCase("Agent")) && !(((Vertex) c1).getNodeType().equalsIgnoreCase("Agent")))
                return 1;
            else if((((Vertex) c1).getNodeType().equalsIgnoreCase("Activity")) && !(((Vertex) c2).getNodeType().equalsIgnoreCase("Activity")))
                return -1;
            else if((((Vertex) c2).getNodeType().equalsIgnoreCase("Activity")) && !(((Vertex) c1).getNodeType().equalsIgnoreCase("Activity")))
                return 1;
            else if((((Vertex) c1).getNodeType().equalsIgnoreCase("Entity")) && !(((Vertex) c2).getNodeType().equalsIgnoreCase("Entity")))
                return 1;
            else if((((Vertex) c2).getNodeType().equalsIgnoreCase("Entity")) && !(((Vertex) c1).getNodeType().equalsIgnoreCase("Entity")))
                return -1;
            else
                return 0;
        }
        else
            return 0;
    }
    
    /**
     * Method to determine if the string has a minus sign in the beginning
     * @param s is the string that we want to know if it has a minus sign
     * @return TRUE if it has a minus sign and FALSE if it does not have it
     */
    public static boolean getMinusSign(String s) {
        String sign = s.substring(0, 1);
        boolean isReverse = false;
        
        if("-".equals(sign)) {
            isReverse = true;
        }
        return isReverse;
    }
    
    /**
     * Method to remove the minus sign in the beginning of the string (if it has it)
     * @param s the string that we want to remove the minus sign
     * @return the string without the minus sign
     */
    public static String removeMinusSign(String s) {
        String sign = s.substring(0, 1);
        if("-".equals(sign)) {
            return s.substring(1);
        }
        return s;
    }
    
    
    /**
     * Method to decide if the current edge should be highlighted or not
     * @param e the edge we are evaluating
     * @param variables location that has the view variable so we can select the vertices and edges that were marked by the user
     * @return true if the edge should be highlighted and false if not
     */
    public static boolean edgeHighlighted(Edge e, Variables variables){
        PickedState<Object> picked_state = variables.view.getPickedVertexState();
        PickedState<Edge> edge_picked_state = variables.view.getPickedEdgeState();
        if(!picked_state.getPicked().isEmpty() || !edge_picked_state.getPicked().isEmpty()) {
            if (!picked_state.getPicked().isEmpty()) {
                for (Object v : picked_state.getPicked()) {
                    if (e.getSource().equals(v)) {
                        return true;
                    } else if (e.getTarget().equals(v)) {
                        return true;
                    }
                }
            }
            if (!edge_picked_state.getPicked().isEmpty()) {
                for (Object v : edge_picked_state.getPicked()) {
                    if (e.equals(v)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
}
