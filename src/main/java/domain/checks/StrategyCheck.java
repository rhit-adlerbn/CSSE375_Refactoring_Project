package domain.checks;

import domain.Result;
import domain.model.ClassModel;
import domain.model.InstructionModel;
import domain.model.MethodInsnModel;
import domain.model.MethodModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StrategyCheck implements LintCheck {
    private String testName = this.getClass().getSimpleName();
    /**
     *
     * @param classes a list of class models to lint over
     * @return list of Strings denoting violations of Strategy Pattern
     *
     */
    @Override
    public List<Result> runLintCheck(List<ClassModel> classes) {
        List<Result> results = new ArrayList<>();

        for(ClassModel c : classes) {
            results.addAll(classLevelCheck(c));
        }

        if(results.isEmpty()) results.add(new Result("All classes",testName,"No Strategy Pattern violations detected."));
        return results;
    }

    private Collection<Result> classLevelCheck(ClassModel c) {
        List<Result> violations = new ArrayList<>();
        List<MethodModel> methods = c.getMethods();

        for(MethodModel m : methods) {
            if(m.getName().equals("<init>")) continue;
            InstructionModel instructions = m.getInstructions();
            if(violatesStrategyPattern(instructions)) {
                violations.add(new Result(c.getName(), testName, "Potential Strategy Pattern violation: " +
                        m.getName() + " in " + c.getName() + " calls methods on concrete objects"));
            }
        }

        return violations;
    }

    /**
     * @param instructions
     * @return whether the instructions violate the strategy pattern
     *
     * A violation is when there is only one method call in the instructions
     * and it is called on a concrete object instead of an interface.
     */
    private boolean violatesStrategyPattern(InstructionModel instructions) {
        List<MethodInsnModel> methodInstructions = new ArrayList<>();
        int s = instructions.getSize();
        for(int i = 0; i < s; i++) {
            if(instructions.get(i).isMethodInsn()) {
                methodInstructions.add(instructions.get(i).getMethodInsnModel());
            }
        }

        return methodInstructions.size() == 1 && !methodInstructions.get(0).isInterfaceMethod();
    }

}
