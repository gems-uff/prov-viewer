/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer;

import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class GraphAttribute {

    private String name;
    private String value;
    private float minValue;
    private float maxValue;
    private int quantity;

    public GraphAttribute(String name, String value) {
        this.name = name;
        this.value = value;
        this.quantity = 1;
        if (Utils.tryParseFloat(value)){
            this.minValue = Float.parseFloat(this.value);
            this.maxValue = Float.parseFloat(this.value);
        }
        else {
            this.minValue = 0;
            this.maxValue = 0;
        }
    }

    public void updateAttribute(String value) {
        this.quantity++;
        if (Utils.tryParseFloat(value)) {
            this.value = Float.toString(Float.parseFloat(this.value) + Float.parseFloat(value));
            this.minValue = Math.min(this.minValue, Float.parseFloat(value));
            this.maxValue = Math.max(this.maxValue, Float.parseFloat(value));
        } else {
            this.value += " " + value;
        }
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getMin() {
        return Float.toString(minValue);
    }

    public String getMax() {
        return Float.toString(maxValue);
    }

    public void setName(String t) {
        this.name = t;
    }

    public void setValue(String t) {
        this.value = t;
    }

    public String printAttribute() {
        return this.getName() + ": " + printValue();
    }

    public String printValue() {
        if (Utils.tryParseFloat(this.value)) {
            return (Float.parseFloat(this.value) / this.quantity)
                    + " (" + this.getMin() + " ~ "
                    + this.getMax() + ")" + "<br>";
        } else {
            return this.value + "<br>";
        }
    }
}
