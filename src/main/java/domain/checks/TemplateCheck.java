package domain.checks;

import domain.Result;
import domain.model.ClassModel;
import domain.model.MethodModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TemplateCheck implements LintCheck{
    private final Set<String> REQ_METHODS = new HashSet<>(Arrays.asList("stepIfImplDifferBySubclassM1","stepIfImplDifferBySubclassM2"));
    private final Set<String> REQ_ABS = new HashSet<>(Arrays.asList("runAlgorithm","stepIfImplDifferBySubclassM1","stepIfImplDifferBySubclassM2","stepIfImplCommonToAllSubclass","hookMethod"));

    /**
     * Checks for the template pattern in a given class
     * @param classes a list of class models to lint over
     * @return A list of Strings to display to the user whether the check passed.
     */

    @Override
    public List<Result> runLintCheck(List<ClassModel> classes) {
        List<Result> results = new ArrayList<>();
        for(ClassModel classNode: classes) {
            results.add(new Result(classNode.getName(), this.getClass().getCanonicalName(), isTemplatePattern(classNode)));
        }
        return results;
    }

    private String isTemplatePattern(ClassModel classNode) {
        Set<String> abstractMethodNames = new HashSet<>();
        Set<String> classMethodNames = new HashSet<>();

        for (MethodModel method : classNode.getMethods()) {
            classMethodNames.add(method.getName());
        }
        for (MethodModel method : classNode.getAbstractMethods()) {
            abstractMethodNames.add(method.getName());
        }

        if (!classNode.getSuperName().equals("Abstraction")) {
            return "Does not implement template method in class " + classNode.getName() + ". Does not extend abstract class Abstraction";
        }else if (!classMethodNames.containsAll(REQ_METHODS)) {
            return "Does not implement template pattern in class "  + classNode.getName() + ". Does not have all required methods";
        }else if (!abstractMethodNames.containsAll(REQ_ABS)) {
           return "Does not implement template pattern in class "  + classNode.getName() + ". Does not have all required abstract class methods";
        }else return "Correctly implements Template pattern in class " + classNode.getName();
    }

}

