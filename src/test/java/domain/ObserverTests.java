package domain;
import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.ObserverPatternCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObserverTests {


    private String filePath = "src/test/resources/ObserverPatternCheck";

    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    ClassModel subject = classesUnderTest.get(0);
    ClassModel observer = classesUnderTest.get(1);
    ClassModel nothing = classesUnderTest.get(2);

    @Test
    public void TestObserver() throws IOException {

        LintCheck check = new ObserverPatternCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(observer);
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        List<String> expected = new ArrayList<String>();
        //expected.add("Subject");
        expected.add("Observer");
        //expected.add("Not Observer Pattern");
        Assert.assertEquals(expected, actual);
    }
    @Test
    public void TestSubject() throws IOException {

        LintCheck check = new ObserverPatternCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(subject);
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        List<String> expected = new ArrayList<String>();
        expected.add("Subject");
        //expected.add("Observer");
        //expected.add("Not Observer Pattern");
        Assert.assertEquals(expected, actual);
    }
    @Test
    public void TestNothing() throws IOException {

        LintCheck check = new ObserverPatternCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(nothing);
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        List<String> expected = new ArrayList<String>();
        //expected.add("Subject");
        //expected.add("Observer");
        expected.add("Not Observer Pattern");
        Assert.assertEquals(expected, actual);
    }


}
