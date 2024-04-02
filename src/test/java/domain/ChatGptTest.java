package domain;
import datasource.ASMAdapter;
import domain.checks.ChatGPTCheck;
import domain.checks.CouplingCheck;
import domain.checks.LintCheck;
import domain.model.ClassModel;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatGptTest {


    private final String filePath = "src/test/resources/CouplingTests";

    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);

    @Test
    public void TestCouplingOutput() throws IOException {
        LintCheck check = new ChatGPTCheck();
        List<String> actual = check.runLintCheck(classesUnderTest);

        // ChatGPT output is nondeterministic, so we can only assert the trivial nonempty case
        // The print statement shows how many responses we received (expect 3, one for each class)
        System.out.println("ChatGPT gave " + actual.size() + " responses");
        for (int i=0; i<actual.size(); i++) {
            System.out.println("Response " + (i + 1) + ": " + actual.get(i));
        }
        Assert.assertFalse(actual.isEmpty());
    }
}
