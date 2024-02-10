package domain;

import org.objectweb.asm.tree.ClassNode;

public class ClassModel {
    private ClassNode node;

    public String getName() {
        return node.name;
    }


}
