package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TemplateCheck implements LintCheck{

    @Override
    public List<String> runLintCheck(List<ClassNode> classes) {
        List<String> returnStrings = new ArrayList<>();
        for(ClassNode classNode: classes) {
            List<MethodNode> classMethods = classNode.methods;
            String abstractClass = classNode.superName;
            if (!abstractClass.equals("Abstraction")) {
                returnStrings.add("Does not implement template method in class " + classNode.name);
                continue;
            }
            List<String> requiredMethods = new ArrayList<>();
            requiredMethods.add("stepIfImplDifferBySubclassM1");
            requiredMethods.add("stepIfImplDifferBySubclassM2");
            Set<String> classMethodNames = new HashSet<>();


            for (MethodNode method : classMethods) {
                classMethodNames.add(method.name);
            }

            ClassReader reader = null;
            try {
                reader = new ClassReader(abstractClass);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ClassNode abstractNode = new ClassNode();
            //get methods for the abstract by defining an abstract node
            reader.accept(abstractNode, ClassReader.EXPAND_FRAMES);
            List<MethodNode> abstractMethods = abstractNode.methods;
            List<String> requiredAbstractMethods = new ArrayList<>();
            requiredAbstractMethods.add("stepIfImplDifferBySubclassM1");
            requiredAbstractMethods.add("stepIfImplDifferBySubclassM2");
            requiredAbstractMethods.add("runAlgorithm");
            requiredAbstractMethods.add("stepIfImplCommonToAllSubclass");
            requiredAbstractMethods.add("hookMethod");

            Set<String> abstractMethodNames = new HashSet<>();
            for (MethodNode method : abstractMethods) {
                abstractMethodNames.add(method.name);
            }


            if (!classMethodNames.containsAll(requiredMethods)) {

                returnStrings.add("Does not implement template pattern in class "  + classNode.name);
                continue;
            }
            if (!abstractMethodNames.containsAll(requiredAbstractMethods)) {
                returnStrings.add("Does not implement template pattern in class "  + classNode.name);
                continue;
            }
            returnStrings.add("Correctly implements Template pattern in class " + classNode.name);


        }

        return returnStrings;
    }

}
