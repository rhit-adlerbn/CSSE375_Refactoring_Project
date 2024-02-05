package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PatternCheck extends BasicLintern{



    /**
     * Reads in a list of Java Classes and prints fun facts about them.
     *
     * For more information, read: https://asm.ow2.io/asm4-guide.pdf
     *
     * @param args
     *            : the names of the classes, separated by spaces. For example:
     *            java example.MyFirstLinter java.lang.String
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException {

        for (String className : args) {

            ClassReader reader = new ClassReader(className);


            ClassNode classNode = new ClassNode();


            reader.accept(classNode, ClassReader.EXPAND_FRAMES);



            System.out.println(checkObserverPattern(classNode));


        }
    }




    private static String checkObserverPattern(ClassNode classNode){
        String returnValue = "Not Observer Pattern";
        if(implementsInterface(classNode, "Subject")) {
            returnValue = "Subject";
            ArrayList<String> requiredMethods = new ArrayList<String>();
            requiredMethods.add("subscribe");
            requiredMethods.add("unsubscribe");
            requiredMethods.add("Notify");
            if(!containsMethods(classNode, requiredMethods)){
                System.out.println("Attempting to use observer pattern but doesnt have all the required methods.");
                returnValue = "Failed Subject";
            }
            if (!(checkFields(classNode, "Observer"))) {
                System.out.println("Attempting to use observer pattern as a Subject but doesnt have a Observer instance.");
                returnValue = "Failed Subject";
            }
            return returnValue;
        } else if (implementsInterface(classNode, "Observer")){
            returnValue = "Observer";
            ArrayList<String> requiredMethods = new ArrayList<String>();
            requiredMethods.add("update");
            if(!containsMethods(classNode, requiredMethods)){
                System.out.println("Attempting to use observer pattern but doesnt have all the required methods.");
                returnValue = "Failed Observer";
            }
            if (!(checkFields(classNode, "Subject"))) {
                System.out.println("Attempting to use observer pattern as an Observer but doesnt have a Subject instance.");
                returnValue = "Failed Observer";
            }

        }
        return returnValue;

    }


}
