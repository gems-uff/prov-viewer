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
import alice.tuprolog.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kohwalter
 */
public class PrologInference {
    private Hashtable solution;
    private Variable X = new Variable();
    
    Prolog engine = new Prolog();
    Theory theory;
    Theory theory2;

//    Variable X = new Variable();
    public void Init() throws IOException, InvalidTheoryException
    {
        URL knowledge = Config.class.getResource("/BaseRegras.pl");
        URL fact = Config.class.getResource("/BaseFatos.pl");
        Query qKnowledgeBase = new Query("consult", new Term[]{new Atom(knowledge.getPath())});
        Query qFactBase = new Query("consult", new Term[]{new Atom(fact.getPath())});
        qKnowledgeBase.query();
        qFactBase.query();
        
         try {
            theory = new Theory(new FileInputStream(knowledge.getPath()));
            theory2 = new Theory(new FileInputStream(fact.getPath()));
            engine.addTheory(theory);
            engine.addTheory(theory2);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrologInference.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String QueryCollapse(String attribute, String edgeType) {

        
       

//        SolveInfo info;
//        try {
//            String q = "collapse(L,"+attribute+","+ edgeType+").";
//            info = engine.solve("collapse(L,'Hours','Neutral').");
//            try {
//                System.out.println(info.getSolution());
//            } catch (NoSolutionException ex) {
//                Logger.getLogger(PrologInference.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } catch (MalformedGoalException ex) {
//            Logger.getLogger(PrologInference.class.getName()).log(Level.SEVERE, null, ex);
//        }
        


        Query q1 = new Query(new Compound("c2", new Term[] { new Variable("L"), new Atom(attribute), new Atom(edgeType)}));

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
            System.out.println( "L = " + aux);
            return aux;
        }
        return "";

    }
}
