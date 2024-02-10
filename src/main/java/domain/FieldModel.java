package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

public class FieldModel {
    private FieldNode node;
    public FieldModel(FieldNode node) {
        this.node = node;
    }
    public String getName() {
        return node.name;
    }
    public String getDesc(){
        return node.desc;
    }
    public boolean isPublic() {
        return isAccessModifier(Opcodes.ACC_PUBLIC);
    }
    public boolean isPrivate() {
        return isAccessModifier(Opcodes.ACC_PRIVATE);
    }
    public boolean isProtected() {
        return isAccessModifier(Opcodes.ACC_PROTECTED);
    }
    public boolean isStatic() {
        return isAccessModifier(Opcodes.ACC_STATIC);
    }
    public boolean isFinal() {
        return isAccessModifier(Opcodes.ACC_FINAL);
    }
    private boolean isAccessModifier(int opCode){
        return (node.access & opCode) != 0;
    }
}
