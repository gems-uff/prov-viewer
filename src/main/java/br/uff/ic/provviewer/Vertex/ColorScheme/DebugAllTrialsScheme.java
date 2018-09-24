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

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Kohwalter
 */
public class DebugAllTrialsScheme extends ColorScheme {
    List<String> correctTrials = new ArrayList<>();
    List<String> pickedTrials = new ArrayList<>();
    
    public DebugAllTrialsScheme(String attribute) {
        super(attribute);
        correctTrials.add("workflow_trial_7.xml");
        correctTrials.add("workflow_trial_11.xml");
        correctTrials.add("workflow_trial_16.xml");
        correctTrials.add("workflow_trial_17.xml");
        correctTrials.add("workflow_trial_18.xml");
        correctTrials.add("workflow_trial_22.xml");
        correctTrials.add("workflow_trial_24.xml");
        correctTrials.add("workflow_trial_25.xml");
        correctTrials.add("workflow_trial_28.xml");
        correctTrials.add("workflow_trial_32.xml");
    }

    public void updatePickedList() {
        if(variables.view.getPickedVertexState() != null) {
            PickedState<Object> picked_state = variables.view.getPickedVertexState();
            if(picked_state.getSelectedObjects().length >= 1) {
                Object picked = picked_state.getSelectedObjects()[0];
                if(picked instanceof Vertex) {
                    String[] pickedVertex_graphs = ((Vertex)picked).getAttributeValues(VariableNames.GraphFile);
                    pickedTrials = new ArrayList<>(Arrays.asList(pickedVertex_graphs));
                }
            }
        }
    }
    @Override
    public Paint Execute(Object v, Variables variables) {
        this.variables = variables;
        updatePickedList();
        PickedState<Object> picked_state = variables.view.getPickedVertexState();
        if(picked_state.getSelectedObjects().length >= 1) {
            String[] graphs = ((Vertex)v).getAttributeValues(VariableNames.GraphFile);
            boolean belongsToPickedTrials = false;
            int n = 0;
            
            for(String s : graphs) {
                if(pickedTrials.contains(s)) {
                    belongsToPickedTrials = true;
                    if(correctTrials.contains(s))
                        n++;
                }   
            }
            
            if(belongsToPickedTrials) {
                return Utils.trafficLight(n, 0, pickedTrials.size(), false);
            } else
                return new Color(200, 200, 200, 0);
        }
        else {
            String[] graphs = ((Vertex)v).getAttributeValues(VariableNames.GraphFile);
            int n = 0;
            for(String s : graphs) {
                if(correctTrials.contains(s))
                    n++;
            }

            return Utils.trafficLight(n, 0, graphs.length, false);
        }
    }
    
}
