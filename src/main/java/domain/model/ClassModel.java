package domain.model;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import datasource.FileOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassModel {
    private final String SAVE_PATH = "files/";
    ClassNode node;
    private ArrayList<MethodModel> methods = new ArrayList<MethodModel>();
    private ArrayList<FieldModel> fields = new ArrayList<FieldModel>();
    private ArrayList<String> interfaces = new ArrayList<String>();
    private ArrayList<String> interfaceMethodNames = new ArrayList<String>();
    private ArrayList<MethodModel> abstractMethods = new ArrayList<MethodModel>();

    private String stringRepresentation = null;
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
           this.addInterface(i);
        }
        if(node.superName.equals("testclasses/TemplateClasses/Abstraction")) {
            handleAbstractClass();
        }
    }

    private void addInterface(String i) {
        ClassNode interfaceNode = new ClassNode();
        //get methods for the interface by defining a interface node
        ClassReader reader = null;
        try {
            reader = new ClassReader(i);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        reader.accept(interfaceNode, ClassReader.EXPAND_FRAMES);
        List<MethodNode> interfaceMethods = interfaceNode.methods;
        for(MethodNode method: interfaceMethods){
            interfaceMethodNames.add(method.name);
        }
        if(i.contains("/")){
            interfaces.add(i.substring(i.lastIndexOf("/")+1));
        }
        else interfaces.add(i);
    }

    private void handleAbstractClass() {
        ClassReader reader = null;
        try {
            reader = new ClassReader(node.superName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ClassNode abstractNode = new ClassNode();
        //get methods for the abstract by defining an abstract node
        reader.accept(abstractNode, ClassReader.EXPAND_FRAMES);
        ClassModel ac = new ClassModel(abstractNode);
        abstractMethods = (ArrayList<MethodModel>) ac.getMethods();
    }

    /**
     * Constructor, see above Constructor
     * @param node the ClassNode this Model Wraps
     * @param representation the string representation of the Class
     */
    public ClassModel(ClassNode node, String representation) {
        this(node);
        this.stringRepresentation = representation;
    }

    /**
     * @return the textual representation of a class bytecode
     */
    public String toString() { return this.stringRepresentation; }

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
     * @return this classes interfaces' methods
     */
    public List<String> getInterfaceMethods() {
        return interfaceMethodNames;
    }

    public List<MethodModel> getAbstractMethods(){ return abstractMethods; }

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
    public byte[] toBytes(){
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(classWriter);
        return classWriter.toByteArray();
    }
    public void saveModelToFile(){
        FileOutput.saveClass(toBytes(),SAVE_PATH + getName() + ".class");
    }

}
