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
    public boolean matches(Model m, ClassModel owner) {
        return node.owner.equals(owner.getName())
                && node.name.equals(m.getName())
                && node.desc.equals(m.getDesc());
    }

    /**
     * @return whether the method's owner is an interface
     */
    public boolean isInterfaceMethod() {
        MethodInsnNode n = (MethodInsnNode) super.node;
        return n.itf;
    }
}
