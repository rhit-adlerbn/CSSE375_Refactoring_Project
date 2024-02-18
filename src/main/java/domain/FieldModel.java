package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

public class FieldModel {
    private final FieldNode node;
    public FieldModel(FieldNode node) {
        this.node = node;
    }
    /**
     * @return field name
     */
    public String getName() {
        return node.name;
    }
    /**
     * @return field description
     */
    public String getDesc() {
        return node.desc;
    }
    /**
     * Determines this fields type
     * @return the type of this field
     */
    public String getType() {
        String desc = node.desc;
        if(desc.contains("/")){
            return desc.substring(desc.lastIndexOf("/")+1, desc.indexOf(";"));
        }
        else return desc;}
    /**
     * @return is this field public
     */
    public boolean isPublic() {
        return isAccessModifier(Opcodes.ACC_PUBLIC);
    }
    /**
     * @return is this field private
     */
    public boolean isPrivate() {
        return isAccessModifier(Opcodes.ACC_PRIVATE);
    }
    /**
     * @return is this field protected
     */
    public boolean isProtected() {
        return isAccessModifier(Opcodes.ACC_PROTECTED);
    }
    /**
     * @return is this field static
     */
    public boolean isStatic() {
        return isAccessModifier(Opcodes.ACC_STATIC);
    }
    /**
     * @return is this field final
     */
    public boolean isFinal() {
        return isAccessModifier(Opcodes.ACC_FINAL);
    }

    public int getAccess(){
        return this.node.access;
    }

    public String getSignature(){return node.signature;}
    /**
     * Determines the access modifiers
     * @param opCode the ASM access flag
     * @return if the access modifier applies to this classes node
     */
    private boolean isAccessModifier(int opCode){
        return (node.access & opCode) != 0;
    }

    /**
     * @return is this field is primitive
     */
    private boolean isPrimitive() {return !(node.desc.startsWith("L") || node.desc.startsWith("["));}

}
