package domain;

import datasource.ASMAdapter;
import domain.checks.LintCheck;
import domain.checks.OCPCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import static org.junit.jupiter.api.Assertions.assertEquals;

public class OCPTest {
    private final String filePath = "src/test/resources/OCPTests";
    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    ClassModel notWork = classesUnderTest.get(0);
    ClassModel notWork2 = classesUnderTest.get(1);
    ClassModel work = classesUnderTest.get(2);


    public OCPTest() throws IOException {
    }

    @Test
    public void OCPTest_ExpectWarning(){
        LintCheck check = new OCPCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(notWork);

        List<String> expected = new ArrayList<>();
        expected.add("Methods are final, so not open for extension. Potential violation of OCP in class AnotherBadOne");
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void OCPTest_ExpectWarning2(){
        LintCheck check = new OCPCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(notWork2);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "Class is final, so not open for extension. Potential violation of OCP in class BadOne"));
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        Assert.assertEquals(expected, actual);
    }
    @Test
    public void OCPTest_ExpectNoWarning(){
        LintCheck check = new OCPCheck();
        ArrayList<ClassModel> classes = new ArrayList<>();
        classes.add(work);

        List<String> expected = new ArrayList<>(Arrays.asList(
                "OCP is held up in class GoodOne"));
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classes)){
           actual.add(res.toString());
        } 
        Assert.assertEquals(expected, actual);
    }
}