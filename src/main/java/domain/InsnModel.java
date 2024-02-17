package domain;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class InsnModel {
    final AbstractInsnNode node;

    protected InsnModel(AbstractInsnNode node) {
        this.node = node;
    }

    /**
     * @param f the field being compared to this insn
     * @param owner the owner of the field f
     * @return is this insn accessing the field f
     */
    public boolean matchesField(FieldModel f, ClassModel owner) {
        FieldInsnNode n = (FieldInsnNode) node;
        return isFieldInsn()
                && n.owner.equals(owner.getName())
                && n.name.equals(f.getName())
                && n.desc.equals(f.getDesc());
    }

    /**
     * @param m the method being compared to this insn
     * @param owner the owner of the method m
     * @return is this insn accessing the method m
     */
    public boolean matchesMethod(MethodModel m, ClassModel owner) {
        MethodInsnNode n = (MethodInsnNode) node;
        return isMethodInsn()
                && n.owner.equals(owner.getName())
                && n.name.equals(m.getName())
                && n.desc.equals(m.getDesc());
    }

    /**
     * @return is this Instruction a FieldInsnNode
     */
    private boolean isFieldInsn() {
        return (node.getOpcode() == GETSTATIC || node.getOpcode() == PUTSTATIC
        || node.getOpcode() == GETFIELD || node.getOpcode() == PUTFIELD);
    }

    /**
     * @return is this Instruction a MethodInsnNode
     */
    private boolean isMethodInsn() {
        return (node.getOpcode() == INVOKEVIRTUAL || node.getOpcode() == INVOKESTATIC
                || node.getOpcode() == INVOKEINTERFACE || node.getOpcode() == INVOKESPECIAL);
    }



}
