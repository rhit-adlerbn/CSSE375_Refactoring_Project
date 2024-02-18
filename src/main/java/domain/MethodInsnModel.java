package domain;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MethodInsnModel {
    private final MethodInsnNode node;

    public MethodInsnModel(AbstractInsnNode node) {
        this.node = (MethodInsnNode) node;
    }

    /**
     * @param m the method being compared to this insn
     * @param owner the owner of the method m
     * @return is this insn accessing the method m
     */
    public boolean matchesMethod(MethodModel m, ClassModel owner) {
        MethodInsnNode n = (MethodInsnNode) node;
        return n.owner.equals(owner.getName())
                && n.name.equals(m.getName())
                && n.desc.equals(m.getDesc());
    }

    /**
     * @return whether the method's owner is an interface
     */
    public boolean isInterfaceMethod() {return node.itf;}
}
