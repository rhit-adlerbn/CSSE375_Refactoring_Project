package domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Privatizer {

    /**
     *
     * @param classes a list of class models to lint over
     * @return list of Strings denoting variables that could be private,
     * but aren't
     */
    public void privatize(List<ClassModel> classes) {
        List<String> violations = new ArrayList<>();
        for(ClassModel c : classes) {
            violations.addAll(privatizeClass(c, classes));
        }
    }

    /**
     *
     * @param subject the class under examination
     * @param classes the full list of classes in the package
     * @return List of strings denoting any non-private variables in c that
     * are not accessed by any other classes
     */
    private void privatizeClass(ClassModel subject, List<ClassModel> classes) {
        Collection<String> violations = new ArrayList<>();
        List<ClassModel> allOtherClasses = new ArrayList<>(classes); allOtherClasses.remove(subject);
        List<FieldModel> fields = subject.getFields();

        for(FieldModel f : fields) {
            if(!f.isPrivate() && !isAccessed(f, subject, allOtherClasses)){

            }
        }
    }

    /**
     *
     * @param f the field in question
     * @param allOtherClasses
     * @return true iff the field f is accessed by at least one of allOtherClasses
     */
    public boolean isAccessed(FieldModel f, ClassModel subject, List<ClassModel> allOtherClasses) {
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

}
