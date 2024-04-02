package domain;
import datasource.ASMAdapter;
import domain.checks.LintCheck;


import domain.checks.PlantUml;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlantUMLTest {


    private String filePath = "src/test/resources/PlantUML";

    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    ClassModel Simple = classesUnderTest.get(0);
    ClassModel second = classesUnderTest.get(1);
    ClassModel third = classesUnderTest.get(2);

    @Test
    public void TestSimple() throws IOException {

        LintCheck check = new PlantUml();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(Simple);
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        List<String> expected = new ArrayList<String>();
        //expected.add("Subject");
        expected.add("+class Test1{\n-First: int\n-Second: int\n}\n");
        //expected.add("Not Observer Pattern");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void TestExternal() throws IOException {

        LintCheck check = new PlantUml();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(second);
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        List<String> expected = new ArrayList<String>();

        expected.add("+class Test2{\n-name: double\n}\nTest2-->Test1\n");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void TestComplex() throws IOException {

        LintCheck check = new PlantUml();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(third);
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        List<String> expected = new ArrayList<String>();

        expected.add("+class Test3{\n-Method1(arg1):void\n+Method2(arg1,arg2):String\n}\nTest3-->Test2\nTest3-->Test1\n");

        Assert.assertEquals(expected, actual);
    }




}
