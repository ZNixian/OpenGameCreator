/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.blocks;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.codeblocks.BlockConnector;
import edu.mit.blocks.workspace.Workspace;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import opengamecreator.BlockInfo;
import opengamecreator.compiler.BlockCompiler;

/**
 *
 * @author Campbell Suter
 */
public class EvalUnit {

    private EvalUnit() {
    }

    public static String eval(Block b, Workspace w) {
        if (b == null) {
            return "";
        }
        if (b.isCommandBlock()) {
            return BlockCompiler.getDefaultBlockCompiler().compileBlocksUnder(b, w);
        }
        try {
            Class<?> c = (Class<?>) getC(b.getGenusName());
            Method m = c.getDeclaredMethod("eval", Block.class, Workspace.class);
            String eval = (String) m.invoke(null, b, w);
            return eval;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Class<?> getC(String b) throws ClassNotFoundException {
        try {
            return Class.forName("opengamecreator.blocks.impl." + b);
        } catch (ClassNotFoundException ex) {
            return Class.forName("opengamecreator.blocks.impl.block" + b);
        }
    }

    public static void codeFor(Block bb, Workspace w, StringBuilder sb) {
        try {
            Class<?> c = (Class<?>) getC(bb.getGenusName());
            Method m = c.getDeclaredMethod("codeFor", Block.class, Workspace.class);
            String eval = (String) m.invoke(null, bb, w);
            sb.append(eval);
        } catch (NoSuchMethodException | ClassNotFoundException ex) {
            String args = argsFor(bb, w);
            sb.append("\t\t").append(bb.getGenusName()).append("(").append(args).append(");\n");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String argsFor(Block bb, Workspace w) {
        String args = "";
        for (String s : argsForAsArray(bb, w)) {
            if (!"".equals(args)) {
                args += ", ";
            }
            args += s;
        }
        return args;
    }

    public static String[] argsForAsArray(Block bb, Workspace w) {
        String[] args = new String[bb.getNumSockets()];
        int i = 0;
        for (BlockConnector sock : bb.getSockets()) {
            Long blockID = sock.getBlockID();
            Block sbb = BlockInfo.getBlockById(w, blockID);
            if (sbb != null) {
                String eval = EvalUnit.eval(sbb, w);
                args[i] = eval;
            }
            i++;
        }
        return args;
    }
}
