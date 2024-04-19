package domain.model;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import static org.objectweb.asm.Opcodes.*;


public class AbstractInsnModel {

    protected AbstractInsnNode node;
    public AbstractInsnModel(AbstractInsnNode asmNode){

        this.node = asmNode;
    }

    public int getType(){return this.node.getType();}


    public VarInsModel getVar(){return new VarInsModel(this.node);}

    public FieldInsnModel getFieldInsnModel() {return new FieldInsnModel((FieldInsnNode)this.node);}

    public MethodInsnModel getMethodInsnModel() {return new MethodInsnModel((MethodInsnNode)this.node);}



    /**
     * @return is this Instruction a FieldInsnNode
     */
    public boolean isFieldInsn() {
        return (node.getOpcode() == GETSTATIC || node.getOpcode() == PUTSTATIC
                || node.getOpcode() == GETFIELD || node.getOpcode() == PUTFIELD);
    }

    /**
     * @return is this Instruction a MethodInsnNode
     */
    public boolean isMethodInsn() {
        return (node.getOpcode() == INVOKEVIRTUAL || node.getOpcode() == INVOKESTATIC
                || node.getOpcode() == INVOKEINTERFACE || node.getOpcode() == INVOKESPECIAL);
    }

}
