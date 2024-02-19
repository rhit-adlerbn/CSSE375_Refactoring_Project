package domain.checks;

import domain.model.ClassModel;
import domain.model.FieldModel;
import domain.model.LocalVarModel;
import domain.model.MethodModel;

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
                if(!l.getName().equals("this") && !isUserDefinedType(l, classes)) violations.add("Potential PLK violation: " + l.getName() + " is used but " +
                        "is not a field of " + c.getName() + ".\n");
            }
        }

        return violations;
    }

    /**
     * Used to determine if a local variable l is of type c,
     * where c can be any class in the "classes" list
     *
     * @param l the local variable in question
     * @param classes the list of available classes to examine
     * @return is l a user-defined type
     */
    private boolean isUserDefinedType(LocalVarModel l, List<ClassModel> classes) {
        for(ClassModel c : classes) {
            if(l.getDesc().equals(c.getName())) return true;
        }
        return false;
    }

}
