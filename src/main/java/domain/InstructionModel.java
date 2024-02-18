package domain;

import org.objectweb.asm.tree.InsnList;

public class InstructionModel {

    private InsnList node;
    public InstructionModel(InsnList asmNode){

        this.node = asmNode;
    }

    public int getSize(){return this.node.size();}

    public AbstractInsModel get(int index){return new AbstractInsModel(this.node.get(index));}
}
