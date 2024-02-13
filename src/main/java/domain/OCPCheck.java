package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class OCPCheck implements LintCheck{
    @Override
    public List<String> runLintCheck(List<ClassModel> classes) {
        List<String> returnStrings = new ArrayList<>();
        for (ClassModel classNode : classes) {
//            int access = classNode.getAccess();
            List<MethodModel> classMethods = classNode.getMethods();
            for (MethodModel method : classMethods) {
                if (method.isFinal()) {
                    returnStrings.add("Methods are final, so not open for extension. Potential violation of OCP in class " + classNode.getName());
                }
            }
//            for (FieldNode field : classNode.fields) {
//                if ((field.access & (Opcodes.ACC_FINAL)) != 0) {
//                    return "Methods are final, not open for mod
//                }
//            }
            if (classNode.isFinal()) {
                returnStrings.add("Class is final, so not open for extension. Potential violation of OCP in class " + classNode.getName());
            }
            returnStrings.add("OCP is held up in class " + classNode.getName());
        }

        return returnStrings;
    }
}
