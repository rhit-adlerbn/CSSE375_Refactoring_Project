package domain.model;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;


public class MethodModel extends Model{
    private MethodNode node;
    private ArrayList<LocalVarModel> localVars = new ArrayList<LocalVarModel>();
    private ArrayList<String> params = new ArrayList<String>();

    private InsnList instructions;

    /**
     * Constructor, instantiates LocalVarNodes, instructions, and a list of method parameters
     * @param node the MethodNode this Model Wraps
     */
    public MethodModel(MethodNode node) {
        this.node = node;
        if(node.localVariables!=null) {
            for (LocalVariableNode lv : node.localVariables) {
                localVars.add(new LocalVarModel(lv));
            }
        }
        for(Type t: Type.getArgumentTypes(node.desc)){
            String desc = t.getDescriptor();
            if(desc.contains("/")){
                params.add(desc.substring(desc.lastIndexOf("/")+1, desc.indexOf(";")));
            }
            else params.add(desc);
        }

        this.instructions = node.instructions;

    }

    /**
     * @return method name
     */
    @Override
    public String getName() {
        return node.name;
    }

    /**
     * @return method description
     */
    @Override
    public String getDesc() {
        return node.desc;
    }

    /**
     * Returns the plain class name of this methods return type
     * @return method return type
     */
    public String getReturnType(){
        String type = Type.getReturnType(node.desc).getClassName();
        if(type.contains(".")){
            return type.substring(type.lastIndexOf(".")+1);
        }
        else return type;
    }

    public InstructionModel getInstructions(){return new InstructionModel(this.instructions);}

    /**
     * @return is this method public
     */
    public boolean isPublic() {
        return isAccessModifier(Opcodes.ACC_PUBLIC);
    }
    /**
     * @return is this method private
     */
    public boolean isPrivate() {
        return isAccessModifier(Opcodes.ACC_PRIVATE);
    }
    /**
     * @return is this method protected
     */
    public boolean isProtected() {
        return isAccessModifier(Opcodes.ACC_PROTECTED);
    }
    /**
     * @return is this method static
     */
    public boolean isStatic() {
        return isAccessModifier(Opcodes.ACC_STATIC);
    }
    /**
     * @return is this method final
     */
    public boolean isFinal() {
        return isAccessModifier(Opcodes.ACC_FINAL);
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
     * @return the local variables
     */
    public List<LocalVarModel> getLocalVars() {
        return this.localVars;
    }
    /**
     * @return the method parameters
     */
    public ArrayList<String> getParams() {
        return this.params;
    }

    /**
     * Changes the access of the method to private
     */
    @Override
    public void privatize() {
        node.access &= ~(Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED);
        node.access |= Opcodes.ACC_PRIVATE;
    }
    /**
     * Changes the access of the node to public
     */
    @Override
    public void publicize() {
        node.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
        node.access |= Opcodes.ACC_PUBLIC;
    }

    public List<String> getNodeNames(){
        MethodModelVisitor methodVisitor = new MethodModelVisitor();
        node.accept(methodVisitor);
        return methodVisitor.getNames();
    }

    private class MethodModelVisitor extends MethodVisitor {
        private final List<String> names = new ArrayList<>();


        public MethodModelVisitor() {
            super(Opcodes.ASM9);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            
            MethodInsnNode methodInsnNode = new MethodInsnNode(opcode, owner, name, descriptor, isInterface);
            names.add(methodInsnNode.name);
            
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            FieldInsnNode fieldInsnNode = new FieldInsnNode(opcode, owner, name, descriptor);
            names.add(fieldInsnNode.name);
            
        }
        public List<String> getNames() {
            return names;
        }
    }
}
