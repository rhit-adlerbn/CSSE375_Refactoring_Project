package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ParameterNode;

import java.util.ArrayList;
import java.util.List;

public class MethodModel {
    private MethodNode node;
    private ArrayList<LocalVarModel> localVars = new ArrayList<LocalVarModel>();
    private ArrayList<ParamModel> params = new ArrayList<ParamModel>();

    public MethodModel(MethodNode node) {

        this.node = node;
        if(node.localVariables!=null) {
            for (LocalVariableNode lv : node.localVariables) {
                localVars.add(new LocalVarModel(lv));
            }
        }
        if(node.parameters != null) {
            for (ParameterNode p : node.parameters) {
                params.add(new ParamModel(p));
            }
        }
    }
    public String getName() {
        return node.name;
    }
    public String getDesc() {
        return node.desc;
    }
    public InsnList getInstructions(){
        return node.instructions;
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
    public List<LocalVarModel> getLocalVars() {
        return this.localVars;
    }
    public List<ParamModel> getParams() {
        return this.params;
    }
    private boolean isAccessModifier(int opCode){
        return (node.access & opCode) != 0;
    }

}
