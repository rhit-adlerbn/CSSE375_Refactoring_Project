package datasource;

import domain.model.ClassModel;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;

public class ASMAdapter {
    public ASMAdapter() {}

    /**
     * Parses the .class files in a directory into a list of ClassModels
     * @param folderPath the directory to parse from
     * @return a list of class models representing the java classes in the directory
     */
    public static ArrayList<ClassModel> parseASM(String folderPath){
        ArrayList<ClassModel> classes = new ArrayList<>();
        List<byte[]> bytecode = PackageLoader.loadPackage(folderPath);
        for(byte[] classInst : bytecode) {
            ClassReader classReader = new ClassReader(classInst); //create a class reader for the class
            ClassNode classNode = new ClassNode(Opcodes.ASM9); //create a node for the class
            classReader.accept(classNode, 0); //read the class into the node
            classes.add(new ClassModel(classNode)); //create new model from node
        }
        return classes;
    }


}
