package domain;

import datasource.ASMAdapter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NamingConvTest {
    private final String filePath = "src/test/resources/namingConvResources";
    ArrayList<ClassModel> classesUnderTest = ASMAdapter.parseASM(filePath);

    ClassModel badConventions = classesUnderTest.get(0);
    ClassModel goodConventions = classesUnderTest.get(1);

    @Test
    public void namingTest_badConventions_expectWarning(){
        LintCheck check = new NamingConvCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(badConventions);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Issue: badNamingClass is named incorrectly",
                "Issue: _do_something is named incorrectly",
                "Issue: VARNAME is named incorrectly"));
        List<String> actual = check.runLintCheck(classes);
        assertEquals(expected, actual);
    }
    @Test
    public void namingTest_goodConventions_expectNoWarning(){
        LintCheck check = new NamingConvCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(goodConventions);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "GoodNamingClass is named correctly",
                "doSomething is named correctly",
                "varName is named correctly"));
        List<String> actual = check.runLintCheck(classes);
        assertEquals(expected, actual);
    }
}
