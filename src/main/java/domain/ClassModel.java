package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class ClassModel {
    private ClassNode node;

    public String getName() {
        return node.name;
    }
    public List<String> getInterfaces() {
        return node.interfaces;
    }
    public String getSuperName() {
        return node.superName;
    }
    public boolean isPublic() {
        return isAccessModifer(Opcodes.ACC_PUBLIC);
    }
    public boolean isPrivate() {
        return isAccessModifer(Opcodes.ACC_PRIVATE);
    }
    public boolean isProtected() {
        return isAccessModifer(Opcodes.ACC_PROTECTED);
    }
    public boolean isStatic() {
        return isAccessModifer(Opcodes.ACC_STATIC);
    }
    public boolean isFinal() {
        return isAccessModifer(Opcodes.ACC_FINAL);
    }
    public boolean isAbstract() {
        return isAccessModifer(Opcodes.ACC_ABSTRACT);
    }
    public boolean isInterface() {
        return isAccessModifer(Opcodes.ACC_INTERFACE);
    }
    private boolean isAccessModifer(int opCode){
        return (node.access & opCode) != 0;
    }

}
