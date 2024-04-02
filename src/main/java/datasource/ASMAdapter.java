package datasource;

import domain.model.ClassModel;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ASMAdapter {
    public ASMAdapter() {
    }

    /**
     * Parses the .class files in a directory into a list of ClassModels
     *
     * @param folderPath the directory to parse from
     * @return a list of class models representing the java classes in the directory
     */
    public ArrayList<ClassModel> parseASM(String folderPath) {
        ArrayList<ClassModel> classes = new ArrayList<>();
        List<byte[]> bytecode = PackageLoader.loadPackage(folderPath);
        for (byte[] classInst : bytecode) {
            ClassNode classNode = this.readClassNode(classInst);
            String representation = this.readTextRepresentation(classInst);
            classes.add(new ClassModel(classNode, representation)); //create new model from node
        }
        return classes;
    }

    /**
     * Read ClassNode from bytecode
     * @param classInst the class bytecode
     * @return the ClassNode representation of the class
     */
    private ClassNode readClassNode(byte[] classInst) {
        ClassReader classReader = new ClassReader(classInst); //create a class reader for the class
        classReader.accept(new TraceClassVisitor(null, new Textifier(), null), 0);
        ClassNode classNode = new ClassNode(Opcodes.ASM9); //create a node for the class
        classReader.accept(classNode, 0); //read the class into the node
        return classNode;
    }

    /**
     * Read text from bytecode
     * @param classInst the class bytecode
     * @return the String representation of the class
     */
    private String readTextRepresentation(byte[] classInst) {
        ClassReader classReader = new ClassReader(classInst); //create a class reader for the class
        Textifier textifier = new Textifier();
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, textifier, printWriter);
        classReader.accept(traceClassVisitor, ClassReader.EXPAND_FRAMES);
        return writer.toString();
    }
}
