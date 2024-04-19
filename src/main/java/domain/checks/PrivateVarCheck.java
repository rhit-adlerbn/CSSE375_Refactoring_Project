package domain.checks;

import domain.Result;
import domain.model.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrivateVarCheck implements LintCheck {
    private String testName = this.getClass().getSimpleName(); 
    /**
     *
     * @param classes a list of class models to lint over
     * @return list of Strings denoting variables that could be private,
     * but aren't
     */
    @Override
    public List<Result> runLintCheck(List<ClassModel> classes) {
        List<Result> results = new ArrayList<>();
        for(ClassModel c : classes) {
            results.addAll(findViolations(c, classes));
        }

        if(results.isEmpty()) results.add( new Result("All Classes", testName,"No private variable violations detected.\n"));
        return results;
    }

    /**
     *
     * @param subject the class under examination
     * @param classes the full list of classes in the package
     * @return List of strings denoting any non-private variables in c that
     * are not accessed by any other classes
     */
    private Collection<Result> findViolations(ClassModel subject, List<ClassModel> classes) {
        String className = subject.getName();
        Collection<Result> violations = new ArrayList<>();
        List<ClassModel> allOtherClasses = new ArrayList<>(classes); allOtherClasses.remove(subject);
        List<FieldModel> fields = subject.getFields();

        for(FieldModel f : fields) {
            if(!f.isPrivate() && !isAccessed(f, subject, allOtherClasses)){
                String msg = "Field " + f.getName() + " from Class " + subject.getName()
                + " is not private, but is never accessed by another class.\n";
                violations.add(new Result(className,testName, msg));
            }
        }
        return violations;
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
                    if(insn.isFieldInsn() && insn.getFieldInsnModel().matches(f, subject)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


}
