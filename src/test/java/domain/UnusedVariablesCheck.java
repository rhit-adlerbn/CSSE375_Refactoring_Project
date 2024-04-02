package domain;
import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.UnusedVariableCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnusedVariablesCheck {


    private String filePath = "src/test/resources/UnusedVariablesTest";

    ArrayList<ClassModel> classesUnderTest;
    {
        classesUnderTest = ASMAdapter.parseASM(filePath);
    }

    ClassModel Unused = classesUnderTest.get(0);
    ClassModel Used = classesUnderTest.get(1);
    @Test
    public void TestUnused() throws IOException {

        LintCheck check = new UnusedVariableCheck();
        ArrayList<ClassModel> classes = new ArrayList<ClassModel>();
        classes.add(Unused);
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        List<String> expected = new ArrayList<String>();

        expected.add("Unused Variable "+ "\""+"unused"+"\"." + " In method " + "doFunny");
        expected.add("Unused Variable "+ "\""+"notUsed"+"\"." + " In method " + "doFunny");

        Assert.assertEquals(expected, actual);



    }



    @Test
    public void TestUsed() throws IOException {

        LintCheck check = new UnusedVariableCheck();
        ArrayList<ClassModel> classes = new ArrayList<ClassModel>();
        classes.add(Used);
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        List<String> expected = new ArrayList<String>();

        expected.add("No unused Vars");


        Assert.assertEquals(expected, actual);



    }

}
