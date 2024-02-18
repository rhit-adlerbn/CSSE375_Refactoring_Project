package domain;

import datasource.ASMAdapter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingletionTest {
    private String filePath = "testclasses/singletonResources";

    ArrayList<ClassModel> classesUnderTest;
    {
        try {
            classesUnderTest = ASMAdapter.parseASM(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ClassModel singleton = classesUnderTest.remove(3);
    @Test
    public void singletonTest_notSingletons(){
        LintCheck check = new SingletonCheck();

        List<String> expected = new ArrayList<String>(Arrays.asList(
                "NotSingleton1 is not a Singleton",
                "NotSingleton2 is not a Singleton",
                "NotSingleton3 is not a Singleton"));
        List<String> actual = check.runLintCheck(classesUnderTest);
        assertEquals(expected, actual);
    }
    @Test
    public void namingTest_Singleton(){
        LintCheck check = new SingletonCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(singleton);

        List<String> expected = new ArrayList<String>(Arrays.asList("Singleton is a Singleton"));
        List<String> actual = check.runLintCheck(classes);
        assertEquals(expected, actual);
    }

}
