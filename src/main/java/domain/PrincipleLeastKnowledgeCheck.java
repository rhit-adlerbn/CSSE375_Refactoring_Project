package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipleLeastKnowledgeCheck implements LintCheck {

    private List<FieldModel> fields;
    @Override
    public List<String> runLintCheck(List<ClassModel> classes) {
        List<String> violations = new ArrayList<>();
        for(ClassModel c : classes) {
            violations.addAll(classLevelCheck(c, classes));
        }

        if(violations.isEmpty()) violations.add("No PLK violations detected.\n");
        return violations;
    }

    private Collection<String> classLevelCheck(ClassModel c, List<ClassModel> classes) {
        List<String> violations = new ArrayList<>();
        fields = c.getFields();
        List<MethodModel> methods = c.getMethods();
        for(MethodModel m : methods) {
            List<LocalVarModel> locVars = m.getLocalVars();
            for(LocalVarModel l : locVars) {
                if(!isAField(l, classes)) violations.add("Potential PLK violation: " + l.getDesc() + " is used but " +
                        "is not a field of " + c.getName() + ".\n");
            }
        }

        return violations;
    }

    private boolean isAField(LocalVarModel l, List<ClassModel> classes) {
        for(ClassModel c : classes) {

        }

        return false;
    }

}
