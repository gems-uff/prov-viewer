/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Collapser;
import br.uff.ic.Prov_Viewer.Edge;
import br.uff.ic.Prov_Viewer.Variables;

/**
 * Subclass of Collapser
 * @author Kohwalter
 */
public class SDM_Collapser extends Collapser {

    private SDM_Edge edge;

    @Override
    public Edge CollapsedEdgeType(Object target, Object source, String influence) {
        //======================================================
        //SDM type: SDM_Edge
        return edge = new SDM_Edge(target, source, influence);
        //======================================================
    }

    @Override
    public void AddFilters(Variables variables, br.uff.ic.Prov_Viewer.Filters filter) {
        GraphFrame.FilterNodeAgentButton.setSelected(false);
        GraphFrame.FilterNodeLonelyButton.setSelected(false);
        GraphFrame.FilterEdgeQualityButton.setSelected(true);
        GraphFrame.FilterEdgeValButton.setSelected(true);
        GraphFrame.FilterEdgeProgressButton.setSelected(true);
        GraphFrame.FilterEdgeCreditsButton.setSelected(true);
        GraphFrame.FilterEdgeNeutralButton.setSelected(true);
        GraphFrame.FilterEdgeAidButton.setSelected(true);
        GraphFrame.FilterEdgeDiscoveryButton.setSelected(true);
        GraphFrame.FilterEdgeRepairButton.setSelected(true);
        GraphFrame.EdgeFilterBugsButton.setSelected(true);
        GraphFrame.EdgeFilterTCButton.setSelected(true);
        Filters(variables, filter, false);
    }

    @Override
    public void RemoveFilters(Variables variables, br.uff.ic.Prov_Viewer.Filters filter) {
        GraphFrame.FilterNodeAgentButton.setSelected(false);
        GraphFrame.FilterNodeLonelyButton.setSelected(false);
        GraphFrame.FilterEdgeQualityButton.setSelected(false);
        GraphFrame.FilterEdgeValButton.setSelected(false);
        GraphFrame.FilterEdgeProgressButton.setSelected(false);
        GraphFrame.FilterEdgeCreditsButton.setSelected(false);
        GraphFrame.FilterEdgeNeutralButton.setSelected(true);
        GraphFrame.FilterEdgeAidButton.setSelected(false);
        GraphFrame.FilterEdgeDiscoveryButton.setSelected(false);
        GraphFrame.FilterEdgeRepairButton.setSelected(false);
        GraphFrame.EdgeFilterBugsButton.setSelected(false);
        GraphFrame.EdgeFilterTCButton.setSelected(false);
        Filters(variables, filter);
    }
}
