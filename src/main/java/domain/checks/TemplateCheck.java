package domain.checks;

import domain.model.ClassModel;

import java.util.List;

public class TemplateCheck implements LintCheck{

    @Override
    public List<String> runLintCheck(List<ClassModel> classes) {
        return null;
    }

    @Override
    public String toString() {
        return "Template Pattern Check.";
    }
}