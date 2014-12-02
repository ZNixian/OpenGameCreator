/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.compiler;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Campbell Suter
 */
public class CommandCompiler {

    public static final File jgamePath;
    public static final File tmpPath = new File("tmp");
    public static String includePath;

    static {
        jgamePath = new File("jgame");
        includePath =
                jgamePath.getAbsolutePath() + "\\jars\\jgame-jre.jar"
                + ";"
                + "C:\\Users\\Campbell Suter\\Documents\\jmonkey\\projects\\OGCLib\\dist\\OGCLib.jar";
    }

    private CommandCompiler() {
    }

    public static void main(String... args) {
        compileAll();
    }

    public static String compileAll() {
        StringBuilder sb = new StringBuilder();
        BlockCompiler.tmpJarFile.delete();
        BlockCompiler.getDefaultBlockCompiler().buildManifest();
        String relative = tmpPath.toURI().relativize(BlockCompiler.tmpJarFile.toURI()).getPath();
        CommandRunner.run("jar cfm \""
                + relative + "\" \""
                + BlockCompiler.tmpManifestFile.getAbsolutePath() + "\"", tmpPath);
        for (File fi : BlockCompiler.tmpSourceFile.getParentFile().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".java");
            }
        })) {
            sb.append(compile(fi));
        }
        return sb.toString();
    }

    public static String compile(File file) {
        String s = CommandRunner.run("javac -classpath \"" + includePath
                + ";" + new File("tmp").getAbsolutePath() + "\" \"ogcgame/"
                + file.getName() + "\"", new File("tmp"));
        if (s.isEmpty()) {
            File f = new File(file.getParent(), file.getName().substring(0, file.getName().length() - 5) + ".class");
            CommandRunner.run("jar uf game.jar \"ogcgame/" + f.getName() + "\"", BlockCompiler.tmpJarFile.getParentFile());
        } else {
            if (!s.endsWith("\n")) {
                s += "\n";
            }
            s += "Could not add file to jar: did not compile!\n";
        }
        return s;
    }
}
