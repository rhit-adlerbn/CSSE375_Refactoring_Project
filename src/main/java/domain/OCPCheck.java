package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class OCPCheck implements LintCheck{

    /**
     * Checks a class to see if the Open-Closed Principle is held up
     * @param classes a list of class models to lint over
     * @return A list of Strings to display to the user whether the check passed.
     */
    @Override
    public List<String> runLintCheck(List<ClassModel> classes) {
        List<String> returnStrings = new ArrayList<>();
        int check = 0;
        for (ClassModel classNode : classes) {
            check = 0;

            List<MethodModel> classMethods = classNode.getMethods();
            for (MethodModel method : classMethods) {
                if (method.isFinal()) {
                    returnStrings.add("Methods are final, so not open for extension. Potential violation of OCP in class " + classNode.getName());
                    check = 1;
                    continue;
                }
            }
            if(check == 1){
                continue;
            }

            if (classNode.isFinal()) {
                returnStrings.add("Class is final, so not open for extension. Potential violation of OCP in class " + classNode.getName());
                continue;
            }
            returnStrings.add("OCP is held up in class " + classNode.getName());
        }

        return returnStrings;
    }
}
