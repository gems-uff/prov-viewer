/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Class responsible for correcting all file paths
 * @author Kohwalter
 */
public class BasePath {

    /**
     * Function to return the correct path for the file. Always use this function instead of manual path input
     * @param clazz is the class path
     * @return 
     */
    public static final String getBasePathForClass(Class<?> clazz) {
        File file;
        try {
            String basePath = null;
            file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            if (file.isFile() || file.getPath().endsWith(".jar") || file.getPath().endsWith(".zip")) {
                basePath = file.getParent() + File.separator + "Classes";
            } else {
                basePath = file.getPath();
            }
            // fix to run inside eclipse
            if (basePath.endsWith(File.separator + "lib") || basePath.endsWith(File.separator + "bin")
                    || basePath.endsWith("bin" + File.separator) || basePath.endsWith("lib" + File.separator)) {
                basePath = basePath.substring(0, basePath.length() - 4);
            }
            // fix to run inside netbean
            if (basePath.endsWith(File.separator + "build" + File.separator + "classes")) {
                basePath = basePath.substring(0, basePath.length() - 14);
            }
            // end fix
            if (!basePath.endsWith(File.separator)) {
                basePath = basePath + File.separator;
            }
            return basePath;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot firgue out base path for class: " + clazz.getName());
        }
    }
}
