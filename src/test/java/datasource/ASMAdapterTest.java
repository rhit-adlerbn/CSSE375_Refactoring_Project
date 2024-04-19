package datasource;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import domain.model.ClassModel;

public class ASMAdapterTest {

    

    @Test
    public void parseASM_ValidPath_ExpectSuccess(){
        String filePath = "src\\test\\resources\\Adapter";
        int expLen = 1;
        String expName = "Test1";

        ASMAdapter adapter = new ASMAdapter();
        ArrayList<ClassModel> classes = adapter.parseASM(filePath);

        assertEquals(expLen, classes.size());
        assertEquals(expName, classes.get(0).getName());
        
    }
}
