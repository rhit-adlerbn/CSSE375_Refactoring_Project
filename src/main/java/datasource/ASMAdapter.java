package datasource;

import domain.ClassModel;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collections;

public class ASMAdapter {
    /**
     * Parses the .java files in a directory into a list of ClassModels
     * @param filePath the directory to parse from
     * @return a list of class models representing the java classes in the directory
     * @throws IOException if filepath is incorrect
     */
    public static ArrayList<ClassModel> parseASM(String filePath) throws IOException {
        ArrayList<ClassModel> classes = new ArrayList<ClassModel>();

        //Locates the folder by name
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = classLoader.getResource(filePath);
        if (resourceUrl == null) {
            throw new FileNotFoundException("Resource not found: " + filePath);
        }
        File dir = new File(resourceUrl.getFile());
        File[] files = dir.listFiles();





        if (files == null) {
            return null;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                try (InputStream inputStream = classLoader.getResourceAsStream(filePath + "/" + file.getName())) {
                    ClassReader reader = new ClassReader(inputStream);
                    ClassNode classNode = new ClassNode();
                    reader.accept(classNode, ClassReader.EXPAND_FRAMES);
                    classes.add(new ClassModel(classNode));
                }
            }
        }
        return classes;
    }


}
