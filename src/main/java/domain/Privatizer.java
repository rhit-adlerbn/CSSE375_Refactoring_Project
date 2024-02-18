package domain;

import java.util.ArrayList;
import java.util.List;

public class Privatizer {

    /**
     * For each class, makes all methods and fields private
     * that are not accessed by any outside class
     *
     * @param classes a list of class models to privatize
     */
    public void privatize(List<ClassModel> classes) {
        List<String> violations = new ArrayList<>();
        for(ClassModel c : classes) {
            privatizeClass(c, classes);
        }
    }

    /**
     *
     * @param subject the class under examination
     * @param classes the full list of classes in the package
     * privatizes all fields and methods of subject that aren't accessed
     * by any other class
     */
    private void privatizeClass(ClassModel subject, List<ClassModel> classes) {
        List<ClassModel> allOtherClasses = new ArrayList<>(classes); allOtherClasses.remove(subject);
        List<FieldModel> fields = subject.getFields();
        List<MethodModel> methods = subject.getMethods();

        for(FieldModel f : fields) {
            if(!f.isPrivate() && !isAccessedField(f, subject, allOtherClasses)) f.privatize();
        }

        for(MethodModel m : methods) {
            if(!m.isPrivate() && !isAccessedField(m, subject, allOtherClasses)) m.privatize();
        }
    }

    /**
     *
     * @param f the field in question
     * @param subject the owner of f
     * @param allOtherClasses
     * @return true iff the field f is accessed by at least one of allOtherClasses
     */
    private boolean isAccessedField(FieldModel f, ClassModel subject, List<ClassModel> allOtherClasses) {
        for(ClassModel c : allOtherClasses) {
            List<MethodModel> methods = c.getMethods();
            for(MethodModel m : methods) {
                InstructionModel instructions = m.getInstructions();
                int s = instructions.getSize();
                for(int i = 0; i < s; i++) {
                    AbstractInsnModel insn = instructions.get(i);
                    if(insn.isFieldInsn() && insn.getFieldInsnModel().matchesField(f, subject)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * @param m the method in question
     * @param subject the owner of m
     * @param allOtherClasses
     * @return true iff the method m is accessed by at least one of allOtherClasses
     */
    private boolean isAccessedMethod(MethodModel m, ClassModel subject, List<ClassModel> allOtherClasses) {
        for(ClassModel c : allOtherClasses) {
            List<MethodModel> methods = c.getMethods();
            for(MethodModel method : methods) {
                InstructionModel instructions = method.getInstructions();
                int s = instructions.getSize();
                for(int i = 0; i < s; i++) {
                    AbstractInsnModel insn = instructions.get(i);
                    if(insn.isMethodInsn() && insn.getMethodInsnModel().matchesMethod(m, subject)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
