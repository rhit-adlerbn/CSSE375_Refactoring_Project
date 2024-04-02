package domain;

import datasource.ASMAdapter;
import domain.checks.InterfaceCheck;
import domain.checks.LintCheck;
import domain.model.ClassModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterfaceTest {
    private final String filePath = "src/test/resources/InterfaceTests";
    ArrayList<ClassModel> classesUnderTest = ASMAdapter.parseASM(filePath);

    ClassModel implement = classesUnderTest.get(0);
    ClassModel notImplement = classesUnderTest.get(1);

    public InterfaceTest() throws IOException {
    }

    @Test
    public void interfaceTest_ExpectWarning(){
        LintCheck check = new InterfaceCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(notImplement);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Fail in classTest2. All methods in interface are not implemented. Missing these methods: [newTestMethod2, newTestMethod1, newTestMethod3]"));
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        assertEquals(expected, actual);
    }
    @Test
    public void interfaceTest_ExpectNoWarning(){
        LintCheck check = new InterfaceCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(implement);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Success in classTest1. All methods implemented from interface."));
       List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        assertEquals(expected, actual);
    }
}