package domain.model;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

public class FieldInsnModel extends AbstractInsnModel{


    public FieldInsnModel(AbstractInsnNode node) {
        super(node);
    }

    /**
     * @param f the field being compared to this insn
     * @param owner the owner of the field f
     * @return is this insn accessing the field f
     */
    public boolean matchesField(FieldModel f, ClassModel owner) {
        FieldInsnNode n = (FieldInsnNode) super.node;
        return n.owner.equals(owner.getName())
                && n.name.equals(f.getName())
                && n.desc.equals(f.getDesc());
    }

}
