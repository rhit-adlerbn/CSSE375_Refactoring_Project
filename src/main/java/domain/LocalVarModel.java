package domain;

import org.objectweb.asm.tree.LocalVariableNode;

public class LocalVarModel {
    LocalVariableNode node;
    public LocalVarModel(LocalVariableNode node) {
        this.node = node;
    }
}
