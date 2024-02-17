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
            System.out.println(checkCoupling(classNode));
            System.out.println(CreateUML(classNode));
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

    private static double checkCoupling(ClassNode classNode){
        double couplingScore = 0;
        List<FieldNode> fields = (List<FieldNode>) classNode.fields;
        HashMap<String, Integer> Primary =  new HashMap<String, Integer>();
        Primary.put("String", 1);
        Primary.put("int", 1);
        Primary.put("char", 1);
        Primary.put("long", 1);
        Primary.put("double", 1);
        for (FieldNode field : fields) {
            Type fieldType;
            if(field.signature == null) {
                fieldType = Type.getType(field.desc);
            } else {
                fieldType = Type.getType(field.signature);
            }
            String name = fieldType.getClassName();
            if(!Primary.containsKey(name)){
                couplingScore*=1.4;
                couplingScore++;

            }



        }
        return couplingScore;
    }

    private static String CreateUML(ClassNode classNode){
        String uml = "";
        uml += "+class " + classNode.name.substring(classNode.name.lastIndexOf("/") + 1) + "{\n";
        //List<MethodNode> methods = (List<MethodNode>) classNode.methods;

        List<FieldNode> fields = (List<FieldNode>) classNode.fields;
        HashMap<String, Integer> Primary =  new HashMap<String, Integer>();
        Primary.put("String", 1);
        Primary.put("int", 1);
        Primary.put("char", 1);
        Primary.put("long", 1);
        Primary.put("double", 1);
        for (FieldNode field : fields) {
            Type fieldType;
            if(field.signature == null) {
                fieldType = Type.getType(field.desc);
            } else {
                fieldType = Type.getType(field.signature);
            }
            String name = fieldType.getClassName();
            System.out.println("\n" + name +"\n");
            if(name.contains("Array")){
                for(String key: Primary.keySet()){
                    if(name.contains(key)){
                        uml += getAccess(field.access) + field.name +  ": List<" + key + ">" + "\n" ;
                    }
                }
            }
            if(Primary.containsKey(name)){
                uml += getAccess(field.access) + field.name + ": " + name + "\n" ;
            }
        }
        uml+="}";
        return uml;
    }

    private static String getAccess(int acces){
        switch (acces){
            case (1):
                return "+";
            case (2):
                return "-";

        }
        return "-";
    }

}
