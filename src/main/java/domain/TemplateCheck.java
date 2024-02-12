package domain;

import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class TemplateCheck implements LintCheck{

    @Override
    public List<String> runLintCheck(List<ClassNode> classes) {
        return null;
    }

    @Override
    public String toString() {
        return "Template Pattern Check.";
    }
}
