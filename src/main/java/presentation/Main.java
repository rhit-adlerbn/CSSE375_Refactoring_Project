package presentation;

import datasource.ASMAdapter;
import domain.ClassModel;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<ClassModel> test = ASMAdapter.parseASM( "src\\main\\java\\domain");
        for(ClassModel javaClass: test){
            System.out.println(javaClass.getName());

        }

    }
}
