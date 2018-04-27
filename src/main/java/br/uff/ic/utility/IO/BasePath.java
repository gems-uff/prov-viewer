/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package br.uff.ic.utility.IO;

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
                basePath = file.getParent() + File.separator + "classes";
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
