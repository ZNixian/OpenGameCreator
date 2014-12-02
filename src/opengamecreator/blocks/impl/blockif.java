/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.blocks.impl;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;
import opengamecreator.BlockInfo;
import opengamecreator.blocks.EvalUnit;
import opengamecreator.compiler.BlockCompiler;

/**
 *
 * @author Campbell Suter
 */
public class blockif {

    public static String codeFor(Block b, Workspace w) {
        Block condition_block = BlockInfo.getBlockById(w, b.getSocketAt(0).getBlockID());
        String condition = EvalUnit.eval(condition_block, w);
        Block action_block = BlockInfo.getBlockById(w, b.getSocketAt(1).getBlockID());
        String action = BlockCompiler.getDefaultBlockCompiler().compileBlocksStartingAt(action_block, w);
        return "if(" + condition + ") {\n" + action + "\n}\n";
    }
}
