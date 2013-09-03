package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Vertex;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Vertex Subclass customized to SDM
 *
 * @author Kohwalter
 */
public class SDM_AgentVertex extends Vertex {

    private String name;
    private String salary;
    private String job;
    private String level;
    private String adaptabilidade;
    private String autoDidata;
    private String detalhista;
    private String negociacao;
    private String objetividade;
    private String organizacao;
    private String paciencia;
    private String raciocinioLogico;
    private String relacionamentoHumano;
    private String specialization;

    /**
     * Constructor
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public SDM_AgentVertex(String id) {
        super(id);
    }

    /**
     * Constructor overload
     * @param array for TSV Reader
     */
    public SDM_AgentVertex(String[] array) {
//        super(array[0]);
        super("Agent<br> " + "<b>Name: " + array[1] + "</b>"
                + " <br>" + " Job: " + array[3]
                + " <br>" + " Level: " + array[4]
                + " <br>" + " Adaptability: " + array[5]
                + " <br>" + " Autodidact: " + array[6]
                + " <br>" + " Meticulous: " + array[7]
                + " <br>" + " Negotiation: " + array[8]
                + " <br>" + " Objectivity: " + array[9]
                + " <br>" + " Organization: " + array[10]
                + " <br>" + " Patience: " + array[11]
                + " <br>" + " Logical Reasoning: " + array[12]
                + " <br>" + " Human Relations: " + array[13]
                + " <br>" + " Specializations: " + array[14]
                + " <br><br>");
        this.name = array[1];
        this.salary = array[2];
        this.job = array[3];
        this.level = array[4];
        this.adaptabilidade = array[5];
        this.autoDidata = array[6];
        this.detalhista = array[7];
        this.negociacao = array[8];
        this.objetividade = array[9];
        this.organizacao = array[10];
        this.paciencia = array[11];
        this.raciocinioLogico = array[12];
        this.relacionamentoHumano = array[13];
        this.specialization = array[14];
    }

    @Override
    public Shape getShape() {
        int[] XArray = {-7, -2, 3, 8, 8, 3, -2, -7};
        int[] YArray = {-2, -7, -7, -2, 3, 8, 8, 3};

        return new Polygon(XArray, YArray, 8);
    }

    @Override
    public Paint getColor() {
        return new Color(119, 136, 153);
    }

    @Override
    public String getNodeType() {
        return "Agent";
    }

    @Override
    public String getDayName() {
        return null;
    }

    @Override
    public int getDate() {
        return 0;
    }

    public String getName() {
        return name;
    }
}