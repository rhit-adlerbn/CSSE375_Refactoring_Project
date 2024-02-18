package datasource;

import domain.ClassModel;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collections;

public class ASMAdapter {
    /**
     * Parses the .class files in a directory into a list of ClassModels
     * @param folderPath the directory to parse from
     * @return a list of class models representing the java classes in the directory
     */
    public static ArrayList<ClassModel> parseASM(String folderPath){
        ArrayList<ClassModel> classes = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderPath))) {
            for (Path path : directoryStream) {

                try {
                    byte[] bytecode = Files.readAllBytes(path); //get the .class bytecode
                    ClassReader classReader = new ClassReader(bytecode); //create a class reader for the class
                    ClassNode classNode = new ClassNode(Opcodes.ASM9); //create a node for the class
                    classReader.accept(classNode, 0); //read the class into the node
                    classes.add(new ClassModel(classNode)); //create new model from node
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }


}
