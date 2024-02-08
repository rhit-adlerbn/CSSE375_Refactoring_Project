package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrincipleLeastKnowledgeCheck {

    private List<FieldNode> fields;

    public List<String> check(String className) throws IOException {

        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        this.fields = getClassFields(classNode);

        List<String> violations = new ArrayList<>(checkMethods(classNode));

        if(violations.size() == 0) violations.add("No PLK violations detected.\n");

        return violations;
    }


    // Returns the list of fields of the ClassNode that are non-primitive
    public List<FieldNode> getClassFields(ClassNode classNode) {

        List<FieldNode> allFields = classNode.fields;
        List<FieldNode> classFields = new ArrayList<>();

        for(FieldNode f : allFields) {
            // checks for non-primitive types
            if(f.desc.startsWith("L") || f.desc.startsWith("[")) classFields.add(f);
        }

        return classFields;
    }


    private List<String> checkMethods(ClassNode classNode) {

        List<String> violations = new ArrayList<>();

        List<MethodNode> methods = classNode.methods;


        for(MethodNode m : methods) {
            violations.addAll(checkVars(m));
        }


        return violations;
    }

    private List<String> checkVars(MethodNode m) {
        List<String> violations = new ArrayList<>();

        List<LocalVariableNode> localVars = m.localVariables;

        for(LocalVariableNode l : localVars) {
            if(isAField(l)) {
                violations.add("Potential PLK violation: " + l.name + " is not a field.\n");
            }
        }

        return violations;
    }


    /**
     * Returns true iff the parameter type matches with
     * one of the class's field's types
     */
    private boolean isAField(LocalVariableNode l) {
        for(FieldNode f : this.fields) {
            if(f.desc.equals(l.desc) && (l.desc.startsWith("L") || l.desc.startsWith("["))) return true;
        }
        return false;
    }

}
