package domain;

import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.ProgramToInterfaceCheck;
import domain.model.ClassModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgramToInterfaceTest {
    private final String filePath = "src/test/resources/programToInterfaceResources";
    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    ArrayList<ClassModel> pToImplementation = new ArrayList<>(classesUnderTest);
    ArrayList<ClassModel> pToInterface = new ArrayList<>(classesUnderTest);

    @Test
    public void interfaceTest_implementation_expectWarning(){
        LintCheck check = new ProgramToInterfaceCheck();
        pToImplementation.remove(3);
        List<String> expected = Arrays.asList(
                "Issue: Method <init> in class: ProgramedToImplementation expects a concrete implementation",
                "Issue: Method <init> in class: ProgramedToImplementation expects a concrete implementation",
                "Issue: Method doSomething in class: ProgramedToImplementation returns a concrete implementation",
                "Issue: Method doSomething in class: ProgramedToImplementation expects a concrete implementation",
                "Issue: Field abs in class: ProgramedToImplementation is a concrete instance",
                "Issue: Field in in class: ProgramedToImplementation is a concrete instance");
        List<String> actual = check.runLintCheck(pToImplementation);
        assertEquals(expected, actual);
    }
    @Test
    public void interfaceTest_interface_expectNoWarning(){
        LintCheck check = new ProgramToInterfaceCheck();
        pToInterface.remove(2);

        List<String> expected = new ArrayList<>();
        List<String> actual = check.runLintCheck(pToInterface);
        assertEquals(expected, actual);
    }

}
