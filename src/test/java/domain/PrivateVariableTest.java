package domain;

import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.PrivateVarCheck;
import domain.checks.StrategyCheck;
import domain.model.ClassModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrivateVariableTest {
    private final String filePath = "src/test/resources/privVarResources";
    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    ClassModel pvFail = classesUnderTest.get(0);
    ClassModel pvPass = classesUnderTest.get(1);

    @Test
    public void privVarTest_Fail(){
        LintCheck check = new PrivateVarCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(pvFail);
        classes.add(pvPass);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Field data from Class PVFail is not private, but is never accessed by another class.\n"));
        List<String> actual = check.runLintCheck(classes);
        assertEquals(expected, actual);
    }

    @Test
    public void privVarTest_Pass(){
        LintCheck check = new PrivateVarCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(pvPass);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "No private variable violations detected.\n"));
        List<String> actual = check.runLintCheck(classes);
        assertEquals(expected, actual);
    }
}
