package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import datasource.ASMAdapter;
import domain.checks.AccessModifer;
import domain.model.ClassModel;
import domain.model.Model;

public class AccessModiferTest {
    private final String filePath = "src\\test\\resources\\AccessModiferResources";
    ASMAdapter asm = new ASMAdapter();
    ArrayList<ClassModel> classesUnderTest = asm.parseASM(filePath);
    AccessModifer acMod = new AccessModifer();

    @Test
    public void accessModefierTest_ClassAChanged(){
        //setup
        List<Model> fieldsAndMethods = new ArrayList<>();
        fieldsAndMethods.addAll(classesUnderTest.get(0).getFields());
        fieldsAndMethods.addAll(classesUnderTest.get(0).getMethods());
        List<Boolean> expectedIsPrivates = new ArrayList<>(Arrays.asList(true,false,true,false, false, true, true));
        List<String> expectedMsg = new ArrayList<>(Arrays.asList(
        "unusedField was changed from public to private",
        "unusedMethod was changed from public to private",
        "found no access modifers to change"));

        //run and parse results
        List<String> actualMsg = new ArrayList<>();
        for(Result res : acMod.runLintCheck(classesUnderTest)){
            actualMsg.add(res.toString());            
        } 
        
        //assert on results and if class was changed
        assertEquals(expectedMsg, actualMsg);
        for(int i = 0; i < fieldsAndMethods.size(); i++){
            assertEquals(fieldsAndMethods.get(i).isPrivate(),expectedIsPrivates.get(i));
        }
        
    }

}
