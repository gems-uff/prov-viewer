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

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.AttValueColor;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Kohwalter
 */
public class VertexColorScheme extends ColorScheme {
    public List<AttValueColor> activityVC = new ArrayList<>();
    public List<AttValueColor> entityVC = new ArrayList<>();
    public List<AttValueColor> agentVC = new ArrayList<>();
    public List<AttValueColor> automaticActivityVC = new ArrayList<>();
    public List<AttValueColor> automaticEntityVC = new ArrayList<>();
    public List<AttValueColor> automaticAgentVC = new ArrayList<>();
    private boolean isAutomatic = false;

    public VertexColorScheme(String filterName, List<AttValueColor> aVC, List<AttValueColor> eVC, List<AttValueColor> agVC, boolean isAutomatic) {
        super(filterName);
        activityVC.addAll(aVC);
        entityVC.addAll(eVC);
        agentVC.addAll(agVC);
        this.isAutomatic = isAutomatic;
    }

    public VertexColorScheme(boolean isZeroWhite, boolean isInverted, String attribute, String empty, String g, String y, boolean l) {
        super(isZeroWhite, isInverted, attribute, empty, g, y, l);
    }
    
    private void automaticInitialization(List<AttValueColor> list, String attribute, int i){
        Collection<String> values = variables.config.DetectAllPossibleValuesFromAttribute(variables.graph.getVertices(), attribute);
        for (String value : values) {
            AttValueColor avc = new AttValueColor();
            avc.name = attribute;
            avc.value = value;
            avc.color = Utils.getColor(++i);
            list.add(avc);
        }
    }
    
    private Paint determineColor(Object v, List<AttValueColor> activity, List<AttValueColor> entity, List<AttValueColor> agent) {
        if (v instanceof ActivityVertex) {
            return getDefaultColor(activity, v);
        }
        else if (v instanceof EntityVertex) {
            return getDefaultColor(entity, v);
        }
        else
            return getDefaultColor(agent, v);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        this.variables = variables;
        if(isAutomatic) {
            if(!isInitialized){
                // Need to automatically populate the lists
                int i = 0;
                for(AttValueColor attname : activityVC) {
                    automaticInitialization(automaticActivityVC, attname.name, i);
                }
                for(AttValueColor attname : entityVC) {
                    automaticInitialization(automaticEntityVC, attname.name, i);
                }
                for(AttValueColor attname : agentVC) {
                    automaticInitialization(automaticAgentVC, attname.name, i);
                }
                isInitialized = true;
            }
            return determineColor(v, automaticActivityVC, automaticEntityVC, automaticAgentVC);
        }
        else
            return determineColor(v, activityVC, entityVC, agentVC);
    }
    
    public Paint getDefaultColor(List<AttValueColor> avc, Object v) {
        for (int i = 0; i < avc.size(); i++) {
            if (((Vertex)v).getAttributeValue(avc.get(i).name).equalsIgnoreCase(avc.get(i).value)) {
                return avc.get(i).color;
            }
        }
        return ((Vertex) v).getColor();
    }
}
