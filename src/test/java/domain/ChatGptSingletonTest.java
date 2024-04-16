package domain;

import datasource.ASMAdapter;
import domain.checks.ChatGPTCouplingCheck;
import domain.checks.ChatGPTSingletonCheck;
import domain.checks.LintCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class ChatGptSingletonTest {
    private final String filePath = "src/test/resources/singletonResources";

    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    @Test
    public void TestSingletonOutput() {
        LintCheck check = new ChatGPTSingletonCheck();
        List<String> actual = new ArrayList<>();
        for(Result res : check.runLintCheck(classesUnderTest)){
            actual.add(res.toString());
        }

        System.out.println("ChatGPT gave " + actual.size() + " responses");
        for (int i=0; i<actual.size(); i++) {
            System.out.println("Response " + (i + 1) + ": " + actual.get(i));
        }
        Assert.assertEquals(actual.size(), classesUnderTest.size());
    }
}
