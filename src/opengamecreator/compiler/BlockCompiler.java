/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.compiler;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.codeblocks.BlockConnector;
import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.renderable.RenderableBlock;
import edu.mit.blocks.workspace.Page;
import edu.mit.blocks.workspace.Workspace;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import opengamecreator.BlockInfo;
import opengamecreator.blocks.EvalUnit;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Campbell Suter
 */
public class BlockCompiler {

    public static final File tmpSourceFile = new File("tmp/ogcgame/OGCMain.java");
    public static final File tmpJarFile = new File("tmp/game.jar");
    public static final File tmpManifestFile = new File("tmp/manifest.mf");
    private static BlockCompiler defaultStaticInstanceSingleton = new BlockCompiler();

    public static BlockCompiler getDefaultBlockCompiler() {
        return defaultStaticInstanceSingleton;
    }

    public void compileAndRun(final WorkspaceController wc) {
        Thread t = new Thread() {
            @Override
            public void run() {
                OutputWindow out = new OutputWindow(wc.getWorkspacePanel());
                out.add("Building source...");
//                String errors = compile(wc);
                compile(wc, false);
                out.add("Done building source...");
                out.add("Compiling...");
                String errors = CommandCompiler.compileAll();
                if (errors.isEmpty()) {
                    out.add("Compile Successful.");
                    out.add("Running...");
                    CommandRunCommand.run(out);
                    out.add("Done.");
                } else {
                    out.add("One or more files were compiled with errors!\n" + errors);
                }
                out.remove();
            }
        };
        t.start();
    }

    public String compile(WorkspaceController wc, boolean export) {
        String errors = "";
        Workspace ws = wc.getWorkspace();
        Page global = ws.getPageNamed("Global");
        for (Page pg : wc.getWorkspace().getBlockCanvas().getPages()) {
            if (pg != global) {
                errors += compileObject(wc, pg);
            }
        }
        errors += compileGlobal(wc, global);
        createMediaTable(export);
        return errors.trim();
    }

    public String compileGlobal(WorkspaceController wc, Page global) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.imports);
        sb.append(Constants.mainHead);
//        int i = 0;
//        for (Block b : getSetups(wc)) {
//            sb.append("\tpublic void setup").append(i++).append("() {\n");
//            sb.append(compileSeries(b, wc));
//            sb.append("\t}\n");
//        }
//        runSeries(i, "setup()", "setup", sb);
        compileGenius(wc, global, "usersetup()", "setup", "usersetupF", sb);
        sb.append("}");
        return saveAndCompile(new File("tmp/ogcgame/Main.java"), sb.toString());
    }

    public String compileObject(WorkspaceController wc, Page pg) {
        StringBuilder sb = new StringBuilder();
        String className = "Object" + pg.getPageName();
        String spritename = getTextureForPage(pg, wc.getWorkspace());
        sb.append(Constants.imports);
        sb.append(Constants.objectClassHead
                .replace("%%%classname%%%", className)
                .replace("%%%spritename%%%", spritename + ""));
        compileGenius(wc, pg, "move()", "forever", "onframe", sb);
        compileGenius(wc, pg, "setup()", "setup", "setup", sb);
        compileGenius(wc, pg, "paint()", "draw", "drawUserF", sb);
//        runSeries(i, "setup()", "setup", sb);
        sb.append("}");
        return saveAndCompile(new File("tmp/ogcgame/", className + ".java"), sb.toString());
    }

    private void compileGenius(WorkspaceController wc, Page pg, String callName,
            String genius, String baseName, StringBuilder sb) {
        int i = 0;
        for (Block b : getTopLevelBlocksFromGeniusAndPage(genius, pg)) {
            sb.append("\tpublic void ").append(baseName).append(i++).append("() {\n");
            sb.append(compileBlocks(b, wc));
            sb.append("\t}\n");
        }
        runSeries(i, callName, baseName, sb);
    }

    private List<Block> getTopLevelBlocksFromGeniusAndPage(String genius, Page page) {
        List<Block> blocks = new ArrayList<>();
        for (RenderableBlock block : page.getTopLevelBlocks()) {
            if (block.getGenus().equals(genius)) {
                blocks.add(block.getBlock());
            }
        }
        return blocks;
    }

    private String saveAndCompile(File f, String s) {
        save(f, s);
        return "";//CommandCompiler.compile(f);
    }

    private void save(File f, String s) {
        try {
            f.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(f);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(s);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void runSeries(int i, String thisName, String otherName, StringBuilder sb) {
        sb.append("public void ").append(thisName).append(" {\n");
        for (int j = 0; j < i; j++) {
            sb.append(otherName).append(j).append("();");
        }
        sb.append("}");
    }

    public String compileBlocks(Block b, WorkspaceController wc) {
        return compileBlocksUnder(b, wc.getWorkspace());
    }

    public String compileBlocksUnder(Block b, Workspace w) {
        StringBuilder sb = new StringBuilder();
        BlockConnector socketAt = b.getSocketAt(0);
        Block bb = BlockInfo.getBlockById(w, socketAt.getBlockID());
        while (bb != null) {
            EvalUnit.codeFor(bb, w, sb);
            bb = BlockInfo.getBlockById(w, bb.getAfterBlockID());
        }
        return sb.toString();
    }

    public String compileBlocksStartingAt(Block b, Workspace w) {
        StringBuilder sb = new StringBuilder();
        while (b != null) {
            EvalUnit.codeFor(b, w, sb);
            b = BlockInfo.getBlockById(w, b.getAfterBlockID());
        }
        return sb.toString();
    }

    public void createMediaTable(boolean export) {
        StringBuilder sb = new StringBuilder();
        File baseSprites = new File("tmp/io/resource/sprites");
        for (File file : baseSprites.listFiles()) {
            if (!file.getName().endsWith(".png") && !file.getName().endsWith(".bmp") && !file.getName().endsWith(".gif")) {
                continue;
            } else {
            }
            sb
                    .append(DigestUtils.md5Hex(file.getName()))
                    .append("	-	0	../io/resource/sprites/")
                    .append(file.getName())
                    .append("	-\n");
        }
        save(new File("tmp/ogcgame/", "media.tbl"), sb.toString());
    }

    public Block getBlockForGenius(String genius, Page page) {
        List<Block> blocks = getTopLevelBlocksFromGeniusAndPage(genius, page);
        if (blocks.size() != 1) {
            return null;
        }
        return blocks.get(0);
    }

    public String getTextureForPage(Page pg, Workspace ws) {
        Block b = getBlockForGenius("propSprite", pg);

        if (b == null) {
            return null;
        }

        BlockConnector bc = b.getSocketAt(0);

        if (!bc.hasBlock()) {
            return null;
        }

        Block bb = ws.getEnv().getBlock(bc.getBlockID());

        if (bb == null) {
            return null;
        }

        String sprite = EvalUnit.eval(bb, ws);

        if (sprite == null) {
            return null;
        }

        sprite = sprite.substring(1, sprite.length() - 1);

//        System.out.println(sprite);

        return "\"" + DigestUtils.md5Hex(sprite) + "\"";
    }

    public void buildManifest() {
        save(tmpManifestFile,
                "Manifest-Version: 1.0\n"
                + "Class-Path: " + CommandCompiler.includePathJar + "\n"
                + "Main-class: ogcgame.Main\n"
                + "");
    }
}
