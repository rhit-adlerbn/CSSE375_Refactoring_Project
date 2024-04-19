package datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
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
    ArrayList<ClassModel> classesToCheck = new ASMAdapter().parseASM(filePath);  

    @Test
    public void fileOut_SaveResults(){
        String savepath = "files/output.csv";
        List<Result> results = new SingletonCheck().runLintCheck(classesToCheck);
        FileOutput.saveResults(results,savepath);

        try (CSVReader actual = new CSVReader(new FileReader(savepath))) {

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
    @Test
    public void fileOut_SaveClass(){
        ClassModel classToSave = classesToCheck.get(0);
        String path = "src\\test\\resources\\FileOutResources\\NotSingleton1.class";
        byte[] expected = classToSave.toBytes();
        byte[] actual = new byte[expected.length];

        FileOutput.saveClass(expected, path);

        try(FileInputStream fis = new FileInputStream(path)){
            fis.read(actual);
            for(int i = 0; i< expected.length; i++){
                assertEquals(expected[i], actual[i]);
            } 
        }
        catch(Exception e){
            e.printStackTrace();
            assertTrue(false);
        }
    }
}