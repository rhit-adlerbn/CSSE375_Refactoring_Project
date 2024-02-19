package domain.checks;

import domain.model.ClassModel;
import domain.model.MethodModel;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
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
    public List<String> runLintCheck(List<ClassModel> classes) {
        List<String> returnString = new ArrayList<>();
        for(ClassModel classNode: classes) {
            List<MethodModel> classMethods = classNode.getMethods();
            if (classNode.getInterfaces().isEmpty()) {
                returnString.add("Interface Check is not applicable" + classNode.getName());
                continue;
            }

            Set<String> classMethodNames = new HashSet<>();


            for (MethodModel method : classMethods) {
                classMethodNames.add(method.getName());
            }

            Set<String> interfaceMethodNames = new HashSet<>(classNode.getInterfaceMethods());
            System.out.println(interfaceMethodNames);

            boolean allMethodsImplemented = classMethodNames.containsAll(interfaceMethodNames);
            if (!allMethodsImplemented) {
                Set<String> missingMethods = new HashSet<>(interfaceMethodNames);
                missingMethods.removeAll(classMethodNames);

                returnString.add("Fail in " + classNode.getName() + ". All methods in interface are not implemented. Missing these methods: " + missingMethods);
            } else {

                returnString.add("Success in "  + classNode.getName() +  ". All methods implemented from interface.");
            }
        }



        return returnString;
    }
}
