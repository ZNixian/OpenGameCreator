/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Campbell Suter
 */
public class CommandRunner {

    private CommandRunner() {
    }

    public static String run(String command) {
        return run(command, new File(""), null);
    }

    public static String run(String command, File base) {
        return run(command, base, null);
    }

    public static String run(String command, OutputWindow out) {
        return run(command, new File(""), out);
    }

    public static String run(String command, File base, OutputWindow out) {
        String s;
        System.out.println("cd \""+base.getAbsolutePath()+"\"; "+command);

        try {
            Process p = Runtime.getRuntime().exec(command, null, base.getAbsoluteFile());
            StringBuilder sb;
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            sb = new StringBuilder();
            p.waitFor();
            while ((s = stdInput.readLine()) != null) {
                sb.append(s).append("\n");
            }
            while ((s = stdError.readLine()) != null) {
                sb.append(s).append("\n");
            }

            if (sb.length() != 0) {
                if (out == null) {
                    System.out.println("Command: " + command);
                    System.out.println(sb);
                } else {
                    out.add(sb);
                }
            }

            stdInput.close();
            stdError.close();

            return sb.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
