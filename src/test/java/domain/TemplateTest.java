package domain;

import datasource.ASMAdapter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemplateTest {
    private final String filePath = "src/test/resources/TemplateTests";
    ArrayList<ClassModel> classesUnderTest = ASMAdapter.parseASM(filePath);

    ClassModel notWork = classesUnderTest.get(0);
    ClassModel work= classesUnderTest.get(1);

    public TemplateTest() throws IOException {
    }

    @Test
    public void TemplateTest_ExpectWarning(){
        LintCheck check = new TemplateCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(notWork);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Does not implement template method in class Abstraction. Does not extend abstract class Abstraction"));
        List<String> actual = check.runLintCheck(classes);
        assertEquals(expected, actual);
    }
    @Test
    public void TemplateTest_ExpectNoWarning(){
        LintCheck check = new TemplateCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(work);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Correctly implements Template pattern in class ConcreteClassOne"));
        List<String> actual = check.runLintCheck(classes);
        assertEquals(expected, actual);
    }
}