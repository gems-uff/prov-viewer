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
    
    //TuProlog
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
        
        //TuProlog
         try {
            theory = new Theory(new FileInputStream(knowledge.getPath()));
            theory2 = new Theory(new FileInputStream(fact.getPath()));
            engine.addTheory(theory);
            engine.addTheory(theory2);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrologInference.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String QueryCollapse(String attribute, String edgeType) throws NoMoreSolutionException {

        
       
        //TuProlog
        SolveInfo info;
        try {
//            String q = "c2(L,"+attribute+","+ edgeType+").";
//            int a = 1;
//            int b = 2;
//            String q = "append(X,Y,[" + a + "," + b+ "]).";
//            info = engine.solve(q);
            info = engine.solve("collapse_vertices(L,'Hours','Neutral').");
//            info = engine.solve("attribute_value(L,'Hours').");
//            info = engine.solve("attribute(_, _, 'Morale, Y).");
//            info =  engine.solve("append(X,Y,[1,2]).");

                while (info.isSuccess()) {
//                    System.out.println("solution: " + info.getSolution()
//                            + " - bindings: " + info);
                    System.out.println("solution: " + info);
                    if (engine.hasOpenAlternatives()) {
                        info = engine.solveNext();
                    } else {
                        break;
                    }
                }
        } catch (MalformedGoalException ex) {
            Logger.getLogger(PrologInference.class.getName()).log(Level.SEVERE, null, ex);
        }
               
        //SWI Prolog
        Query q1 = new Query(new Compound("collapse_vertices", new Term[] { new Variable("L"), new Atom(attribute), new Atom(edgeType)}));

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
            //System.out.println( "L = " + aux);
            return aux;
        }
        return "";

    }
}
