package datasource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.opencsv.CSVReader;

import domain.checks.LintCheck;
import domain.checks.SingletonCheck;
import domain.model.ClassModel;

public class FileOutTest {
    private final String filePath = "src/test/resources/singletonResources";
    ArrayList<ClassModel> classesToCheck = ASMAdapter.parseASM(filePath);
    LintCheck check = new SingletonCheck();
    String path = "files/output.csv";

    @Test
    public void namingTest_badConventions_expectWarning(){
        List<String> result = check.runLintCheck(classesToCheck);
        FileOutput.saveResults(result);

        try (CSVReader actual = new CSVReader(new FileReader(path))) {

            String[] expectedNextLine = {"NotSingleton1 is not a Singleton","NotSingleton2 is not a Singleton","NotSingleton3 is not a Singleton","Singleton is a Singleton"};
            String[] actualNextLine;

            for(String expected : expectedNextLine){
                actualNextLine = actual.readNext();
                assertTrue(expected.equals(actualNextLine[0]));
            }
        }
        catch(Exception e){
            assertTrue(false);
        }
    }
}