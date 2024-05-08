package domain;

import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.PrincipleLeastKnowledgeCheck;
import domain.model.ClassModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PLKTest {
    private final String filePath = "src/test/resources/plkResources";
    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    ClassModel plkHelper = classesUnderTest.get(0);
    ClassModel plkFail = classesUnderTest.get(1);
    ClassModel plkPass = classesUnderTest.get(2);
    @Test
    public void plkTest_Fail(){
        LintCheck check = new PrincipleLeastKnowledgeCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(plkFail);
        classes.add(plkHelper);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Potential PLK violation: p is used but is not a field of PLKFail."));
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        assertEquals(expected, actual);
    }

    @Test
    public void plkTest_Pass(){
        LintCheck check = new PrincipleLeastKnowledgeCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(plkPass);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "No PLK violations detected."));
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
            actual.add(res.toString());
        } 
        assertEquals(expected, actual);
    }
}


