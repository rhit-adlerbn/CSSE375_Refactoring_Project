package domain.model;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class InstructionModel {

    private InsnList node;

    public InstructionModel(InsnList asmNode){
        this.node = asmNode;
    }

    public int getSize(){return this.node.size();}

    public AbstractInsnModel get(int index){return new AbstractInsnModel(this.node.get(index));}

    public List<AbstractInsnModel> getInsnList(){
        List<AbstractInsnModel> nodes = new ArrayList<>();
        AbstractInsnNode[] asmNodes = node.toArray();
        for(AbstractInsnNode n : asmNodes){
            if(n != null)  nodes.add(new AbstractInsnModel(n));
        }
        return nodes;
    }
}
