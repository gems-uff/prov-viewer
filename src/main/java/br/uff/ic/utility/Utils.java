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

import br.uff.ic.utility.IO.XMLWriter;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.GraphVertex;
import br.uff.ic.utility.graph.Vertex;
import br.uff.ic.utility.graphgenerator.NoiseGraph;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
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
     * Method to export the graph in an xml format
     *
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
                Float.parseFloat(v);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
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
        value = value.replace(",", ".");
        String v = new BigDecimal(value.trim()).toPlainString();
        return Float.valueOf(v);
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
        return Math.round(convertFloat(value));
    }

    /**
     * Function to compare two floats with an epsilon margin of error
     *
     * @param f1 is the first float
     * @param f2 is the second float
     * @param epsilon is the margin of error for the comparison
     * @return true if floats are equals within the epsilon error margin
     */
    public static boolean FloatEqualTo(float f1, float f2, float epsilon) {
        return Math.abs(f1 - f2) <= epsilon;
    }

    public static boolean DoubleEqualTo(double f1, double f2, double epsilon) {
        return Math.abs(f1 - f2) <= epsilon;
    }

    public static boolean FloatSimilar(float f1, float f2, float epsilon) {
        float max = Math.max(f1, f2);
        float min = Math.min(f1, f2);

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
    public static float clamp(float min, float max, float value) {
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
     * Convert a Date in the format of a string to float
     *
     * @param d1 is the string to be converted
     * @return the value in Float for the date
     */
    public static double convertStringDateToFloat(String d1) {
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
            float[] v = new float[l.length];
            int i = 0;
            for (Object s : l) {
                if (tryParseFloat((String) s)) {
                    v[i++] = convertFloat((String) s);
                } else {
                    isString = true;
                }

            }
            if (!isString) {
                Arrays.sort(v);
                if ((end - start) % 2 == 0) {
                    float left = v[middle - 1];
                    float right = v[middle];
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
     *
     * @param allNumbers is the arrayList of float
     * @return the same arrayList without the outliers
     */
    public static ArrayList<Float> removeOutLierAnalysis(ArrayList<Float> allNumbers, String att) {
        if (allNumbers.isEmpty()) {
            return null;
        }
        ArrayList<Float> normalNumbers = new ArrayList<>();

        ThresholdValues thresholds = calculateOutliers(allNumbers, att);

        for (float number : allNumbers) {
            if ((thresholds.lowerThreshold <= number) && (number <= thresholds.upperThreshold)) {
                normalNumbers.add(number);
            }
        }

        return normalNumbers;
    }
    
    /**
     * Method to calculate the upper and lower outliers thresholds from a sequence of numbers
     * @param allNumbers contains the numbers
     * @return the lower and upper thresholds for outlier detection
     */
    public static ThresholdValues calculateOutliers(ArrayList<Float> allNumbers, String att) {
        Collections.sort(allNumbers);
        float q1;
        float q3;
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
        float iqr = q3 - q1;
        float lowerThreshold = q1 - iqr * 1.5f;
        float upperThreshold = q3 + iqr * 1.5f;
        return new ThresholdValues(att, lowerThreshold, upperThreshold);
    }

    /**
     * Method to compute the standard deviation
     *
     * @param list is the list of float values
     * @return the stdev
     */
    public static float stdev(Float[] list) {
        float mean = 0.0F;
        mean = mean(list);
        return stdev(list, mean);
    }

    /**
     * Method to compute the standard deviation when the mean is already known
     *
     * @param list is the list of float values
     * @param mean is the known mean of the list
     * @return the stdev
     */
    public static float stdev(Float[] list, float mean) {
        float num = 0.0f;
        float numi = 0.0f;
        float deno = 0.0f;

        for (int i = 0; i < list.length; i++) {
            numi = (float) Math.pow((list[i] - mean), 2);
            num += numi;
            deno = list.length - 1;
        }

        float stdevResult = (float) Math.sqrt(num / deno);

//        float v = (float) ((int) stdevResult * 10000) * 0.0001f;
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
    public static float std(Collection<Object> vertices, String attribute) {
        ArrayList<Float> values = new ArrayList<>();
        for (Object v1 : vertices) {
            float val = ((Vertex) v1).getAttributeValueFloat(attribute);
                if (!(val != val)) {
                    values.add(val);
                }
            }
        Float[] floatArray = new Float[values.size()];
        floatArray = Utils.listToFloatArray(values);
        return stdev(floatArray);
    }

    /**
     * Method to compute the mean of a list
     *
     * @param list is the list
     * @return the mean of the list
     */
    public static float mean(Float[] list) {
        float mean = 0.0F;
        float sum = 0.0F;
        for (int i = 0; i < list.length; i++) {
            sum += list[i];
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
    public static float minimumValue(Float[] list) {
        float min = Float.POSITIVE_INFINITY;
        for (int i = 0; i < list.length; i++) {
            min = Math.min(min, list[i]);
        }

        return min;
    }

    /**
     * Method to find the maximum value of a list
     *
     * @param list is the list of values
     * @return the maximum value of the list
     */
    public static float maximumValue(Float[] list) {
        float max = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < list.length; i++) {
            max = Math.max(max, list[i]);
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
        ArrayList<Float> derivateValues = new ArrayList<>();
        double max = Double.NEGATIVE_INFINITY;
        for (Object node : nodes) {
            if (!((Vertex) node).getAttributeValue(attribute).contentEquals("Unknown")) {
                max = Math.max(max, ((Vertex) node).getAttributeValueFloat(attribute));
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
//    public static Double[] listToDoubleArray(ArrayList<Double> values) {
//        Double[] doubleArray = new Double[values.size()];
//        int i = 0;
//        for (Double f : values) {
//            doubleArray[i++] = (f != null ? f : Double.NaN);
//        }
//        return doubleArray;
//    }
    /**
     * Method to convert a list of objects to an arraylist of double
     *
     * @param values is the list of objects to be convertable
     * @return the arraylist of double
     */
    public static Float[] listToFloatArray(ArrayList<Float> values) {
        Float[] floatArray = new Float[values.size()];
        int i = 0;
        for (Float f : values) {
            floatArray[i++] = (f != null ? f : Float.NaN);
        }
        return floatArray;
    }

    /**
     * Method to clone a graph
     *
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
//                time *= 1000;
                break;
            case "milliseconds":
                t = TimeUnit.MILLISECONDS;
//                time *= 1000;
                break;
            case "seconds":
                t = TimeUnit.SECONDS;
//                time *= 1000;
                break;
            case "minutes":
                t = TimeUnit.MINUTES;
//                time *= 60;
                break;
            case "hours":
                t = TimeUnit.HOURS;
//                time *= 60;
                break;
            case "days":
                t = TimeUnit.DAYS;
//                time *= 24;
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
     * @deprecated 
     * @param graph
     * @param graphFileName 
     */
    public static void updateVertexIDs(DirectedGraph<Object, Edge> graph, String graphFileName) {
        // Instead of changing ID, make an attribute with value as the graph file name
        for (Object v : graph.getVertices()) {
            if(!((Vertex)v).getID().contains(graphFileName)) {
                ((Vertex)v).setID(graphFileName + "_" + ((Vertex)v).getID());
            }
            GraphAttribute att = ((Vertex)v).getAttribute("GraphFile");
            if(att == null) {
                att = new GraphAttribute("GraphFile", graphFileName);
                ((Vertex)v).addAttribute(att);
            }
            else if(!att.getValue().contains(graphFileName)) {
                att.updateAttribute(graphFileName);
                ((Vertex)v).addAttribute(att);
            }
        }
    }
    
    /**
     * @deprecated 
     * @param graph
     * @param graphFileName 
     */
    public static void updateEdgeIDs(DirectedGraph<Object, Edge> graph, String graphFileName) {
        for (Edge e : graph.getEdges()) {
            e.setID(graphFileName + "_" + e.getID());
            GraphAttribute att = new GraphAttribute("GraphFile", graphFileName);
            e.addAttribute(att);
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
//                if (!(c1 instanceof Graph) && !(c2 instanceof Graph)) {
                    double c1t = ((Vertex) c1).getTime();
                    double c2t = ((Vertex) c2).getTime();
                    if (c1t != c2t) {
                        return Double.compare(c1t, c2t);
                    } else {
                        return ((Vertex) c2).getNodeType().compareTo(((Vertex) c1).getNodeType());
                    }
                    //TODO make agent lose priority to appear after the activity
//                } else {
//                    return 0;
//                }
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
//                if (!(c1 instanceof Graph) && !(c2 instanceof Graph)) {
                    double c1t = 0;
                    double c2t = 0;
                    if(isItTime(attribute)) {
                        c1t = ((Vertex) c1).getTime();
                        c2t = ((Vertex) c2).getTime();
                    } else if(!tryParseFloat(((Vertex) c1).getAttributeValue(attribute))){
                        String c1v = ((Vertex) c1).getAttributeValue(attribute);
                        String c2v = ((Vertex) c2).getAttributeValue(attribute);
                        return c1v.compareTo(c2v);
                    } else {
                        c1t = ((Vertex) c1).getAttributeValueFloat(attribute);
                        c2t = ((Vertex) c2).getAttributeValueFloat(attribute);
                    }
                    if (c1t != c2t) {
                        return Double.compare(c1t, c2t);
                    } else {
                        return ((Vertex) c2).getNodeType().compareTo(((Vertex) c1).getNodeType());
                    }
                    //TODO make agent lose priority to appear after the activity
//                } else {
//                    return 0;
//                }
            }
        };
        return comparator;
    }
    
    /**
     * Method that return a grayscale color from the grayscale gradient
     * @param value is the current value that we want the color
     * @param max is the maximum possible value that the previous value can assume
     * @return the gray color corresponding to the value, which is based on value/max
     */
    public static Color getGrayscaleColor(float value, float max)
    {
        if(max == 0)
            max = 1;
        float gray = value / max * 191;
        int rgbNum = 255 - (int) gray;
        return new Color (rgbNum,rgbNum,rgbNum);
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

}
