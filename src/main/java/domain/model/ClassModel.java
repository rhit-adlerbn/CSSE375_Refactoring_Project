package domain.model;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class ClassModel {
    private ClassNode node;
    private ArrayList<MethodModel> methods = new ArrayList<MethodModel>();
    private ArrayList<FieldModel> fields = new ArrayList<FieldModel>();
    private ArrayList<String> interfaces = new ArrayList<String>();
    /**
     * Constructor, instantiates a list of MethodModels and a list of FieldModels
     * @param node the ClassNode this Model Wraps
     */
    public ClassModel(ClassNode node){
        this.node = node;
        if(node.methods!=null) {
            for (MethodNode m : node.methods) {
                methods.add(new MethodModel(m));
            }
        }
        if(node.fields!=null) {
            for (FieldNode f : node.fields) {
                fields.add(new FieldModel(f));
            }
        }
        for(String i: node.interfaces){
            if(i.contains("/")){
                interfaces.add(i.substring(i.lastIndexOf("/")+1));
            }
            else interfaces.add(i);
        }
    }

    /**
     * @return a parsed class name
     */
    public String getName() {
        return node.name.substring(node.name.lastIndexOf("/")+1);
    }

    /**
     * @return this classes interfaces
     */
    public List<String> getInterfaces() {
        return interfaces;
    }

    /**
     * @return this classes super class
     */
    public String getSuperName() {
        return node.superName.substring(node.superName.lastIndexOf("/")+1);
    }
    /**
     * @return is this class public
     */
    public boolean isPublic() {
        return isAccessModifier(Opcodes.ACC_PUBLIC);
    }
    /**
     * @return is this class private
     */
    public boolean isPrivate() {
        return isAccessModifier(Opcodes.ACC_PRIVATE);
    }
    /**
     * @return is this class protected
     */
    public boolean isProtected() {
        return isAccessModifier(Opcodes.ACC_PROTECTED);
    }
    /**
     * @return is this class static
     */
    public boolean isStatic() {
        return isAccessModifier(Opcodes.ACC_STATIC);
    }
    /**
     * @return is this class final
     */
    public boolean isFinal() {
        return isAccessModifier(Opcodes.ACC_FINAL);
    }

    /**
     * @return is this class an Abstract Class
     */
    public boolean isAbstract() {
        return isAccessModifier(Opcodes.ACC_ABSTRACT);
    }

    /**
     * @return is this class an Interface
     */
    public boolean isInterface() {
        return isAccessModifier(Opcodes.ACC_INTERFACE);
    }
    /**
     * Determines the access modifiers
     * @param opCode the ASM access flag
     * @return if the access modifier applies to this classes node
     */
    private boolean isAccessModifier(int opCode){
        return (node.access & opCode) != 0;
    }

    /**
     * @return this classes Methods as MethodModels
     */
    public List<MethodModel> getMethods(){
        return this.methods;
    }
    /**
     * @return this classes Fields as FieldModels
     */
    public List<FieldModel> getFields(){
        return this.fields;
    }


}
