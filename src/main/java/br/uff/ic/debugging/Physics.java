/*
 * The MIT License
 *
 * Copyright 2018 Kohwalter.
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
package br.uff.ic.debugging;

import br.uff.ic.provviewer.GUI.GuiButtons;
import br.uff.ic.provviewer.GUI.GuiReadFile;
import br.uff.ic.utility.GraphAttribute;
import static br.uff.ic.provviewer.ProvDebugger.DebugTrials;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Kohwalter
 */
/*
 // Gravity
double g = 9.8F;
// Air density
// Ranges from 0.660 to 1.225: 0.9 - 1.2 with 0.1 interval
double air_density_altitude = 1.0F;
// Ranges from 1.145 - 1.422: 1.15 - 1.45 with 0.1 interval
double air_density_temperature = 1.0F;

// Drag Coeficient A.K.A. wind: 0.3, 0.47, 0.75
double Cd = 1.0F;

// Object's Mass: 0.4, 0.7. 1.0
double m = 1.0F;
// Initial conditions
double X0 = 1.0F;
double Y0 = 0.0F;
double Vx0 = 70.0F;
double Vy0 = 80.0F;
 */
public class Physics {

    int steps = 25; // 1000
    int shots = 20;
    int competitors = 15;
    boolean targetInGround = false;
    int targetBase = 750;
    int targetOffset = 250;
    private String trialName = "t_";
    Map<String, String> failedResults = new HashMap<>();
    Map<String, String> okResults = new HashMap<>();
    Map<String, Object> vertices = new HashMap<>();
    Map<String, Edge> edges = new HashMap<>();
    String previousVertex = "";

    void main() throws IOException {
//        generateFiles(true);
//        evaluate();

        // Single test
        singleTest();
//        generateFiles(true);
    }

