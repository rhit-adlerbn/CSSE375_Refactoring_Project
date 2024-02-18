package domain;

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


        return violations;
    }

}
