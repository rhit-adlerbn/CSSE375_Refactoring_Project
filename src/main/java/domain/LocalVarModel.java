package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LocalVariableNode;

public class LocalVarModel {
    LocalVariableNode node;
    public LocalVarModel(LocalVariableNode node) {
        this.node = node;
    }
    public String getName() {
        return node.name;
    }
    public String getDesc() {
        return node.desc;
    }
    public int getIndex(){
        return node.index;
    }

}