    void evaluate() throws IOException {
        // Variables
        double accuracy = 0;
        Map<String, Integer> diffSize = new HashMap<>();
        int countPatchedTrials = 0;
        DirectedGraph<Object, Edge> graph;
        List<String> correctTrials = new ArrayList<>();
        List<String> allTrials = new ArrayList<>();
        String okTrialsPath = "OKTrials.txt";
        String TrialListPath = "TrialList.txt";
        String unifiedGraphPath = "Physics_Unified_Graph.xml";
        String[] oklist = new String[1];
        oklist[0] = "";
        double meanDiff = 0;
        double meanDiffOK = 0;

        String resultsLog = "";

        // Need to load graph
        File summarizedGraphFile = new File(unifiedGraphPath);
        graph = GuiReadFile.getGraph(summarizedGraphFile);

        // Populate the OK Trials list
        readTrialList(okTrialsPath, correctTrials);

        // Now lets read all executed trial
        readTrialList(TrialListPath, allTrials);

        // Now lets debug each trial
        for (String currentTrial : allTrials) {
            if (!correctTrials.contains(currentTrial)) {
                Map<String, List<Vertex>> reasons = DebugTrials(currentTrial, correctTrials, graph, false, false, true);
                // Need to recover the OKTrial used to get the value for the attributes during the patch
                String okTrial = reasons.get("minDiffTrial").get(0).getID();
                int minDiffSize = reasons.get("correct").size();
                // Now lets evaluate the accuracy of the algorithm
                // Lets get the original values
                Map<String, String> patchedGraph = new HashMap<>();
                for (Vertex v : reasons.get("originalVertices")) {
                    if (v.getAttribute(v.getLabel()).getOriginalValues().get(currentTrial) != null) {
                        patchedGraph.put(v.getLabel(), v.getAttribute(v.getLabel()).getOriginalValues().get(currentTrial));
                    } else {
                        patchedGraph.put(v.getLabel(), v.getAttributeValue(v.getLabel()));
                    }
                }
                // Now apply patch
                diffSize.put(currentTrial, reasons.get("correct").size());
                for (Vertex v : reasons.get("correct")) {
                    if (v.getAttribute(v.getLabel()).getOriginalValues().get(currentTrial) != null) {
                        patchedGraph.put(v.getLabel(), v.getAttribute(v.getLabel()).getOriginalValues().get(okTrial));
                    } else {
                        patchedGraph.put(v.getLabel(), v.getAttributeValue(v.getLabel()));
                    }
                }

                // Lets get the variable values
                System.out.println(patchedGraph.get("air_density_altitude"));
                double air_density_altitude = Double.parseDouble(patchedGraph.get("air_density_altitude"));
                double air_density_temperature = Double.parseDouble(patchedGraph.get("air_density_temperature"));
                double Cd = Double.parseDouble(patchedGraph.get("Cd"));
                double m = Double.parseDouble(patchedGraph.get("m"));
                double X0 = Double.parseDouble(patchedGraph.get("X0"));
                double Y0 = Double.parseDouble(patchedGraph.get("Y0"));
                double Vx0 = Double.parseDouble(patchedGraph.get("Vx0"));
                double Vy0 = Double.parseDouble(patchedGraph.get("Vy0"));
                int targetPos = (int) Double.parseDouble(patchedGraph.get("targetPos"));

                // Now we test if the patch works
//            System.out.println("========================");
//            System.out.println("Degugging trial: " + currentTrial);
//            System.out.println("MinDiffOK trial: " + okTrial);
//            System.out.println("DiffSize: " + minDiffSize);
//            System.out.println("air_density_altitude: " + air_density_altitude);
//            System.out.println("air_density_temperature: " + air_density_temperature);
//            System.out.println("Cd: " + Cd);
//            System.out.println("m: " + m);
//            System.out.println("X0: " + X0);
//            System.out.println("Y0: " + Y0);
//            System.out.println("Vx0: " + Vx0);
//            System.out.println("Vy0: " + Vy0);
//            System.out.println("targetPos: " + targetPos);
                resultsLog += "========================" + "\n";
                resultsLog += "Degugging trial: " + currentTrial + "\n";
                resultsLog += "MinDiffOK trial: " + okTrial + "\n";
                resultsLog += "DiffSize: " + minDiffSize + "\n";
                resultsLog += "air_density_altitude: " + air_density_altitude + "\n";
                resultsLog += "air_density_temperature: " + air_density_temperature + "\n";
                resultsLog += "Cd: " + Cd + "\n";
                resultsLog += "m: " + m + "\n";
                resultsLog += "X0: " + X0 + "\n";
                resultsLog += "Y0: " + Y0 + "\n";
                resultsLog += "Vx0: " + Vx0 + "\n";
                resultsLog += "Vy0: " + Vy0 + "\n";
                resultsLog += "targetPos: " + targetPos + "\n";

                boolean reachedTarget = runSimulation(oklist, currentTrial, targetPos, m, air_density_altitude, air_density_temperature, Cd, X0, Y0, Vx0, Vy0);
                if (reachedTarget) {
                    countPatchedTrials++;
                    resultsLog += "Hit target" + "\n";
                    okResults.put(currentTrial, currentTrial + "\t" + okTrial + "\t" + "OK" + "\t" + minDiffSize + "\t" + (9 - minDiffSize));
                    meanDiffOK += (9 - minDiffSize);
                } else {
                    resultsLog += "Missed Target" + "\n";
                    failedResults.put(currentTrial, currentTrial + "\t" + okTrial + "\t " + "FAIL" + "\t" + minDiffSize + "\t" + (9 - minDiffSize));
                }
                meanDiff += (9 - minDiffSize);
            }
        }
        // Now that we debugged all trials, lets compute the algorithm accuracy
        accuracy = (double) (double) countPatchedTrials / ((double) allTrials.size() - (double) correctTrials.size());
        double mean = meanDiff / ((double) allTrials.size() - (double) correctTrials.size());
        double mean2 = meanDiffOK / (double) countPatchedTrials;
        System.out.println("========================");
        System.out.println("Final Results");
        System.out.println("countPatchedTrials: " + countPatchedTrials);
        System.out.println("correctTrials: " + correctTrials.size());
        System.out.println("allTrials: " + allTrials.size());
        System.out.println("accuracy: " + accuracy);
        System.out.println("Mean Not Changed: " + mean);
        System.out.println("Mean Not Changed of only those that worked: " + mean2);

        String finalTally = "";
        finalTally += "countPatchedTrials: " + countPatchedTrials + "\n";
        finalTally += "correctTrials: " + correctTrials.size() + "\n";
        finalTally += "allTrials: " + allTrials.size() + "\n";
        finalTally += "accuracy: " + accuracy + "\n";
        finalTally += "Mean Not Changed: " + mean + "\n";

        writeFileResults(okResults, "ResultsWorked");
        writeFileResults(failedResults, "ResultsFailed");
        writeResults(finalTally, "ResultsTally");
        writeResults(resultsLog, "ResultsLog");
    }

