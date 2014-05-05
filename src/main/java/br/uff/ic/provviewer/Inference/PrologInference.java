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

//        String attribute = "Hours";
//        String edgeType = "Neutral";
//        URL knowledge = Config.class.getResource("/BaseRegras.pl");
//        URL fact = Config.class.getResource("/BaseFatos.pl");
//        Query qKnowledgeBase = new Query("consult", new Term[]{new Atom(knowledge.getPath())});
//        Query qFactBase = new Query("consult", new Term[]{new Atom(fact.getPath())});
//        qKnowledgeBase.query();
//        qFactBase.query();
        //System.out.println("consult " + (qKnowledgeBase.query() ? "succeeded" : "failed"));
        //System.out.println("consult " + (qFactBase.query() ? "succeeded" : "failed"));
        
        Query q1 = new Query(new Compound("collapse", new Term[] { new Variable("L"), new Variable("X"), new Atom(attribute), new Atom(edgeType)}));
        //Query q2 = new Query(new Compound("setof", new Term[]{ new Variable("X"), (new Compound("collapsevertex", new Term[] { new Variable("X"), new Atom("Hours"), new Atom("Neutral")})), new Variable("L")}));
//        Query q2 = new Query("setof(X,collapsevertex(X,'Hours','Neutral'),L).");
        q1.query();
        //q2.query();
        solution = q1.oneSolution();
        //Clean the solution to a readable string
        if(solution != null)
        {
            String aux = (solution.get("L")).toString();
            System.out.println( "L = " + aux);
            aux = aux.replace("'.'", "");
            aux = aux.replace("[]", "");
            
            aux = aux.replace("(", " ");
            aux = aux.replace(")", " ");
            aux = aux.replace(",  ", ",");
            aux = aux.replace(",   , ", " ");
            aux = aux.replace("  ", " ");
            aux = aux.replace(",  ,", " ");
            
//            System.out.println( "L = " + aux);
            
//            aux = aux.replace("(a", "a");
//            aux = aux.replace(" ", "");
//            aux = aux.replace("))))", ")");
//            aux = aux.replace(",)", ")");
//            aux = aux.replace("))))", ")");
//            aux = aux.replace("),", " ");
//            aux = aux.replace("(", " ");
//            aux = aux.replace(")", " ");

            //Print Solution
            System.out.println( "L = " + aux);
            return aux;
        }
        return "";
        
//
//        System.out.println("first solution of descendent_of(X, joe)");
//        System.out.println("X = " + solution.get(X));
//        while ( q1.hasMoreSolutions() ){
//            solution = q1.nextSolution();
//            //System.out.println( "L = " + solution.get("L"));
//            System.out.println( "L = " + (solution.get("L")).toString());
//        }
        

//        System.out.println("consult " + (q1.query() ? "succeeded" : "failed"));


//        Query q2 =
//                new Query(
//                "child_of",
//                new Term[]{new Atom("joe"), new Atom("ralf")});
//        System.out.println(
//                "child_of(joe,ralf) is "
//                + (q2.query() ? "provable" : "not provable"));
//
//        Query q3 =
//                new Query(
//                "descendent_of",
//                new Term[]{new Atom("steve"), new Atom("ralf")});
//        System.out.println(
//                "descendent_of(joe,ralf) is "
//                + (q3.query() ? "provable" : "not provable"));

        
//        Query q4 =
//                new Query(
//                "descendent_of",
//                new Term[]{X, new Atom("joe")});
//        Query q4 = new Query("descendent_of(X,joe)");
//        Query q4 = new Query(new Compound("descendent_of", new Term[] { new Variable("X"), new Atom("joe")}));
//
//        while ( q4.hasMoreSolutions() ){
//            solution = q4.nextSolution();
//            System.out.println( "X = " + solution.get("X"));
//        }

//        java.util.Hashtable solution;
//
//        solution = q4.oneSolution();
//
//        System.out.println("first solution of descendent_of(X, joe)");
//        System.out.println("X = " + solution.get(X));

    }
}
