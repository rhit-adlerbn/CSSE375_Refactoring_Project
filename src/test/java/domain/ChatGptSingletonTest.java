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

    private final boolean useChatGPTtests = false;

    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    @Test
    public void TestSingletonOutput() {
        LintCheck check = new ChatGPTSingletonCheck();
        List<String> actual = new ArrayList<>();
        List<Result> results = check.runLintCheck(classesUnderTest);
        for(Result res : results){
            actual.add(res.toString());
        }

        System.out.println("ChatGPT gave " + actual.size() + " responses");
        for (int i=0; i<actual.size(); i++) {
            System.out.println("Response " + (i + 1) + ": " + actual.get(i));
        }

        // NotSingleton2 cannot be read by ChatGPT
        Assert.assertEquals(actual.size(), classesUnderTest.size() - 1);

        if (useChatGPTtests) {
            ChatGPTTestHelper testHelper = new ChatGPTTestHelper();
            String filterPrompt = "Is the class a singleton? Answer with yes or no";
            // Class 0 is not a singleton
            Assert.assertTrue(testHelper.interpretResponse(results.get(0), filterPrompt, "no"));
            // Class 1 is not a singleton
            Assert.assertTrue(testHelper.interpretResponse(results.get(1), filterPrompt, "no"));
            // Class 3 is a singleton
            Assert.assertTrue(testHelper.interpretResponse(results.get(2), filterPrompt, "yes"));
        }
    }
}
