/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.blocks.impl;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;
import opengamecreator.BlockInfo;
import opengamecreator.blocks.EvalUnit;

/**
 *
 * @author Campbell Suter
 */
public class setObjectX {

    public static String codeFor(Block b, Workspace w) {
        Long blockID = b.getSocketAt(0).getBlockID();
        Block sbb = BlockInfo.getBlockById(w, blockID);
        String eval = EvalUnit.eval(sbb, w);
        return "x=" + eval + ";";
    }
}
