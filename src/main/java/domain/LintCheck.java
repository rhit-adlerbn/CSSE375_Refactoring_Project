package domain;

import org.objectweb.asm.tree.ClassNode;

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
    List<String> runLintCheck(List<ClassModel> classes);
}