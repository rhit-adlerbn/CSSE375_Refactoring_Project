package domain.checks;

import domain.Result;
import domain.model.ClassModel;
import domain.model.LocalVarModel;
import domain.model.MethodModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipleLeastKnowledgeCheck implements LintCheck {
    private String testName = this.getClass().getSimpleName();
    @Override
    public List<Result> runLintCheck(List<ClassModel> classes) {
        List<Result> results = new ArrayList<>();
        for(ClassModel c : classes) {
            results.addAll(classLevelCheck(c, classes));
        }
        if(results.isEmpty()) results.add(new Result("All classes", testName,"No PLK violations detected."));
        return results;
    }

    private Collection<Result> classLevelCheck(ClassModel c, List<ClassModel> classes) {
        String className = c.getName();
        List<Result> violations = new ArrayList<>();
        List<MethodModel> methods = c.getMethods();
        for(MethodModel m : methods) {
            List<LocalVarModel> locVars = m.getLocalVars();
            for(LocalVarModel l : locVars) {
                if(!l.getName().equals("this") && !isUserDefinedType(l, classes)) 
                violations.add(new Result (className,testName, "Potential PLK violation: " + l.getName() + " is used but " +"is not a field of " + c.getName() + "."));
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
