/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.XMLConverter;

import br.ufrj.ppgi.parser.XMLParser;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Kohwalter
 */
public class XMLConverter {
    
    private HashMap<String, File> fileList = new HashMap<String, File>();
    
    public void ConvertXMLtoProlog(File fileChosen){
        String fileName = fileChosen.getName();
        fileList.put(fileName, fileChosen);
        XMLParser xmlParser = new XMLParser();
        xmlParser.setClearData(true);
        xmlParser.setResetLastId(true);
        xmlParser.executeParseSax(fileList);
        
        File afile =new File("BaseFatos.pl");
        afile.renameTo(new File("src\\main\\resources\\PrologFacts.pl"));
        
    }
}
