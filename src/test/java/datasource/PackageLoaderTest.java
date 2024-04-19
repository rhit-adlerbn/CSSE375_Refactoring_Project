package datasource;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import domain.model.ClassModel;

public class PackageLoaderTest {

    @Test
    public void parseASM_ValidPath_ExpectSuccess(){
        String filePath = "src\\test\\resources\\Adapter";
    
        
        List<byte[]> bytes = PackageLoader.loadPackage(filePath);

        for(byte[] b : bytes){
            for(int i = 0; i<b.length ; i++)
            System.out.println(b[i]);
        }
    }
}
