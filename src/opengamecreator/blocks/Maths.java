/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.blocks;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;
import opengamecreator.BlockInfo;

/**
 *
 * @author Campbell Suter
 */
public class Maths {

    public static double[] eval(Block b, Workspace w) {
        String[] bb = socks(b, w);

        double d1 = Double.parseDouble(bb[0]);
        double d2 = Double.parseDouble(bb[1]);

        return new double[]{d1, d2};
    }

    public static String[] socks(Block b, Workspace w) {
        Block b1 = BlockInfo.getBlockById(w, b.getSocketAt(0).getBlockID());
        Block b2 = BlockInfo.getBlockById(w, b.getSocketAt(1).getBlockID());

        return new String[]{EvalUnit.eval(b1, w), EvalUnit.eval(b2, w)};
    }
}
