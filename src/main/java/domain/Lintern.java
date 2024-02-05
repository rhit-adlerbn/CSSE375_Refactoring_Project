package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Lintern extends BasicLintern{

    String[] fieldForAnalysisByThisProgram = new String[1];

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
        // TODO: Learn how to create separate Run Configurations so you can run
        // 		 your code on different programs without changing the code each time.
        //		 Otherwise, you will just see your program runs without any output.
//		args = new String[1];
//		args[0] = "MyFirstLinter";
        for (String className : args) {
            // One way to read in a Java class with ASM:
            // Step 1. ASM's ClassReader does the heavy lifting of parsing the compiled Java class.
            ClassReader reader = new ClassReader(className);

            // Step 2. ClassNode is just a data container for the parsed class
            ClassNode classNode = new ClassNode();

            // Step 3. Tell the Reader to parse the specified class and store its data in our ClassNode.
            // EXPAND_FRAMES means: I want my code to work. (Always pass this flag.)
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);

            // Now we can navigate the classNode and look for things we are interested in.
            //printClass(classNode);

            //printFields(classNode);

            //printMethods(classNode);
            PrintAllVariables(classNode);


            System.out.println(checkObserverPattern(classNode));


        }
    }









    private static void PrintAllVariables(ClassNode classNode){

        List<MethodNode> methods = (List<MethodNode>) classNode.methods;
        for (MethodNode method : methods) {
            printVariables(method);
        }
    }
    private static void printVariables(MethodNode methodNode){
        InsnList instructions = methodNode.instructions;
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        HashMap<Integer, String> variables = new HashMap<Integer, String>();


        Type[] argumentTypes = Type.getArgumentTypes(methodNode.desc);

        for (int i = 0; i < argumentTypes.length; i++) {
            map.put(i, 1);
            variables.put(i, argumentTypes[i].getClassName());
        }

        for (LocalVariableNode localVar : methodNode.localVariables) {
            variables.put(localVar.index, localVar.name);

        }

        for (int i = 0; i < instructions.size(); i++) {
            AbstractInsnNode insn = instructions.get(i);
            //System.out.println(insn.getOpcode());
            if (insn.getType() == 2) {

                VarInsnNode varInsn = (VarInsnNode) insn;
               // System.out.println("Variable name: " + variables.get(varInsn.var)+" " + varInsn.getOpcode());
                if(map.containsKey(varInsn.var)){
                    if(varInsn.getOpcode() != 54) {
                        map.put(varInsn.var, 2);
                    }
                } else {
                    map.put(varInsn.var, 1);
                }
            }

        }

        for(Integer var: map.keySet()){
            if(map.get(var) == 1){
                System.out.println("Unused Variable "+ "\""+variables.get(var)+"\"." + " In method " + methodNode.name);
            }

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
