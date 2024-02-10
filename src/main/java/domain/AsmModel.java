package domain;

import org.objectweb.asm.ClassVisitor;

public abstract class AsmModel {
    private  ClassVisitor node;

    public String getName() {
        return node.getClass().getName();
    }

}
