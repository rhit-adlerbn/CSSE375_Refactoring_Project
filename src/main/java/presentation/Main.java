package presentation;

import datasource.ASMAdapter;
import domain.ClassModel;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<ClassModel> test= ASMAdapter.parseASM( "C:\\Users\\taskand\\IdeaProjects\\project-202420-s3-team15-202420\\src\\main\\java\\domain");
        for(ClassModel javaClass: test){
            System.out.println(javaClass.getName());

        }

    }
}
