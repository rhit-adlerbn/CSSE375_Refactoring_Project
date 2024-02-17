package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MethodModel {
    private MethodNode node;
    private ArrayList<LocalVarModel> localVars = new ArrayList<LocalVarModel>();
    private ArrayList<String> params = new ArrayList<String>();
    private ArrayList<InsnModel> instructions = new ArrayList<>();

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

        for(AbstractInsnNode n : node.instructions) {
            instructions.add(new InsnModel(n));
        }
    }

    /**
     * @return method name
     */
    public String getName() {
        return node.name;
    }

    /**
     * @return method description
     */
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
            return type.substring(type.lastIndexOf("."));
        }
        else return type;
    }

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
     * @return the method's instructions
     */
    public ArrayList<InsnModel> getInstructions() {return this.instructions; }


}
