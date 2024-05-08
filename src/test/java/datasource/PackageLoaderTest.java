package datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import domain.model.ClassModel;

public class PackageLoaderTest {

    @Test
    public void loadPackage_ExpectSuccess(){
        String filePath = "src\\test\\resources\\Adapter";
        
        List<byte[]> bytes = new ArrayList<>();
        
        try{ bytes = PackageLoader.loadPackage(filePath);}
        catch(Exception e ){
            assertTrue(false);
        }
        assertFalse(bytes.isEmpty());
       
    }
    @Test
    public void loadPackage_ExpectError(){
        String filePath = "src\\test\\resources\\nonexistantfolder";
        
        List<byte[]> bytes = new ArrayList<>();
        
        try{ bytes = PackageLoader.loadPackage(filePath);}
        catch(IOException e ){
            assertTrue(true);
        }
        assertTrue(bytes.isEmpty());
       
    }
}
