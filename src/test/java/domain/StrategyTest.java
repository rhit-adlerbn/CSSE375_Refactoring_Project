package domain;

import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.StrategyCheck;
import domain.model.ClassModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrategyTest {
    private final String filePath = "src/test/resources/strategyResources";
    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    ClassModel strategyFail = classesUnderTest.get(2);
    ClassModel strategyPass = classesUnderTest.get(3);

    @Test
    public void strategyTest_Fail(){
        LintCheck check = new StrategyCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(strategyFail);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Potential Strategy Pattern violation: exMethod in StrategyFail calls methods on concrete objects"));
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
            actual.add(res.toString());
        } 
        assertEquals(expected, actual);
    }

    @Test
    public void strategyTest_Pass(){
        LintCheck check = new StrategyCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(strategyPass);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "No Strategy Pattern violations detected."));
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
            actual.add(res.toString());
        } 
        assertEquals(expected, actual);
    }
}
