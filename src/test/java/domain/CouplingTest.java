package domain;
import datasource.ASMAdapter;
import domain.checks.CouplingCheck;
import domain.checks.LintCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CouplingTest {


    private String filePath = "src/test/resources/CouplingTests";

    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    ClassModel none = classesUnderTest.get(0);
    ClassModel some = classesUnderTest.get(1);
    ClassModel high = classesUnderTest.get(2);

    @Test
    public void TestNoCoupling() throws IOException {

        LintCheck check = new CouplingCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(none);
        List<String> actual = check.runLintCheck(classes);
        List<String> expected = new ArrayList<String>();
        //expected.add("Subject");
        expected.add("0.0");
        //expected.add("Not Observer Pattern");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void TestSomeCoupling() throws IOException {

        LintCheck check = new CouplingCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(some);
        List<String> actual = check.runLintCheck(classes);
        List<String> expected = new ArrayList<String>();
        //expected.add("Subject");
        expected.add("2.4");
        //expected.add("Not Observer Pattern");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void TestHighCoupling() throws IOException {

        LintCheck check = new CouplingCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(high);
        List<String> actual = check.runLintCheck(classes);
        List<String> expected = new ArrayList<String>();
        //expected.add("Subject");
        expected.add("10.945599999999999");
        //expected.add("Not Observer Pattern");
        Assert.assertEquals(expected, actual);
    }




}
