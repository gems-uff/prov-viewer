/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Inference;

import br.uff.ic.provviewer.Input.Config;
import java.net.URL;
import java.util.Hashtable;
import jpl.Atom;
import jpl.Compound;
import jpl.Query;
import jpl.Term;
import jpl.Variable;

/**
 *
 * @author Kohwalter
 */
public class PrologInference {
    private Hashtable solution;
    private Variable X = new Variable();

//    Variable X = new Variable();
    public void Init()
    {
        URL knowledge = Config.class.getResource("/BaseRegras.pl");
        URL fact = Config.class.getResource("/BaseFatos.pl");
        Query qKnowledgeBase = new Query("consult", new Term[]{new Atom(knowledge.getPath())});
        Query qFactBase = new Query("consult", new Term[]{new Atom(fact.getPath())});
        qKnowledgeBase.query();
        qFactBase.query();
    }
    public String QueryCollapse(String attribute, String edgeType) {

        Query q1 = new Query(new Compound("collapse", new Term[] { new Variable("L"), new Variable("X"), new Atom(attribute), new Atom(edgeType)}));

        q1.query();
        solution = q1.oneSolution();
        //Clean the solution to a readable string
        if(solution != null)
        {
            String aux = (solution.get("L")).toString();
            aux = aux.replace("'.'", "");
            aux = aux.replace("[]", "");
            
            aux = aux.replace("(", " ");
            aux = aux.replace(")", " ");
            aux = aux.replace(",  ", ",");
            aux = aux.replace(",   , ", " ");
            aux = aux.replace("  ", " ");
            aux = aux.replace(",  ,", " ");

            //Print Solution
//            System.out.println( "L = " + aux);
            return aux;
        }
        return "";

    }
}
