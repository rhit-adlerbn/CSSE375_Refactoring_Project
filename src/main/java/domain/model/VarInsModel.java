package domain.model;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;

public class VarInsModel {

    private VarInsnNode node;
    public VarInsModel(AbstractInsnNode asmNode){

        this.node = (VarInsnNode)asmNode;
    }

    public int getType(){return this.node.getType();}

    public int getVar(){return this.node.var;}
    //public int getSize(){return this.node.size();}

    public int getOpCode(){return this.node.getOpcode();}
}
