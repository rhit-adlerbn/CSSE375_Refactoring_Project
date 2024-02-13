package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterfaceCheck implements LintCheck{
    @Override
    public List<String> runLintCheck(List<ClassNode> classes) {
        List<String> returnString = new ArrayList<>();
        for(ClassNode classNode: classes) {
            List<MethodNode> classMethods = classNode.methods;
            if (classNode.interfaces.isEmpty()) {
                returnString.add("Interface Check is not applicable in class " + classNode.name);
                continue;
            }
            String theInterface = classNode.interfaces.get(0);

            ClassReader reader = null;
            try {
                reader = new ClassReader(theInterface);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            ClassNode interfaceNode = new ClassNode();
            //get methods for the interface by defining a interface node
            reader.accept(interfaceNode, ClassReader.EXPAND_FRAMES);
            List<MethodNode> interfaceMethods = interfaceNode.methods;

            Set<String> classMethodNames = new HashSet<>();
            Set<String> interfaceMethodNames = new HashSet<>();

            for (MethodNode method : classMethods) {
                classMethodNames.add(method.name);
            }

            for (MethodNode method : interfaceMethods) {
                interfaceMethodNames.add(method.name);
            }

            boolean allMethodsImplemented = classMethodNames.containsAll(interfaceMethodNames);
            if (!allMethodsImplemented) {
                Set<String> missingMethods = new HashSet<>(interfaceMethodNames);
                missingMethods.removeAll(classMethodNames);

                returnString.add("Fail in " + classNode.name + ". All methods in interface are not implemented. Missing these methods: " + missingMethods);
            } else {

                returnString.add("Success in "  + classNode.name +  ". All methods implemented from interface.");
            }
        }



        return returnString;
    }
}
