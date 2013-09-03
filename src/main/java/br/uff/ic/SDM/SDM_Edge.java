package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Edge;
import java.awt.Color;
import java.awt.Paint;

/**
 * Subclass of Edge
 * @author Kohwalter
 */
public class SDM_Edge extends Edge {

    /**
     * Constructor
     * @param target Edge's target
     * @param source Edge's source
     * @param influence Edge's influence value + type
     */
    public SDM_Edge(Object target, Object source, String influence) {
        super(target, source, influence);
    }

    @Override
    public String getType() {
        String[] line = this.getInfluence().toString().split(" ");
        if (this.isNeutral()) {
            return "Neutral";
        } else {
            if (line[1].equals("%")) {
                return line[2];
            } else if (line[1].equals("New")) {
                return "New Bug";
            } else {
                return line[1];
            }
        }
    }

    @Override
    public Paint getColor() {
        float value = getValue();
        if (this.getInfluence().contains("New Bug")) {
            if (value > 0) {
                return new Color(255, 0, 0);
            } else {
                return new Color(0, 0, 0);
            }
        } else if (value > 0) {
            return new Color(34, 139, 34);
        } else if (value < 0) {
            return new Color(255, 0, 0);
        } else {
            return new Color(0, 0, 0);
        }
    }

    @Override
    public boolean AddInfluence() {
        if (this.getInfluence().contains("Aid")) {
            return false;
        }
        return true;
    }
}
