package domain;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class FieldInsnModel {
    private final AbstractInsnNode node;


    public FieldInsnModel(AbstractInsnNode node) {
        this.node = (FieldInsnNode) node;
    }

    /**
     * @param f the field being compared to this insn
     * @param owner the owner of the field f
     * @return is this insn accessing the field f
     */
    public boolean matchesField(FieldModel f, ClassModel owner) {
        FieldInsnNode n = (FieldInsnNode) node;
        return n.owner.equals(owner.getName())
                && n.name.equals(f.getName())
                && n.desc.equals(f.getDesc());
    }

}
