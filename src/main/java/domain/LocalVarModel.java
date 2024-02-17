package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LocalVariableNode;

public class LocalVarModel {
    LocalVariableNode node;
    public LocalVarModel(LocalVariableNode node) {
        this.node = node;
    }
    /**
     * @return local variable name
     */
    public String getName() {
        return node.name;
    }

    /**
     * @return local variable description
     */
    public String getDesc() {
        return node.desc;
    }

    /**
     * @return local variable index
     */
    public int getIndex(){
        return node.index;
    }


}
