package domain;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class OCPCheck implements LintCheck{
    @Override
    public List<String> runLintCheck(List<ClassNode> classes) {
        List<String> returnStrings = new ArrayList<>();
        for (ClassNode classNode : classes) {
            int access = classNode.access;
            List<MethodNode> classMethods = classNode.methods;
            for (MethodNode method : classMethods) {
                if ((method.access & Opcodes.ACC_FINAL) != 0) {
                    returnStrings.add("Methods are final, so not open for extension. Potential violation of OCP in class " + classNode.name);
                }
            }
//            for (FieldNode field : classNode.fields) {
//                if ((field.access & (Opcodes.ACC_FINAL)) != 0) {
//                    return "Methods are final, not open for mod
//                }
//            }
            if ((access & Opcodes.ACC_FINAL) != 0) {
                returnStrings.add("Class is final, so not open for extension. Potential violation of OCP in class " + classNode.name);
            }
            returnStrings.add("OCP is held up in class " + classNode.name);
        }

        return returnStrings;
    }
}
