package domain;

import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.TemplateCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplateTest {
    private final String filePath = "src/test/resources/TemplateClasses";
    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

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
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        Assert.assertEquals(expected, actual);
    }
    @Test
    public void TemplateTest_ExpectNoWarning(){
        LintCheck check = new TemplateCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(work);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Correctly implements Template pattern in class ConcreteClassOne"));
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
            actual.add(res.toString());
        } 
        Assert.assertEquals(expected, actual);
    }
}