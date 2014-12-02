/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.compiler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Campbell Suter
 */
public class CommandCompiler {

    public static final File jgamePath;
    public static final File tmpPath = new File("tmp");
    public static String includePath;
    public static String includePathJar;

    static {
        jgamePath = new File("assets");
        includePath =
                jgamePath.getAbsolutePath() + "/jgame-jre.jar"
                + File.pathSeparator
                + jgamePath.getAbsolutePath() + "/OGCLib.jar";

        includePathJar =
                ". " + relativeFile(tmpPath, jgamePath, "jgame-jre.jar") + " "
                + relativeFile(tmpPath, jgamePath, "OGCLib.jar");
    }

    /**
     * Computes the path for a file relative to a given base, or fails if the
     * only shared directory is the root and the absolute form is better.
     *
     * @param base File that is the base for the result
     * @param name File to be "relativized"
     * @return the relative name
     * @throws IOException if files have no common sub-directories, i.e. at best
     * share the root prefix "/" or "C:\"
     */
    public static String getRelativePath(File base, File name) throws IOException {
        File parent = base.getParentFile();

        if (parent == null) {
            throw new IOException("No common directory");
        }

        String bpath = base.getCanonicalPath();
        String fpath = name.getCanonicalPath();

        if (fpath.startsWith(bpath)) {
            return fpath.substring(bpath.length() + 1);
        } else {
            return (".." + File.separator + getRelativePath(parent, name));
        }
    }

    public static String relativeFile(File base, File path) {
        Path sourceFile = base.toPath();
        Path targetFile = path.toPath();
        Path relativePath = sourceFile.relativize(targetFile);
        return relativePath.toString();
    }

    public static String relativeFile(File base, File path1, String path2) {
        return relativeFile(base, new File(path1, path2));
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
        for (File fi : BlockCompiler.tmpSourceFile.getParentFile().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".java");
            }
        })) {
            sb.append(compile(fi));
        }
        CommandRunner.run("jar cfm \""
                + relative + "\" \""
                + BlockCompiler.tmpManifestFile.getAbsolutePath() + "\" ogcgame", tmpPath);
        return sb.toString();
    }

    public static String compile(File file) {
        String s = CommandRunner.run("javac -classpath \"" + includePath
                + ";" + new File("tmp").getAbsolutePath() + "\" \"ogcgame/"
                + file.getName() + "\"", new File("tmp"));
        if (s.isEmpty()) {
//            File f = new File(file.getParent(), file.getName().substring(0, file.getName().length() - 5) + ".class");
//            CommandRunner.run("jar uf game.jar \"ogcgame/" + f.getName() + "\"", BlockCompiler.tmpJarFile.getParentFile());
        } else {
            if (!s.endsWith("\n")) {
                s += "\n";
            }
            s += "Could not add file to jar: did not compile!\n";
        }
        return s;
    }
}
