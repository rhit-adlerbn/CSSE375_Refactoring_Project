package domain;

import org.objectweb.asm.tree.InsnList;

public class InstructionModel {

    private InsnList node;
    public InstructionModel(InsnList asmNode){

        this.node = asmNode;
    }

    public int getSize(){return this.node.size();}

    public AbstractInsnModel get(int index){return new AbstractInsnModel(this.node.get(index));}
}
