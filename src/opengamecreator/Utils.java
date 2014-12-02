/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator;

import java.io.File;

/**
 *
 * @author Campbell Suter
 */
public class Utils {

    private Utils() {
    }

    public static void delete(File fi) {
        if(fi.isDirectory()) {
            for (File file : fi.listFiles()) {
                delete(file);
            }
            fi.delete();
        } else {
            fi.delete();
        }
    }
}
