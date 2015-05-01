/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Inference;

//import alice.tuprolog.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
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
//    private Variable X = new Variable();
//    
//    //TuProlog
//    Prolog engine = new Prolog();
//    Theory theory;
//    Theory theory2;

    /**
* Adds the specified path to the java library path
*
* @param pathToAdd the path to add
* @throws Exception
*/
public static void addLibraryPath(String pathToAdd) throws Exception{
    final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
    usrPathsField.setAccessible(true);
 
    //get array of paths
    final String[] paths = (String[])usrPathsField.get(null);
 
    //check if the path to add is already present
    for(String path : paths) {
        if(path.equals(pathToAdd)) {
            return;
        }
    }
 
    //add the new path
    final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
    newPaths[newPaths.length-1] = pathToAdd;
    usrPathsField.set(null, newPaths);
}
    public void Init()
    {
//        try {
//            System.loadLibrary("jpl");
//        } catch (UnsatisfiedLinkError e) {
//            System.err.println("Native code library failed to load.\n" + e);
//        }
//        try {
//            System.load("C:/Program Files/swipl/binjpl.dll");
//        } catch (UnsatisfiedLinkError e) {
//            System.err.println("Native code library failed to load.\n" + e);
//        }
        try {
            addLibraryPath("C:/Program Files/swipl/bin");
        } catch (Exception ex) {
            Logger.getLogger(PrologInference.class.getName()).log(Level.SEVERE, null, ex);
        }

        URL knowledge = PrologInference.class.getResource("/Prolog/BaseRegras.pl");
        URL fact = PrologInference.class.getResource("/Prolog/PrologFacts_Angry.pl");
//        URL fact = null;
//        try {
//            fact = new URL("PrologFacts.pl");
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(PrologInference.class.getName()).log(Level.SEVERE, null, ex);
//        }
        Query qKnowledgeBase = new Query("consult", new Term[]{new Atom(knowledge.getPath())});
        Query qFactBase = new Query("consult", new Term[]{new Atom(fact.getPath())});
        //Query qFactBase = new Query("consult", new Term[]{new Atom("PrologFacts.pl")});
        qKnowledgeBase.query();
        qFactBase.query();
//            //TuProlog
//            try {
//                theory = new Theory(new FileInputStream(knowledge.getPath()));
//                theory2 = new Theory(new FileInputStream(fact.getPath()));
//                engine.addTheory(theory);
//                engine.addTheory(theory2);
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(PrologInference.class.getName()).log(Level.SEVERE, null, ex);
//            }
        System.out.println("Prolog facts and rules Set!");
    }
    public String QueryCollapse(String attribute, String edgeType) {

        
       
        //TuProlog
//        SolveInfo info;
//        try {
////            String q = "c2(L,"+attribute+","+ edgeType+").";
////            int a = 1;
////            int b = 2;
////            String q = "append(X,Y,[" + a + "," + b+ "]).";
////            info = engine.solve(q);
//            info = engine.solve("collapse_vertices(L,'Hours','Neutral').");
////            info = engine.solve("attribute_value(L,'Hours').");
////            info = engine.solve("attribute(_, _, 'Morale, Y).");
////            info =  engine.solve("append(X,Y,[1,2]).");
//
//                while (info.isSuccess()) {
////                    System.out.println("solution: " + info.getSolution()
////                            + " - bindings: " + info);
//                    System.out.println("solution: " + info);
//                    if (engine.hasOpenAlternatives()) {
//                        info = engine.solveNext();
//                    } else {
//                        break;
//                    }
//                }
//        } catch (MalformedGoalException ex) {
//            Logger.getLogger(PrologInference.class.getName()).log(Level.SEVERE, null, ex);
//        }
               
        //SWI Prolog
        Query q1 = new Query(new Compound("collapse_vertices", new Term[] { new Variable("L"), new Atom(attribute), new Atom(edgeType)}));

        q1.query();
        solution = q1.oneSolution();
        System.out.println( "S = " + solution);
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
