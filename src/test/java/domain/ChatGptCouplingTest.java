package domain;

import datasource.ASMAdapter;
import domain.checks.ChatGPTCouplingCheck;
import domain.checks.ChatGPTSingletonCheck;
import domain.checks.ChatGptObserverCheck;
import domain.checks.LintCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatGptCouplingTest {
    private final String filePath = "src/test/resources/CouplingTests";
    private final String keyPath = "files/key" + ".txt";
    private final boolean useChatGPTtests = false;

    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    @Test
    public void TestCouplingOutput() {
        File keyFile = new File(keyPath);

        LintCheck check = null;
        try {
            Scanner scanner = new Scanner(keyFile);
            String line = scanner.nextLine();
            if (line.startsWith("sk-")) {
                check = new ChatGPTCouplingCheck(line);
            } else {
                System.err.println("No ChatGPT key file found!");
                throw new RuntimeException();
            }
        } catch (FileNotFoundException e) {
            // We can run without keyfile existing
            System.err.println("No ChatGPT key file found!");
            throw new RuntimeException(e);
        }

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
            String filterPrompt = "Does the class exhibit low, moderate, or high coupling. " +
                    "Answer with only low, moderate, or high";
            // Class 0 has low coupling
            Assert.assertTrue(testHelper.interpretResponse(results.get(0), filterPrompt, "low"));
            // Class 1 has low-medium coupling
            Assert.assertTrue(testHelper.interpretResponse(results.get(1), filterPrompt, "low") ||
                    testHelper.interpretResponse(results.get(1), filterPrompt, "moderate"));
            // Class 2 has medium-high coupling
            Assert.assertTrue(testHelper.interpretResponse(results.get(1), filterPrompt, "moderate") ||
                    testHelper.interpretResponse(results.get(2), filterPrompt, "high"));
        }
    }
}
