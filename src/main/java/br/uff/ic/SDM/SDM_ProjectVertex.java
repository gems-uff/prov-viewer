package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Vertex;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Vertex Subclass customized to SDM
 * @author Kohwalter
 */
public class SDM_ProjectVertex extends Vertex {
    private String name;
    private String date;
    private String deadline;
    private String linguagemProgramacao;
    private String pagamento;
    private String requirements;
    private String sincronismo;
    private String codeQuality;
    private String percentageDone;
    private String bugUnitaryFound;
    private String bugIntegrationFound;
    private String bugSystemFound;
    private String bugAcceptionFound;
    private String bugUnitaryRepaired;
    private String bugIntegrationRepaired;
    private String bugSystemRepaired;
    private String bugAcceptionRepaired;
    private String credits;
    private String totalBugs;
    
   // private String description;

    /**
     * Constructor overload
     * @param array for TSV Reader
     */
    public SDM_ProjectVertex(String[] array) {
//        super(array[0]);
        super("Project<br> " + "<b>Date: " + array[2] + "</b>" + 
                " <br>" + "Credits: " + array[18] + 
                " <br>" + "Name: " + array[1] + 
                " <br>" + "Deadline: " + array[3] + 
                " <br>" + "Code Completed: " + array[9] + "%" + 
                " <br>" + "Especification: " + array[6] +  "%" + //Requirements Modeled
                " <br>" + "Elicitation: " + array[7] +  "%" +   //Client's Requirements
                " <br>" + "Code Quality: " + array[8] +  "%" +
                " <br>" + "Salary: " + array[5] + 
                " <br><br>" + "Unitary Bugs Found: " + array[10] + 
                " <br>" + "Integration Bugs Found: " + array[11] + 
                " <br>" + "System Bugs Found: " + array[12] + 
                " <br>" + "Acception Bugs Found: " + array[13] + 
                " <br><br>" + "Unitary Bugs Repaired: " + array[14] + 
                " <br>" + "Integration Bugs Repaired: " + array[15] + 
                " <br>" + "System Bugs Repaired: " + array[16] + 
                " <br>" + "Acception Bugs Repaired: " + array[17] + 
                " <br>" + "Total Bugs: " + array[19] + 
                " <br><br>");
        this.name = array[1];
        this.date = array[2];
        this.deadline = array[3];
        this.linguagemProgramacao = array[4];
        this.pagamento = array[5];
        this.requirements = array[6];
        this.sincronismo = array[7];
        this.codeQuality = array[8];
        this.percentageDone = array[9];
        this.bugUnitaryFound = array[10];
        this.bugIntegrationFound = array[11];
        this.bugSystemFound = array[12];
        this.bugAcceptionFound = array[13];
        this.bugUnitaryRepaired = array[14];
        this.bugIntegrationRepaired = array[15];
        this.bugSystemRepaired = array[16];
        this.bugAcceptionRepaired = array[17];
        this.credits = array[18];
        this.totalBugs = array[19];
    }
    /**
     * Constructor overload
     * @deprecated 
     * @param id 
     */
    public SDM_ProjectVertex(String id) {
        super(id);
        this.name = "";
        this.date = "";
        this.deadline = "";
        this.linguagemProgramacao = "";
        this.pagamento = "";
        this.requirements = "";
        this.sincronismo = "";
        this.codeQuality = "";
        this.percentageDone = "";
        this.bugUnitaryFound = "";
        this.bugIntegrationFound = "";
        this.bugSystemFound = "";
        this.bugAcceptionFound = "";
        this.bugUnitaryRepaired = "";
        this.bugIntegrationRepaired = "";
        this.bugSystemRepaired = "";
        this.bugAcceptionRepaired = "";
        this.totalBugs = "";
    }

    /**
     * Method to return credits
     * @return credits
     */
    public int getCredits()
    {
//        return Float.parseFloat(credits);
        return Integer.parseInt(credits);
    }
    @Override
    public Shape getShape() {
        return new Ellipse2D.Float(-7, -7, 17, 17);
    }    

    @Override
    public Paint getColor() {
        return new Color(255,222,173);
    }

    @Override
    public String getNodeType() {
        return "Project";
    }
    
    @Override
    public String getDayName() {
        String[] day = date.split(":");
        return day[1];
    }

    @Override
    public int getDate() {
        String[] day = date.split(":");
        return Integer.parseInt(day[0]);
    }

    @Override
    public String getName() {
        return name;
    }
}