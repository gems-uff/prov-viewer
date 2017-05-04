///*
// * The MIT License
// *
// * Copyright 2017 Kohwalter.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//
//package br.uff.ic.provviewer.xmlToProlog;
//
//import br.uff.ic.utility.IO.BasePath;
//import br.uff.ic.provviewer.Input.Config;
//import br.ufrj.ppgi.parser.XMLParser;
//import java.io.File;
//import java.util.HashMap;
//
///**
// *
// * @author Kohwalter
// */
//public class XMLConverter {
//
//    private HashMap<String, File> fileList = new HashMap<String, File>();
//
//    public void ConvertXMLtoProlog(File fileChosen) {
//        String fileName = fileChosen.getName();
//        fileList.put(fileName, fileChosen);
//        XMLParser xmlParser = new XMLParser();
//        xmlParser.setClearData(true);
//        xmlParser.setResetLastId(true);
//        xmlParser.executeParseSax(fileList);
//        System.out.println();
//        System.out.println("XML: " + fileName);
//
//        try {
//            File file = new File(BasePath.getBasePathForClass(Config.class) + File.separator + "Prolog" + File.separator + "PrologFacts.pl");
//
//            if (file.delete()) {
//                System.out.println(file.getName() + " is deleted!");
//            } else {
//                System.out.println("Delete operation is failed.");
//            }
//
//            File afile = new File("BaseFatos.pl");
//
//            if (afile.renameTo(new File(BasePath.getBasePathForClass(Config.class) + File.separator + "Prolog" + File.separator + "PrologFacts.pl"))) {
//                System.out.println("File is moved successful!");
//            } else {
//                System.out.println("File is failed to move!");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//}