    void readTrialList(String TrialList_file, List<String> allTrials) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(TrialList_file))) {
            String line;
            // For each trial in the file
            while ((line = br.readLine()) != null) {
                // Debug
                allTrials.add(line);
            }
            br.close();
        }
    }

    private void singleTest() {
        String[] oklist = new String[1];
        oklist[0] = "";
        String trial = "test"; 
        int targetPos = 611;
        double m = 0.6353646875652665;
        double air_density_altitude = 1.1230581905722996;
        double air_density_temperature = 1.1834768535914768;
        double Cd = 0.33555423241294413;
        double x0 = 2.831824135384543;
        double y0 = 1.3170337438099498;
        double vx0 = 1004.8383442583655;
        double vy0 = 3.3491982995793554;
//        int targetPos = (int) (Math.random() * 750) + 500;
        generateGraph(0, air_density_altitude, air_density_temperature, Cd, m, x0, y0, vx0, vy0, targetPos, false, false, false);
        runSimulation(oklist, trial, targetPos, m, air_density_altitude, air_density_temperature, Cd, x0, y0, vx0, vy0);
        GuiButtons.ExportGraphXML(vertices.values(), edges.values(), trialName + trial);
    }

    private void generateFiles(boolean writeFiles) {

        String[] oklist = new String[1];
        oklist[0] = "";
        Random rng = new Random();
        // 20 Shots each competitor
        for (int j = 0; j < shots; j++) {
//            int targetPos = (int) (Math.random() * targetBase) + targetOffset;
            int targetPos = -1;
            while (targetPos <= 0) {
                targetPos = (int) (targetBase + targetOffset * rng.nextGaussian());
            }
            // Around 150 competitors
            for (int i = 1; i < competitors + 1; i++) {
                generateSimulation(i + (j * competitors), targetPos, oklist);
            }
        }
        if (writeFiles) {
            writeOKList(oklist);
            writeTrialList(shots * competitors);
        }
    }

    public double generateRandomValue(double mean, double sigma) {
        Random rng = new Random();
        double value = (double) (mean + sigma * rng.nextGaussian());
        return value;
    }

    public void generateSimulation(int trial, int targetPos, String[] oklist) {
        System.out.println("==============");
        System.out.println("Trial #" + trial);
        String trialFile = trialName + trial + ".xml" + System.lineSeparator();
        double air_density_altitude;
        double air_density_temperature;
        double Cd;
        double m;
        double X0;
        double Y0;
        double Vx0;
        double Vy0;
        int r;

        air_density_altitude = generateRandomValue(1.0, 0.1);
        System.out.println("air_density_altitude: " + air_density_altitude);

        air_density_temperature = generateRandomValue(1.3, 0.1);
        System.out.println("air_density_temperature: " + air_density_temperature);

        Cd = generateRandomValue(0.5, 0.1);
        System.out.println("Cd: " + Cd);

        m = generateRandomValue(0.7, 0.1);
        System.out.println("m: " + m);

        X0 = generateRandomValue(1.0, 0.75);
        System.out.println("X0: " + X0);

        Y0 = generateRandomValue(1.5, 0.25);
        System.out.println("Y0: " + Y0);

        Vx0 = generateRandomValue(1000, 50);
        System.out.println("Vx0: " + Vx0);

        Vy0 = generateRandomValue(5, 2);
        System.out.println("Vy0: " + Vy0);

        boolean reachedTarget = runSimulation(oklist, trialFile, targetPos, m, air_density_altitude, air_density_temperature, Cd, X0, Y0, Vx0, Vy0);
        generateGraph(trial, air_density_altitude, air_density_temperature, Cd, m, X0, Y0, Vx0, Vy0, targetPos, reachedTarget, false, true);
    }

    public void generateSimulation2(int trial, int targetPos, String[] oklist) {
        System.out.println("==============");
        System.out.println("Trial #" + trial);
        String trialFile = trialName + trial + ".xml" + System.lineSeparator();
        double air_density_altitude;
        double air_density_temperature;
        double Cd;
        double m;
        double X0;
        double Y0;
        double Vx0;
        double Vy0;
        int r;

        r = (int) (Math.random() * 3) + 1;

        switch (r) {
            case 1:
                air_density_altitude = 0.8;
                break;
            case 2:
                air_density_altitude = 1.0;
                break;
            default:
                air_density_altitude = 1.2;
                break;
        }
        System.out.println("r: " + r + " / air_density_altitude: " + air_density_altitude);

        r = (int) (Math.random() * 3) + 1;
        switch (r) {
            case 1:
                air_density_temperature = 1.15;
                break;
            case 2:
                air_density_temperature = 1.30;
                break;
            default:
                air_density_temperature = 1.45;
                break;
        }
        System.out.println("r: " + r + " / air_density_temperature: " + air_density_temperature);

        r = (int) (Math.random() * 3) + 1;
        switch (r) {
            case 1:
                Cd = 0.3;
                break;
            case 2:
                Cd = 0.47;
                break;
            default:
                Cd = 0.75;
                break;
        }
        System.out.println("r: " + r + " / Cd: " + Cd);

        r = (int) (Math.random() * 3) + 1;
        switch (r) {
            case 1:
                m = 0.4;
                break;
            case 2:
                m = 0.7;
                break;
            default:
                m = 1.0;
                break;
        }
        System.out.println("r: " + r + " / m: " + m);

        r = (int) (Math.random() * 3) + 1;
        switch (r) {
            case 1:
                X0 = 0.0;
                break;
            case 2:
                X0 = 1.0;
                break;
            default:
                X0 = 2.0;
                break;
        }
        System.out.println("r: " + r + " / X0: " + X0);

        r = (int) (Math.random() * 3) + 1;
        switch (r) {
            case 1:
                Y0 = 1.5;
                break;
            case 2:
                Y0 = 1.75;
                break;
            default:
                Y0 = 2.0;
                break;
        }
        System.out.println("r: " + r + " / Y0: " + Y0);

        r = (int) (Math.random() * 3) + 1;
        switch (r) {
            case 1:
                Vx0 = 50;
                break;
            case 2:
                Vx0 = 75;
                break;
            default:
                Vx0 = 100;
                break;
        }
        System.out.println("r: " + r + " / Vx0: " + Vx0);

        r = (int) (Math.random() * 3) + 1;
        switch (r) {
            case 1:
                Vy0 = 50;
                break;
            case 2:
                Vy0 = 75;
                break;
            default:
                Vy0 = 100;
                break;
        }
        System.out.println("r: " + r + " / Vy0: " + Vy0);
        boolean reachedTarget = runSimulation(oklist, trialFile, targetPos, m, air_density_altitude, air_density_temperature, Cd, X0, Y0, Vx0, Vy0);
        generateGraph(trial, air_density_altitude, air_density_temperature, Cd, m, X0, Y0, Vx0, Vy0, targetPos, reachedTarget, false, true);
    }

    private void generateGraph(int trial, double air_density_altitude, double air_density_temperature, double Cd,
            double m, double X0, double Y0, double Vx0, double Vy0, int targetPos, boolean reachedTarget, boolean exportEdges, boolean exportGraph) {

        vertices = new HashMap<>();
        edges = new HashMap<>();
        Vertex e1, e2, e3, e4, e5, e6, e7, e8, e9, e10;
        e1 = new EntityVertex("trial_" + trial + "_air_density_altitude", "air_density_altitude", "");
        e1.addAttribute(new GraphAttribute("air_density_altitude", Double.toString(air_density_altitude), trialName + trial));
        e1.addAttribute(new GraphAttribute("x", "0", trialName + trial));
        e1.addAttribute(new GraphAttribute("y", "0", trialName + trial));
        e1.addAttribute(new GraphAttribute("Timestamp", "0", trialName + trial));
        vertices.put(e1.getID(), e1);
        e2 = new EntityVertex("trial_" + trial + "_air_density_temperature", "air_density_temperature", "");
        e2.addAttribute(new GraphAttribute("air_density_temperature", Double.toString(air_density_temperature), trialName + trial));
        e2.addAttribute(new GraphAttribute("x", "1", trialName + trial));
        e2.addAttribute(new GraphAttribute("y", "0", trialName + trial));
        e2.addAttribute(new GraphAttribute("Timestamp", "1", trialName + trial));
        vertices.put(e2.getID(), e2);
        e3 = new EntityVertex("trial_" + trial + "_Cd", "Cd", "");
        e3.addAttribute(new GraphAttribute("Cd", Double.toString(Cd), trialName + trial));
        e3.addAttribute(new GraphAttribute("x", "2", trialName + trial));
        e3.addAttribute(new GraphAttribute("y", "0", trialName + trial));
        e3.addAttribute(new GraphAttribute("Timestamp", "2", trialName + trial));
        vertices.put(e3.getID(), e3);
        e4 = new EntityVertex("trial_" + trial + "_X0", "X0", "");
        e4.addAttribute(new GraphAttribute("X0", Double.toString(X0), trialName + trial));
        e4.addAttribute(new GraphAttribute("x", "3", trialName + trial));
        e4.addAttribute(new GraphAttribute("y", "0", trialName + trial));
        e4.addAttribute(new GraphAttribute("Timestamp", "3", trialName + trial));
        vertices.put(e4.getID(), e4);
        e5 = new EntityVertex("trial_" + trial + "_Y0", "Y0", "");
        e5.addAttribute(new GraphAttribute("Y0", Double.toString(Y0), trialName + trial));
        e5.addAttribute(new GraphAttribute("x", "4", trialName + trial));
        e5.addAttribute(new GraphAttribute("y", "0", trialName + trial));
        e5.addAttribute(new GraphAttribute("Timestamp", "4", trialName + trial));
        vertices.put(e5.getID(), e5);
        e6 = new EntityVertex("trial_" + trial + "_Vx0", "Vx0", "");
        e6.addAttribute(new GraphAttribute("Vx0", Double.toString(Vx0), trialName + trial));
        e6.addAttribute(new GraphAttribute("x", "5", trialName + trial));
        e6.addAttribute(new GraphAttribute("y", "0", trialName + trial));
        e6.addAttribute(new GraphAttribute("Timestamp", "5", trialName + trial));
        vertices.put(e6.getID(), e6);
        e7 = new EntityVertex("trial_" + trial + "_Vy0", "Vy0", "");
        e7.addAttribute(new GraphAttribute("Vy0", Double.toString(Vy0), trialName + trial));
        e7.addAttribute(new GraphAttribute("x", "6", trialName + trial));
        e7.addAttribute(new GraphAttribute("y", "0", trialName + trial));
        e7.addAttribute(new GraphAttribute("Timestamp", "6", trialName + trial));
        vertices.put(e7.getID(), e7);
        e8 = new EntityVertex("trial_" + trial + "_m", "m", "");
        e8.addAttribute(new GraphAttribute("m", Double.toString(m), trialName + trial));
        e8.addAttribute(new GraphAttribute("x", "7", trialName + trial));
        e8.addAttribute(new GraphAttribute("y", "0", trialName + trial));
        e8.addAttribute(new GraphAttribute("Timestamp", "7", trialName + trial));
        vertices.put(e8.getID(), e8);
        e9 = new EntityVertex("trial_" + trial + "_targetPos", "targetPos", "");
        e9.addAttribute(new GraphAttribute("targetPos", Integer.toString(targetPos), trialName + trial));
        e9.addAttribute(new GraphAttribute("x", Integer.toString(targetPos / 10), trialName + trial));
        e9.addAttribute(new GraphAttribute("y", Double.toString(1.3), trialName + trial));
        e9.addAttribute(new GraphAttribute("Timestamp", "8", trialName + trial));
        vertices.put(e9.getID(), e9);
        
//        e10 = new EntityVertex("trial_" + trial + "_reachedTarget", "reachedTarget", "");
//        e10.addAttribute(new GraphAttribute("reachedTarget", Boolean.toString(reachedTarget), trialName + trial));
//        e10.addAttribute(new GraphAttribute("x", "9", trialName + trial));
//        e10.addAttribute(new GraphAttribute("y", "0", trialName + trial));
//        e10.addAttribute(new GraphAttribute("Timestamp", "9", trialName + trial));
//        vertices.add(e10);

        if(exportEdges) {
            Edge edge;
            edge = new Edge("trial_" + trial + "_e1", e1, e2);
            edges.put(edge.getID(), edge);
            edge = new Edge("trial_" + trial + "_e2", e2, e3);
            edges.put(edge.getID(), edge);
            edge = new Edge("trial_" + trial + "_e3", e3, e4);
            edges.put(edge.getID(), edge);
            edge = new Edge("trial_" + trial + "_e4", e4, e5);
            edges.put(edge.getID(), edge);
            edge = new Edge("trial_" + trial + "_e5", e5, e6);
            edges.put(edge.getID(), edge);
            edge = new Edge("trial_" + trial + "_e6", e6, e7);
            edges.put(edge.getID(), edge);
            edge = new Edge("trial_" + trial + "_e7", e7, e8);
            edges.put(edge.getID(), edge);
//            edge = new Edge("trial_" + trial + "_e8", e8, e9);
//            edges.put(edge.getID(), edge);
            //        edge = new Edge("trial_" + trial + "_e9", e9, e10);
            //        edges.add(edge);
        }
        if(exportGraph)
            GuiButtons.ExportGraphXML(vertices.values(), edges.values(), trialName + trial);

    }

    private void writeOKList(String[] list) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            File logFile = new File("OKTrials.txt");

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(list[0]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    private void writeFileResults(Map<String, String> results, String fileName) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            File logFile = new File(fileName + ".txt");

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            // currentTrial + "\t" + okTrial + "\t" + "OK" + "\t" + minDiffSize + "\t" + (9 - minDiffSize)
            writer.write("Current Trial" + "\t" + "okTrial" + "\t" + "Worked?" + "\t" + "MinDiffSize" + "\t" + "OriginalValues Kept");
            writer.write("\n");
            for (String key : results.keySet()) {
                writer.write(results.get(key));
                writer.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    private void writeResults(String results, String fileName) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            File logFile = new File(fileName + ".txt");

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            // currentTrial + "\t" + okTrial + "\t" + "OK" + "\t" + minDiffSize + "\t" + (9 - minDiffSize)
            writer.write(results);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    private void writeTrialList(int ntrials) {
        String list = "";
        for (int i = 1; i < ntrials + 1; i++) {
            list += trialName + i + ".xml" + System.lineSeparator();
        }
        BufferedWriter writer = null;
        try {
            //create a temporary file
            File logFile = new File("TrialList.txt");

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    private void fireVertex(String trial, double time, double x, double y) {
        Vertex fire = new ActivityVertex("trial_" + trial + "_Fire", "Fire", "");
        fire.addAttribute(new GraphAttribute("x", Double.toString(x), trialName + trial));
        fire.addAttribute(new GraphAttribute("y", Double.toString(y), trialName + trial));
        fire.addAttribute(new GraphAttribute("Timestamp", Double.toString(time), trialName + trial));
        vertices.put(fire.getID(), fire);
        Map<String, Edge> newEdges = new HashMap<>();
        int i = 1;
        for(Object v : vertices.values()) {
            if(!("trial_" + 0 + "_targetPos").equals(((Vertex)v).getID())) {
                Edge influence = new Edge("trial_" + trial + "_fire" + ++i, (Vertex) v, fire);
                newEdges.put(influence.getID(), influence);
            }
        }
        edges.putAll(newEdges);
        previousVertex = fire.getID();
    }
    private void hitTarget(String trial, double time) {
        Edge edge = new Edge("trial_" + trial + "_Hit_" + time, vertices.get(previousVertex), vertices.get("trial_" + 0 + "_targetPos"));
        edges.put(edge.getID(), edge);
    }
    private void addVertex(String trial, double time, double x, double y, double vx, double vy) {
        Vertex v;
        v = new ActivityVertex("trial_" + trial + "_bullet" + "_" + time, "BulletPos", "");
        v.addAttribute(new GraphAttribute("VelocityX", Double.toString(vx), trialName + trial));
        v.addAttribute(new GraphAttribute("VelocityY", Double.toString(vy), trialName + trial));
        v.addAttribute(new GraphAttribute("x", Double.toString(x * 0.1), trialName + trial));
        v.addAttribute(new GraphAttribute("y", Double.toString(y), trialName + trial));
        v.addAttribute(new GraphAttribute("Timestamp", Double.toString(time), trialName + trial));
        vertices.put(v.getID(), v);
        Edge edge = new Edge("trial_" + trial + "_" + time, vertices.get(previousVertex), v);
        edges.put(edge.getID(), edge);
        previousVertex = v.getID();
    }
    public boolean runSimulation(String[] oklist, String trial, int targetPos_, double m_, double air_density_altitude_, double air_density_temperature_, double Cd_, double x0_, double y0_, double vx0_, double vy0_) {
        boolean reachedTarget = false;
        int targetPos = targetPos_;
        // Gravity
        double g = 9.8F;
        // Object's Mass
        double m = m_;
        // Air density
        double rho = air_density_altitude_ * air_density_temperature_; // rho = 1.0F;
        // Drag Coeficient
        double Cd = Cd_;
        // Initial conditions
        double X0 = x0_;
        double Y0 = y0_;
        double Vx0 = vx0_;
        double Vy0 = vy0_;

        // Cross-sectional area of the projectile
        double A = (float) (Math.PI * Math.pow(0.01F, 2.0F));
        double alpha = rho * Cd * A / 2.0F;
        double beta = alpha / m;

        // Time steps
        double t_HIT = 2.0F * Vy0 / g;
        double dt = t_HIT / steps;

        // Air With drag
        List<Double> X_WD = new ArrayList<>();
        List<Double> Y_WD = new ArrayList<>();
        List<Double> Vx_WD = new ArrayList<>();
        List<Double> Vy_WD = new ArrayList<>();
        X_WD.add(X0);
        Y_WD.add(Y0);
        Vx_WD.add(Vx0);
        Vy_WD.add(Vy0);

        double speed = 0;
        fireVertex(trial, 0, X0, Y0);

        int stop = 0;
        boolean createVertex = true;
        for (int i = 1; i < steps + 1; i++) {
            if (stop != 1) {
                speed = Math.pow(Math.pow((double) Vx_WD.get(i - 1), 2.0) + Math.pow((double) Vy_WD.get(i - 1), 2.0), 0.5);

                // First calculate velocity
                double vx = Vx_WD.get(i - 1) * (1.0 - beta * speed * dt);
                double vy = Vy_WD.get(i - 1) + (-g - beta * Vy_WD.get(i - 1) * speed) * dt;
                Vx_WD.add(vx);
                Vy_WD.add(vy);
                double x = X_WD.get(i - 1) + Vx_WD.get(i - 1) * dt;
                double y = Y_WD.get(i - 1) + Vy_WD.get(i - 1) * dt;

                // Now calculate position
                X_WD.add(x);
                Y_WD.add(y);
                if(createVertex)
                    addVertex(trial, i, x, y, vx, vy);
                
                // Stop if hits ground
                if (targetInGround) {
                    createVertex = false;
                    if ((double) Y_WD.get(i) <= 0.0F) {
                        stop = 1;
                        if ((X_WD.get(i).intValue() < targetPos + 10) && (X_WD.get(i).intValue() > targetPos - 10)) {
                            reachedTarget = true;
                            System.out.println("Hit target");
                            oklist[0] += trial;
                        } else {
                            System.out.println("Missed target!");
                        }
                    }
                } else {
                    if ((double) X_WD.get(i) >= targetPos) {
                        createVertex = false;
                        System.out.println(i);
                        stop = 1;
                        if ((Y_WD.get(i) > 0.8) && (Y_WD.get(i) < 1.8)) {
                            reachedTarget = true;
                            System.out.println("Hit target");
                            oklist[0] += trial;
                            hitTarget(trial, i);
                        } else {
                            System.out.println("Missed target!");
                        }
                    }
                }

            }
        }
        // Plot results
//        System.out.println("x" + (X_WD.size() -2) + ": " + X_WD.get(X_WD.size() -2 ) + " / y: " + Y_WD.get(Y_WD.size() -2 ));
        System.out.println("x" + (X_WD.size() - 1) + ": " + X_WD.get(X_WD.size() - 1) + " / y: " + Y_WD.get(Y_WD.size() - 1));
        System.out.println("Hit target? " + reachedTarget + " / targetX: " + targetPos);
        return reachedTarget;
    }
    
}
