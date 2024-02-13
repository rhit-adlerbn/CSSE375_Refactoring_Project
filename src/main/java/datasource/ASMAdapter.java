package datasource;

import domain.ClassModel;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ASMAdapter {

    public static void main (String[] args){


    }


    public static ArrayList<ClassModel> parseASM(String filePath) throws IOException {
        ArrayList<ClassModel> classes = new ArrayList<ClassModel>();
        File dir = new File(filePath);
        File[] files = dir.listFiles();



        if(files  == null)
            return null;
        for(File javaClass: files){
            String className =  dir.getName() + "." + javaClass.getName();

            if(className.endsWith("java")) {
               className = className.substring(0, className.length() - 5);

                ClassReader reader = new ClassReader(className);


                ClassNode classNode = new ClassNode();


                reader.accept(classNode, ClassReader.EXPAND_FRAMES);
                classes.add(new ClassModel(classNode));
            }
        }
        return classes;
    }


}
