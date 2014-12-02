/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.compiler;

import java.io.File;

/**
 *
 * @author Campbell Suter
 */
public class CommandRunCommand {

    private CommandRunCommand() {
    }

    public static void main(String[] args) throws Exception {
        run();
    }

    public static String run() {
        return run(null);
    }

    public static String run(OutputWindow out) {
        return CommandRunner.run("java -jar \""
                + BlockCompiler.tmpJarFile.getName()+ "\"", new File("tmp"), out);
    }
}
