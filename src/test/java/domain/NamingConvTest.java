package domain;

import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.NamingConvCheck;
import domain.model.ClassModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NamingConvTest {
    private final String filePath = "src/test/resources/namingConvResources";
    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    ClassModel badConventions = classesUnderTest.get(0);
    ClassModel goodConventions = classesUnderTest.get(1);

    @Test
    public void namingTest_expectWarning(){
        LintCheck check = new NamingConvCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(badConventions);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Issue: badNamingClass is named incorrectly",
                "Issue: _do_something is named incorrectly",
                "Issue: VARNAME is named incorrectly"));
       List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        assertEquals(expected, actual);
    }
    @Test
    public void namingTest_expectNoWarning(){
        LintCheck check = new NamingConvCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(goodConventions);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "GoodNamingClass is named correctly",
                "doSomething is named correctly",
                "varName is named correctly"));
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
            actual.add(res.toString());
        } 
        assertEquals(expected, actual);
    }
}
