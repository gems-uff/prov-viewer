package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.ActivityVertex;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Vertex Subclass customized to SDM
 * @author Kohwalter
 */
public class SDM_ProcessVertex extends ActivityVertex {
    private String date;
    private String name;
    private String task;
    private String role;
    private String morale;
    private String stamina;
    private String hours;
    private String cost;
    private String work;
    private String description;
    private String rate;
    
    /**
     * Constructor overload
     * @param array for TSV Reader
     */
    public SDM_ProcessVertex(String[] array) {
//        super(array[0]);
        super("Activity<br> " + "<b>Role: " + array[4] + "</b>" +
                " <br>" + "Date: " + array[1] + 
                " <br>" + "Name: " + array[2] + 
                " <br>" + "Task: " + array[3] + 
                " <br>" + "Morale: " + array[5] + 
                " <br>" + "Stamina: " + array[6] + 
                " <br>" + "Hours: " + array[7] +
                " <br>" + "Cost: " + array[8] + 
                " <br>" + "Work: " + array[9] + 
                " <br>" + "Rate: " + array[10] +
//                " <br>" + "Description: " + array[11] +
                " <br><br>");
        
        this.date = array[1];
        this.name = array[2];
        this.task = array[3];
        this.role = array[4];
        this.morale = array[5];
        this.stamina = array[6];
        this.hours = array[7];
        this.cost = array[8];
        this.work = array[9];
        this.rate = array[10];
//        this.description = array[11];
    }
    
    /**
     * Method to return Task
     * @return tasl
     */
    public String getTask()
    {
        return this.task;
    }
    
    /**
     * Method to return role
     * @return role
     */
    public String getRole()
    {
        return this.role;
    }
    /**
     * Method to return stamina
     * @return stamina
     */
    public int getStamina()
    {
        return Integer.parseInt(this.stamina);
    }
    /**
     * Method to return morale
     * @return morale
     */
    public int getMorale()
    {
        return Integer.parseInt(this.morale);
    }
    /**
     * Method to return working hours
     * @return working hours
     */
    public int getHours()
    {
        return Integer.parseInt(this.hours);
    }
    @Override
    public int getDate()
    {
        String[] day = this.date.split(":");
        return Integer.parseInt(day[0]);
    }
    /**
     * Method to return vertex color for "Fired"
     * @return color
     */
    public Paint getFiredColor()
    {
        return new Color(139,69,19);
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Float(-7, -7, 17, 17);
    }    

    @Override
    public Stroke getStroke(float width) {
        //dash = null returns a continuous line
        float[] dash = {4.0f};
        if(!this.task.equalsIgnoreCase("Idle")) {
            dash = null;
        }
        final Stroke nodeStroke = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        return nodeStroke;
    }

    @Override
    public Paint getColor() {
        if(this.task.contains("Idle"))
            return new Color(255,69,0);
        else if(this.role.contains("Training"))
            return new Color(102,0,102);
        else if(this.task.contains("Fired"))
            return getFiredColor();
        else if(this.task.contains("Promotion"))
            return new Color(0,153,0);
        else if(this.task.contains("Hired"))
            return new Color(139,136,120);
        else if(this.task.contains("Negotiation"))
            return new Color(193,205,193);
        return new Color(190,190,190);
    }
    
    @Override
    public String getNodeType() {
        return "Process";
    }

    @Override
    public String getDayName() {
        String[] day = this.date.split(":");
        return day[1];
    }

    @Override
    public String getName() {
        return name;
    }
}