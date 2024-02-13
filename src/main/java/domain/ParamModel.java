package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ParameterNode;

public class ParamModel {
    ParameterNode node;
    public ParamModel(ParameterNode node) {
        this.node = node;
    }
    public String getName() {
        return node.name;
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
