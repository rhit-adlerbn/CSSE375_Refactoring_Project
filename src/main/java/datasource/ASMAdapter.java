package datasource;

import domain.ClassModel;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ASMAdapter {
    /**
     * Parses the .java files in a directory into a list of ClassModels
     * @param filePath the directory to parse from
     * @return a list of class models representing the java classes in the directory
     * @throws IOException if filepath is incorrect
     */
    public static ArrayList<ClassModel> parseASM(String filePath) throws IOException {
        ArrayList<ClassModel> classes = new ArrayList<ClassModel>();
        File dir = new File(filePath);
        File[] files = dir.listFiles();

        System.out.println(filePath);

        if(files  == null)
            return null;
        for(File javaClass: files){
            String className =  dir.getName() + "." + javaClass.getName();
            System.out.println(className);
            if(className.endsWith("java")) {
               className = className.substring(0, className.length() - 5);
                System.out.println(className);

                //InputStream inputStream = Files.newInputStream(Paths.get(filePath));

                ClassReader reader = new ClassReader(className);


                ClassNode classNode = new ClassNode();


                reader.accept(classNode, ClassReader.EXPAND_FRAMES);
                classes.add(new ClassModel(classNode));
            }
        }
        return classes;
    }


}
