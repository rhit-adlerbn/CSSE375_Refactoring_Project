package domain;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class AbstractInsModel {

    private AbstractInsnNode node;
    public AbstractInsModel(AbstractInsnNode asmNode){

        this.node = asmNode;
    }

    public int getType(){return this.node.getType();}


    public VarInsModel getVar(){return new VarInsModel(this.node);}
    //public int getSize(){return this.node.size();}
}
