package domain;

import datasource.ASMAdapter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgramToInterfaceTest {
    private String filePath = "testclasses/programToInterfaceResources";

    ArrayList<ClassModel> classesUnderTest;
    {
        try {
            classesUnderTest = ASMAdapter.parseASM(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<ClassModel> pToImplementation = new ArrayList<>(classesUnderTest);
    ArrayList<ClassModel> pToInterface = new ArrayList<>(classesUnderTest);

    @Test
    public void interfaceTest_implementation_expectWarning(){
        LintCheck check = new ProgramToInterfaceCheck();
        pToImplementation.remove(3);
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(
                "Issue: Method <init> in class: ProgramedToImplementation expects a concrete implementation",
                "Issue: Method <init> in class: ProgramedToImplementation expects a concrete implementation",
                "Issue: Method doSomething in class: ProgramedToImplementation returns a concrete implementation",
                "Issue: Method doSomething in class: ProgramedToImplementation expects a concrete implementation",
                "Issue: Field abs in class: ProgramedToImplementation is a concrete instance",
                "Issue: Field in in class: ProgramedToImplementation is a concrete instance"));
        ArrayList<String> actual = check.runLintCheck(pToImplementation);
        assertEquals(expected, actual);
    }
    @Test
    public void interfaceTest_interface_expectNoWarning(){
        LintCheck check = new ProgramToInterfaceCheck();
        pToImplementation.remove(2);
        ArrayList<String> expected = new ArrayList<String>();
        ArrayList<String> actual = check.runLintCheck(pToImplementation);
        assertEquals(expected, actual);
    }

}
