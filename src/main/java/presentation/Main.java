package presentation;

import datasource.ASMAdapter;
import domain.ClassModel;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    /**
     * Main function, for now just parses and prints out the classes in domain
     * @param args unused
     * @throws IOException if parsing issue
     */
    public static void main(String[] args) throws IOException {
        ArrayList<ClassModel> test = ASMAdapter.parseASM("src\\main\\java\\domain");
        if(test !=null)
        for(ClassModel javaClass: test){
            System.out.println(javaClass.getName());

        }

    }
}
