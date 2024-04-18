package domain;

import datasource.ASMAdapter;
import domain.checks.ChatGPTCouplingCheck;
import domain.checks.ChatGPTSingletonCheck;
import domain.checks.ChatGptObserverCheck;
import domain.checks.LintCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class ChatGptObserverTest {
    private final String filePath = "src/test/resources/ObserverPatternCheck";

    private final boolean useChatGPTtests = false;

    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    @Test
    public void TestObserverOutput() {
        LintCheck check = new ChatGptObserverCheck();
        List<String> actual = new ArrayList<>();
        List<Result> results = check.runLintCheck(classesUnderTest);
        for(Result res : results){
            actual.add(res.toString());
        }

        System.out.println("ChatGPT gave " + actual.size() + " responses");
        for (int i=0; i<actual.size(); i++) {
            System.out.println("Response " + (i + 1) + ": " + actual.get(i));
        }
        Assert.assertEquals(actual.size(), classesUnderTest.size());

        if (useChatGPTtests) {
            ChatGPTTestHelper testHelper = new ChatGPTTestHelper();
            String filterPrompt = "Is the class a subject, and observer, or neither? " +
                    "Answer with subject, observer, or neither";
            // Class 0 is a subject
            Assert.assertTrue(testHelper.interpretResponse(results.get(0), filterPrompt, "subject"));
            // Class 1 is an observer
            Assert.assertTrue(testHelper.interpretResponse(results.get(1), filterPrompt, "observer"));
            // Class 2 is neither
            Assert.assertTrue(testHelper.interpretResponse(results.get(2), filterPrompt, "neither"));
        }
    }
}
