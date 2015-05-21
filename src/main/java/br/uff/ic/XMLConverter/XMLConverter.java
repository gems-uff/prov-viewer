/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.XMLConverter;

import br.uff.ic.provviewer.BasePath;
import br.uff.ic.provviewer.Input.Config;
import br.ufrj.ppgi.parser.XMLParser;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Kohwalter
 */
public class XMLConverter {

    private HashMap<String, File> fileList = new HashMap<String, File>();

    public void ConvertXMLtoProlog(File fileChosen) {
        String fileName = fileChosen.getName();
        fileList.put(fileName, fileChosen);
        XMLParser xmlParser = new XMLParser();
        xmlParser.setClearData(true);
        xmlParser.setResetLastId(true);
        xmlParser.executeParseSax(fileList);
        System.out.println();
        System.out.println("XML: " + fileName);

        try {
            File file = new File(BasePath.getBasePathForClass(Config.class) + File.separator + "Prolog" + File.separator + "PrologFacts.pl");

            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }

            File afile = new File("BaseFatos.pl");

            if (afile.renameTo(new File(BasePath.getBasePathForClass(Config.class) + File.separator + "Prolog" + File.separator + "PrologFacts.pl"))) {
                System.out.println("File is moved successful!");
            } else {
                System.out.println("File is failed to move!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
