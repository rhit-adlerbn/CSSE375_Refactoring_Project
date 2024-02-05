package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GrammarCheck extends BasicLintern{



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


            PrintAllVariables(classNode);





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





}
