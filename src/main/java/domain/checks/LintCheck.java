package domain.checks;

import domain.Result;
import domain.model.ClassModel;

import java.util.List;

/**
 * Interface for individual linting checks
 */
public interface LintCheck {
    /**
     * Method to run a lint check on classes
     * @param classes a list of class models to lint over
     * @return a list of strings that hold the linting messages
     */
    List<Result> runLintCheck(List<ClassModel> classes);
}