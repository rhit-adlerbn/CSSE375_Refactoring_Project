package domain;

import datasource.ASMAdapter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NamingConvTest {
    private String filePath = "testclasses/namingConvResources";

    ArrayList<ClassModel> classesUnderTest;
    {
        try {
            classesUnderTest = ASMAdapter.parseASM(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ClassModel badConventions = classesUnderTest.get(0);
    ClassModel goodConventions = classesUnderTest.get(1);

    @Test
    public void namingTest_badConventions_expectWarning(){
        LintCheck check = new NamingConvCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(badConventions);

        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(
                "Issue: badNamingClass is named incorrectly",
                "Issue: _do_something is named incorrectly",
                "Issue: VARNAME is named incorrectly"));
        ArrayList<String> actual = check.runLintCheck(classes);
        assertEquals(expected, actual);
    }
    @Test
    public void namingTest_goodConventions_expectNoWarning(){

    }
}
