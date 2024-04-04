package domain.checks;

import domain.Result;
import domain.model.ClassModel;
import domain.model.MethodModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterfaceCheck implements LintCheck{
    /**
     * if an abstract class implements an interface, it checks for if all the methods in the interface get put into
     * the abstract class.
     * @param classes a list of class models to lint over
     * @return A list of Strings to display to the user whether the check passed.
     */
    @Override
    public List<Result> runLintCheck(List<ClassModel> classes) {
        List<Result> results = new ArrayList<>();
        for(ClassModel classNode: classes) {
            List<MethodModel> classMethods = classNode.getMethods();
            if (classNode.getInterfaces().isEmpty()) {
                String result = "Interface Check is not applicable";
                Result res = new Result(classNode.getName(), this.getClass().getSimpleName(),result);
                results.add(res);
                continue;
            }
            else{
                String result = checkForImplementation(classNode, classMethods);
                Result res = new Result(classNode.getName(), this.getClass().getSimpleName(),result);
                results.add(res);
            }
        }
        return results;
    }
    
    private String checkForImplementation(ClassModel classNode, List<MethodModel> classMethods) {
        Set<String> classMethodNames = new HashSet<>();
        Set<String> interfaceMethodNames = new HashSet<>(classNode.getInterfaceMethods());

        for (MethodModel method : classMethods) {
            classMethodNames.add(method.getName());
        }
        boolean allMethodsImplemented = classMethodNames.containsAll(interfaceMethodNames);
        if (!allMethodsImplemented) {
            Set<String> missingMethods = new HashSet<>(interfaceMethodNames);
            missingMethods.removeAll(classMethodNames);
            return "Fail in " + classNode.getName() + ". All methods in interface are not implemented. Missing these methods: " + missingMethods;
        } else {

            return "Success in "  + classNode.getName() +  ". All methods implemented from interface.";
        }
    }
    
}
