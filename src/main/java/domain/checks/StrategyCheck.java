package domain.checks;

import domain.model.ClassModel;
import domain.model.InstructionModel;
import domain.model.MethodInsnModel;
import domain.model.MethodModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StrategyCheck implements LintCheck {

    /**
     *
     * @param classes a list of class models to lint over
     * @return list of Strings denoting violations of Strategy Pattern
     *
     */
    @Override
    public List<String> runLintCheck(List<ClassModel> classes) {
        List<String> violations = new ArrayList<>();

        for(ClassModel c : classes) {
            violations.addAll(classLevelCheck(c));
        }

        if(violations.isEmpty()) violations.add("No Strategy Pattern violations detected.\n");
        return violations;
    }

    private Collection<String> classLevelCheck(ClassModel c) {
        List<String> violations = new ArrayList<>();
        List<MethodModel> methods = c.getMethods();

        for(MethodModel m : methods) {
            InstructionModel instructions = m.getInstructions();
            if(violatesStrategyPattern(instructions)) {
                violations.add("Potential Strategy Pattern violation: " +
                        m.getName() + " in " + c.getName() + " calls methods on concrete objects\n");
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
