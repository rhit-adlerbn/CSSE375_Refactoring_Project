package datasource;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import domain.Result;

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
        List<Result> results = check.runLintCheck(classesToCheck);
        FileOutput.saveResults(results);

        try (CSVReader actual = new CSVReader(new FileReader(path))) {

           List<String[]> expectedLines = new ArrayList<>();
           expectedLines.add(new String[] {"Class tested","Test ran","Output"});
           expectedLines.add(new String[] {"NotSingleton1","SingletonCheck","NotSingleton1 is not a Singleton"});
           expectedLines.add(new String[] {"NotSingleton2","SingletonCheck","NotSingleton2 is not a Singleton"});
           expectedLines.add(new String[] {"NotSingleton3","SingletonCheck","NotSingleton3 is not a Singleton"});
           expectedLines.add(new String[] {"Singleton","SingletonCheck","Singleton is a Singleton"});
            
            List<String[]> actualLines = actual.readAll();

            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 3; j++){
                    assertTrue(actualLines.get(i)[j].equals(expectedLines.get(i)[j]));
                }
            }
               
        }
        catch(Exception e){
            e.printStackTrace();
            assertTrue(false);
        }
    }
}