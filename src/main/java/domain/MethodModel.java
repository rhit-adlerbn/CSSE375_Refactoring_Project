package domain;

import org.objectweb.asm.tree.MethodNode;

public class MethodModel {
    private MethodNode node;
    public MethodModel(MethodNode node) {
        this.node = node;
    }
}
