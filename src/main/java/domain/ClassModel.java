package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class ClassModel {
    private ClassNode node;
    private List<MethodModel> methods;
    private List<FieldModel> fields;

    public ClassModel(ClassNode node){
        this.node = node;
        for(MethodNode m : node.methods) {
            methods.add(new MethodModel(m));
        }
        for(FieldNode f: node.fields){
            fields.add(new FieldModel(f));
        }
    }

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
    public boolean isAbstract() {
        return isAccessModifier(Opcodes.ACC_ABSTRACT);
    }
    public boolean isInterface() {
        return isAccessModifier(Opcodes.ACC_INTERFACE);
    }
    public List<MethodModel> getMethods(){
        return this.methods;
    }
    public List<FieldModel> getFields(){
        return this.fields;
    }
    private boolean isAccessModifier(int opCode){
        return (node.access & opCode) != 0;
    }

}
