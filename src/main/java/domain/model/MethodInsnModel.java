package domain.model;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MethodInsnModel extends AbstractInsnModel {

    public MethodInsnModel(AbstractInsnNode node) {
        super(node);
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
    public boolean isInterfaceMethod() {
        MethodInsnNode n = (MethodInsnNode) super.node;
        return n.itf;
    }
}
