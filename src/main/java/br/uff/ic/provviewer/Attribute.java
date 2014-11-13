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
public class Attribute {

    private String name;
    private String value;
    
    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }  
    
    public Attribute(String value) {
        this.name = "Unknown";
        this.value = value;
    }  
    
    public Attribute() {
        this.name = "Unknown";
        this.value = "0";
    }  
    
    public String getName()
    {
        return name;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setName(String t)
    {
        this.name = t;
    }
    
    public void setValue(String t)
    {
        this.value = t;
    }
    
    public String printAttribute()
    {
        return this.getName() + ": " + this.getValue() + " <br>";
    }
}
