package domain.model;

import org.objectweb.asm.tree.FieldInsnNode;

public class FieldInsnModel {
    private final FieldInsnNode node;


    public FieldInsnModel(FieldInsnNode node) {
        this.node = node;
    }

    /**
     * @param f the field being compared to this insn
     * @param owner the owner of the field f
     * @return is this insn accessing the field f
     */
    public boolean matches(Model f, ClassModel owner) {
        return node.owner.equals(owner.getName())
                && node.name.equals(f.getName())
                && node.desc.equals(f.getDesc());
    }

}
