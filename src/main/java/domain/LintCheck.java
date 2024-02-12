package domain;

import org.objectweb.asm.tree.ClassNode;

import java.util.List;

/**
 * Interface for individual linting checks
 */
public interface LintCheck {
    /**
     * BE AWARE: "classes" will change types to a list of classNodeAdapters in the future
     * BE AWARE: make sure if you implement this before the change any code that deals with classNodes is easy to change
     * Method to run a lint check on classes
     * @param classes a list of class nodes to lint over
     * @return a list of strings that hold the linting messages
     */
    List<String> runLintCheck(List<ClassNode> classes);
    String toString();
}